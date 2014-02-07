package coursera;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class SynchronizerDownloadItemVisitor implements VideoVisitor {
	
	private Path destination;
	private Path weekDirectory;
	private Web web;
	private boolean overwrite;
	
	//Used between event calls
	private int filePosition;
	private int numberOfDownloadItems;
	

	public SynchronizerDownloadItemVisitor(Web web, Path destination, boolean overwrite) {
		this.destination = destination;
		this.web = web;
		this.overwrite = overwrite;
		filePosition = 0;
	}
	
	
	@Override
	public VideoVisitResult beforeCourse(CourseName courseName, int numberOfWeeks, int totalDownloadItems) {
		
		destination = destination.resolve(courseName.getNameFileName());
		if(!Files.exists(destination))
			try {
				Files.createDirectories(destination);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		System.out.format("%n[ %s ]%n%n", courseName);
		
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult beforeWeek(WeekTitle weekTitle, int numberOfDownloadItems) {
		
		this.numberOfDownloadItems = numberOfDownloadItems;
		
		//Resets the file position counter for the files in this week
		filePosition = 0;
		
		weekDirectory = destination.resolve(weekTitle.getTitleFileName()); 
		if(!Files.exists(weekDirectory))
			try {
				Files.createDirectories(weekDirectory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.format("%nScanning week: %s [%s videos]%n%n", weekTitle, numberOfDownloadItems);
		
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult video(Video video) {
		
		++filePosition;
		String videoName = Utils.normalizeFileName(video.getVideoName() + ".mp4");
		Path videoDestinationPath = weekDirectory.resolve(videoName);
		
		if(Files.exists(videoDestinationPath) && !overwrite) {
			System.out.format("File %s already exists.%n", videoName);
			return VideoVisitResult.CONTINUE;
		} else
			System.out.format("%n%s [%s/%s] Downloading...%n", videoName, filePosition, numberOfDownloadItems);

		try {
			URL source = new URL(video.getVideoUrl());
			Downloader downloader = web.getDownloader(source, videoDestinationPath, 1048576); //1MB
			downloader.setDownloaderListener(new ItemDownloaderListener());
			downloader.download();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.format("Done.%n");
		
		
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult afterWeek(WeekTitle weekTitle, int numberOfDownloadItems) {

		System.out.format("%n%s files successfully downloaded%n", numberOfDownloadItems);
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult afterCourse(CourseName courseTitle, int numberOfWeeks, int totalDownloadItems) {

		System.out.format("%nCourse [ %s ] successfully downloaded.%n%n", courseTitle);
		return VideoVisitResult.CONTINUE;
	}
	
	
	private class ItemDownloaderListener implements DownloaderListener {
		
		@Override
		public void beforeDownloading() {}

		@Override
		public void downloading(int sizeDownloaded) {
			System.out.format("%s downloaded (%s Bs)%n", Utils.convertByteToStringRepresentation(sizeDownloaded), sizeDownloaded);
		}

		@Override
		public void afterDownloading() {}

		@Override
		public void stoppedDownloading() {
			
		}
		
	}


	@Override
	public VideoVisitResult beforeAnything(int totalVideos) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public VideoVisitResult afterEverything(int totalVideos) {
		// TODO Auto-generated method stub
		return null;
	}
}
