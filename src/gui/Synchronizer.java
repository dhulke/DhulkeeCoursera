package gui;

import gui.ConfigurationDialog.ButtonPressed;
import gui.ToolBar.ConnectedState;

import java.awt.Window;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.ClosedByInterruptException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import coursera.AbstractVideoVisitor;
import coursera.CourseName;
import coursera.Courses;
import coursera.Downloader;
import coursera.DownloaderListener;
import coursera.Video;
import coursera.VideoVisitResult;
import coursera.Web;
import coursera.WeekTitle;

public class Synchronizer {

	private Web web;
	private SynchronizeCourses synchronizeCourses;

	// Configurations
	private Path destinationDirectory;
	private boolean overwrite;
	private boolean index;

	public ProgressDialog progressDialog;

	/*
	 * Change this to a list of Course
	 */

	public Synchronizer(Window window, String cookies,
			ConfigurationDialog configurationDialog) {

		web = new Web(cookies);

		configurationDialog.setModal(true);
		configurationDialog.setVisible(true);

		if (configurationDialog.getButtonPressed().equals(ButtonPressed.CANCEL))
			return;

		destinationDirectory = configurationDialog.getDirectory();
		overwrite = configurationDialog.getOverwrite();
		index = configurationDialog.getIndex();

		progressDialog = new ProgressDialog(window, "Downloading videos");
		progressDialog.setVisible(true);

		progressDialog.setProgressListener(new SynchronizerProgressListener());

		ToolBar.getInstace().setConnectedState(ConnectedState.LOADING);

		synchronizeCourses = new SynchronizeCourses();
		synchronizeCourses.execute();
	}

	class SynchronizeCourses extends SwingWorker<Void, Integer> {

		/*
		 * current file, week and course being downloaded. We use these
		 * addresses to delete the half downloaded files if a cancel operation
		 * is fired
		 */

		private Path currentCoursePath;
		private Path currentWeekPath;
		private Path currentVideoPath;

		@Override
		protected Void doInBackground() throws Exception {

			// All the selected nodes
			TreePath[] treePaths = CoursesPanel.getInstace().getTree()
					.getCheckingPaths();

			List<Video> videos = new ArrayList<>();

			for (TreePath treePath : treePaths) {

				if (isCancelled())
					return null; // right before some heavy processing

				DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) treePath
						.getLastPathComponent();

				// just process Video objects
				if (!(mutableTreeNode.getUserObject() instanceof Video))
					continue;

				Video video = (Video) mutableTreeNode.getUserObject();

				// Add all the videos to a list to be turned into a list of
				// courses by Courses.parse()
				videos.add(video);
			}

			/*
			 * Turns all the videos into a Courses object so that we can use
			 * Courses.walkCourseTree() to download all the videos
			 */
			if (isCancelled())
				return null; // right before some heavy processing

			Courses courses = Courses.parse(videos);

			Courses.walkCourseTree(courses, new SynchronizerVideoVisitor());

			return null;
		}

