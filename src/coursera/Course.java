package coursera;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Dhulkee
 * 
 * LinkedHashMap keeps the same order found in the file
 *
 */
public class Course extends LinkedHashMap<WeekTitle, List<Video>> {

	private CourseName courseName;

	
	public Course() {}
	
	public Course(CourseName courseName) {
		
		this.courseName = courseName;
		
	}
	
	//Move the parsing to a method
	public Course(String name, String courseHtmlPage) {

		String courseFileName = coursera.Utils.normalizeFileName(name);
		courseName = new CourseName(name, courseFileName);
		
		new CourseHtmlPageParser(this).parse(courseHtmlPage);
	}
	
	/**
	 * Constructs a Course object from a list of Videos
	 * @param videos
	 * @param courseName
	 * @return
	 */
	public static Course parse(List<Video> videos, CourseName courseName) {
		
		Course course = new Course(courseName);
		
		for(Video video : videos) {

			if(video.getCourseName() != courseName) continue;
			
			WeekTitle weekTitle = video.getWeekTitle();
			
			course.add(weekTitle, video);
		}
		
		return course;
	}
	
	/**
	 * Adds the downloadItem to the list in this Course Map
	 * @param weekTitle
	 * @param downloadItem
	 */
	public void add(WeekTitle weekTitle, Video video) {
		if(get(weekTitle) == null) {
			ArrayList<Video> list = new ArrayList<Video>();
			list.add(video);
			put(weekTitle, list);
		} else
			get(weekTitle).add(video);
	}
	
	/**
	 * 
	 * @return The course's title
	 */
	public CourseName getCourseName() {
		return courseName;
	}

	public void setCourseName(CourseName courseName) {
		this.courseName = courseName;
	}

}
