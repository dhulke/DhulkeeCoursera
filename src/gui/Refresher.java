package gui;

import gui.ToolBar.ConnectedState;

import java.awt.Window;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import coursera.AbstractVideoVisitor;
import coursera.Course;
import coursera.CourseAttributes;
import coursera.CourseName;
import coursera.Courses;
import coursera.CoursesAttributes;
import coursera.Video;
import coursera.VideoVisitResult;
import coursera.VideoVisitor;
import coursera.Web;
import coursera.WeekTitle;

public class Refresher {
	
	private ProgressDialog progressDialog;
	
	private DefaultTreeModel treeModel;
	private DefaultMutableTreeNode root;
	
	private LoadCourses loadCourses;
	
	private Web web;


	public Refresher(Window window, String cookies) {
		
		web = new Web(cookies);
		
		progressDialog = new ProgressDialog(window, "Retrieving list of videos");
		progressDialog.setVisible(true);
		
		progressDialog.setProgressListener(new RefreshProgressListener());
		
		ToolBar.getInstace().setConnectedState(ConnectedState.LOADING);
		
		root = new DefaultMutableTreeNode();
		treeModel = new DefaultTreeModel(root);
		
		//erase whats already there
		CoursesPanel.getInstace().setTreeModel(treeModel);
		
		loadCourses = new LoadCourses();
		loadCourses.execute();
	}
	
	
	class LoadCourses extends SwingWorker<Void, Void> {
		
		private VideoVisitor videoVisitor;
		
		
		public LoadCourses() {
			this.videoVisitor = new TreeVideoVisitor();
		}
		
		
		@Override
		protected Void doInBackground() throws Exception {
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override public void run() {
					progressDialog.appendMessage("Retrieving your list of courses...");
					progressDialog.setIndeterminate(true);
				}
			});

			String list2Json = web.getList2Json();

			if(isCancelled()) return null; //right before some heavy processing

			CoursesAttributes coursesAttributes = new CoursesAttributes(list2Json);
			
			progressDialog.appendMessageEDT("Done.\n");
			
			int totalProgress = 0;
			for(CourseAttributes courseAttributes : coursesAttributes) {
				
				if(isCancelled()) return null; //right before some heavy processing
				
				if(courseAttributes.isActive())
					totalProgress++;
			}
			
			final int finalTotalProgress = totalProgress;

			SwingUtilities.invokeLater(new Runnable() {
				@Override public void run() {
					progressDialog.setMaxProgress(finalTotalProgress);
					progressDialog.setIndeterminate(false);
				}
			});
			
			int progress = 0;
			Courses courses = new Courses();

			for(CourseAttributes courseAttributes : coursesAttributes) {

				if(isCancelled()) return null;

				if(!courseAttributes.isActive()) continue;
					
				String courseUrl = courseAttributes.getHome_link();
				String name = courseAttributes.getName();
				
				progressDialog.appendMessageEDT("Retrieving list of videos from [ " + name + " ]");

				String courseHtmlPage = web.getCourseHtmlPage(courseUrl);
				
				Course course = new Course(name, courseHtmlPage);
				
				//Processing will be done later
				courses.add(course);
				
				final int finalProgress = ++progress;
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						progressDialog.appendMessage("Done.\n");
						progressDialog.setProgress(finalProgress);						
					}
				});
			}
			
			Courses.walkCourseTree(courses, videoVisitor);
			
			return null;
		}
		
		@Override
		protected void done() {
			/*
			 * If a connection problem happened, don't close the window so that
			 * the user can read the message
			 */
			if(web.isConnectionProblem()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override public void run() {
						progressDialog.setIndeterminate(false);
						progressDialog.setProgress(progressDialog.getMaxProgress());
						progressDialog.appendMessage(web.getConnectionProblemMessage());
					}
				});
				
				return;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override public void run() {
					
					if(!isCancelled()) {
						CoursesPanel.getInstace().setTreeModel(treeModel);
						ToolBar.getInstace().setConnectedState(ConnectedState.DOWNLOADABLE);
					} else {
						ToolBar.getInstace().setConnectedState(ConnectedState.CONNECTED);
					}
					
					progressDialog.setVisible(false);					
				}
			});
		}




		class TreeVideoVisitor extends AbstractVideoVisitor {
			
			DefaultMutableTreeNode course;
			DefaultMutableTreeNode week;

			
			@Override
			public VideoVisitResult beforeCourse(CourseName courseName, int numberOfWeeks, int totalDownloadItems) {

				if(isCancelled()) return VideoVisitResult.TERMINATE;
				
				course = add(courseName, root);
				
			    return VideoVisitResult.CONTINUE;
			}
		
			@Override
			public VideoVisitResult beforeWeek(WeekTitle weekTitle,	int numberOfDownloadItems) {

				if(isCancelled()) return VideoVisitResult.TERMINATE;
				
				week = add(weekTitle, course);
				
				return VideoVisitResult.CONTINUE;
			}
		
			@Override 
			public VideoVisitResult video(Video video) {
				
				if(isCancelled()) return VideoVisitResult.TERMINATE;
				
				add(video, week);
				
				return VideoVisitResult.CONTINUE;
			}
		
		}
		
	}
	
	
	class RefreshProgressListener implements ProgressDialogListener {
		@Override public void progressCancelled() {
			web.getDownloader().stop();
			loadCourses.cancel(true);
		}

		@Override public void progressOk() {}
	}
	
	private DefaultMutableTreeNode add(Object name, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(name);
		treeModel.insertNodeInto(treeNode, parent, parent.getChildCount());
		return treeNode;
	}
}