		@Override
		protected void done() {
			// Finishing operations

			/*
			 * If a connection problem happened, don't close the window so that
			 * the user can read the message
			 */
			if (web.isConnectionProblem()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						progressDialog.setProgress(progressDialog
								.getMaxProgress());
						progressDialog.appendMessage(web
								.getConnectionProblemMessage());
						progressDialog.switchOk();
					}
				});

				return;
			}

			if (isCancelled()) {
				// Delete half downloaded files
				deleteIfEmpty(currentVideoPath, currentWeekPath,
						currentCoursePath);
			}

			progressDialog.switchOk();

			ToolBar.getInstace().setConnectedState(ConnectedState.DOWNLOADABLE);
		}

		class SynchronizerVideoVisitor extends AbstractVideoVisitor {

			Path coursePath;
			Path weekPath;

			int filePosition = 0;
			int totalVideos = 0;

			@Override
			public VideoVisitResult beforeAnything(final int totalVideos) {

				if (isCancelled())
					return VideoVisitResult.TERMINATE;

				this.totalVideos = totalVideos;

				progressDialog.appendMessageEDT(String.format(
						"You selected a total of %s videos to download.",
						totalVideos));

				return VideoVisitResult.CONTINUE;
			}

			@Override
			public VideoVisitResult beforeCourse(CourseName courseName,
					int numberOfWeeks, int totalCourseVideos) {

				if (isCancelled())
					return VideoVisitResult.TERMINATE;

				String courseFileName = courseName.getNameFileName();

				coursePath = destinationDirectory.resolve(courseFileName);

				createDirectory(coursePath);

				// This will be used to delete this directory when the user
				// stops the download
				currentCoursePath = coursePath;

				progressDialog.appendMessageEDT(String.format(
						"%nCourse: %s [ %s videos]", courseFileName,
						totalCourseVideos));

				return VideoVisitResult.CONTINUE;
			}

			@Override
			public VideoVisitResult beforeWeek(WeekTitle weekTitle,
					int numberOfDownloadItems) {

				if (isCancelled())
					return VideoVisitResult.TERMINATE;

				String weekFileName = weekTitle.getTitleFileName();

				if (index)
					weekFileName = weekTitle.getIndex() + " - " + weekFileName;

				weekPath = coursePath.resolve(weekFileName);

				createDirectory(weekPath);

				// This will be used to delete this directory when the user
				// stops the download
				currentWeekPath = weekPath;

				progressDialog.appendMessageEDT(String.format(
						"%nWeek: %s [%s videos]%n", weekFileName,
						numberOfDownloadItems));

				return VideoVisitResult.CONTINUE;
			}

			@Override
			public VideoVisitResult video(Video video) {

				if (isCancelled())
					return VideoVisitResult.TERMINATE;

				++filePosition;

				String videoFileName = video.getVideoFileName();

				if (index)
					videoFileName = video.getIndex() + " - " + videoFileName;

				currentVideoPath = weekPath.resolve(videoFileName);

				if (Files.exists(currentVideoPath) && !overwrite) {
					progressDialog.appendMessageEDT(String.format(
							"File %s already exists.", videoFileName));

					return VideoVisitResult.CONTINUE;
				} else
					progressDialog.appendMessageEDT(String.format(
							"%s [%s/%s] Downloading...", videoFileName,
							filePosition, totalVideos));

				try {
					URL source = new URL(video.getVideoUrl());
					Downloader downloader = web.getDownloader(source,
							currentVideoPath, 4096); // 1048576 1MB
					int contentLength = web.getContentLength();
					downloader
							.setDownloaderListener(new VideoDownloaderListener(
									contentLength));
					downloader.download();
				} catch (ClosedByInterruptException e) {
					return VideoVisitResult.TERMINATE;
				} catch (IOException e) {
					System.err
							.println("Some problem occurred while downloading files");
					return VideoVisitResult.TERMINATE;
				}

				progressDialog.appendMessageEDT("Done.");

				return VideoVisitResult.CONTINUE;
			}

			@Override
			public VideoVisitResult afterCourse(CourseName courseName,
					int numberOfWeeks, int totalDownloadItems) {

				String courseFileName = courseName.getNameFileName();

				progressDialog.appendMessageEDT(String.format(
						"Course: %s successfully downloaded.%n%n",
						courseFileName));

				return VideoVisitResult.CONTINUE;
			}

		}

		class VideoDownloaderListener implements DownloaderListener {

			private int totalSize;

			public VideoDownloaderListener(int totalSize) {
				this.totalSize = totalSize;
			}

			@Override
			public void beforeDownloading() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {

						// If couldn't retrieve size from header, set the
						// progressDialog to indeterminate
						boolean unknownValue = totalSize == -1;
						if (unknownValue) {
							progressDialog.setIndeterminate(true);
						} else {
							progressDialog.setIndeterminate(false);
						}
						progressDialog.setMaxProgress(totalSize);
						progressDialog.setMinProgress(0);
						progressDialog.setProgress(0);
					}
				});
			}

			@Override
			public void downloading(final int sizeDownloaded) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int currentProgress = progressDialog.getProgress();
						progressDialog.setProgress(currentProgress
								+ sizeDownloaded);
					}
				});
			}

			@Override
			public void afterDownloading() {
			}

			@Override
			public void stoppedDownloading() {
			}
		}

	}

	class SynchronizerProgressListener implements ProgressDialogListener {

		@Override
		public void progressCancelled() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					web.getDownloader().stop();
					synchronizeCourses.cancel(true);
					progressDialog.setVisible(false);
				}
			});
		}

		@Override
		public void progressOk() {

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					progressDialog.setVisible(false);
				}
			});
		}
	}

	private void deleteIfEmpty(Path videoPath, Path weekPath, Path coursePath) {
		/*
		 * It isn't deleting the course folder.
		 */
		try {
			Files.deleteIfExists(videoPath);

			if (isEmpty(weekPath)) {
				Files.deleteIfExists(weekPath);

				if (isEmpty(coursePath))
					Files.deleteIfExists(coursePath);
			}
		} catch (IOException e) {
		}

	}

	private boolean isEmpty(Path directory) {
		try (DirectoryStream<Path> directoryStream = Files
				.newDirectoryStream(directory)) {
			return !directoryStream.iterator().hasNext();
		} catch (IOException e) {
		}
		return false;
	}

	private void createDirectory(Path directory) {
		if (!Files.exists(directory))
			try {
				Files.createDirectories(directory);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
