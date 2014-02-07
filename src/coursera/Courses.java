package coursera;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Courses extends ArrayList<Course> {

	/**
	 * Creates a Courses object from a list of videos
	 * @param videos
	 * @return
	 */
	public static Courses parse(List<Video> videos) {

		Courses courses = new Courses();
		Map<CourseName, List<Video>> map = new LinkedHashMap<>();

		/*
		 * Constructs a structure of CourseName and Videos to be parsed by
		 * Course.parse()
		 */
		for (Video video : videos) {

			CourseName courseName = video.getCourseName();

			List<Video> list = map.get(courseName);

			if (list == null) {

				list = new ArrayList<>();
				list.add(video);

				map.put(courseName, list);
			} else {

				list = map.get(courseName);
				list.add(video);
			}
		}

		/*
		 * Iterate through that structure parsing it with Course.parse()
		 */
		for (Map.Entry<CourseName, List<Video>> entry : map.entrySet()) {

			CourseName courseName = entry.getKey();
			List<Video> localVideos = entry.getValue();

			Course course = Course.parse(localVideos, courseName);

			courses.add(course);
		}

		return courses;
	}

	public static void walkCourseTree(Courses courses,
			VideoVisitor videoVisitor) {

		if (courses.isEmpty())
			return;

		int totalVideos = 0;
		
		
		//calculates the total number of videos in courses 
		for(Course course : courses)
			totalVideos += totalVideos(course);


		switch (videoVisitor.beforeAnything(totalVideos)) {
		case TERMINATE:
			return;
		case CONTINUE:
		default:
			break;
		}

		for (Course course : courses) {

			int totalCourseVideos = totalVideos(course);
			int numberOfWeeks = course.size();
			CourseName courseName = course.getCourseName();

			switch (videoVisitor.beforeCourse(courseName, numberOfWeeks,
					totalCourseVideos)) {
			case TERMINATE:
				return;
			case CONTINUE:
			default:
				break;
			}

			// Iterates through the map
			for (Map.Entry<WeekTitle, List<Video>> entry : course.entrySet()) {

				int numberOfVideos = entry.getValue().size();
				WeekTitle weekTitle = entry.getKey();

				switch (videoVisitor.beforeWeek(weekTitle, numberOfVideos)) {
				case SKIP_WEEK:
					continue;
				case TERMINATE:
					return;
				case CONTINUE:
				default:
					break;
				}

				VideosLoop: for (Video video : entry.getValue())
					switch (videoVisitor.video(video)) {
					case SKIP_SIBLINGS:
						break VideosLoop;
					case TERMINATE:
						return;
					case CONTINUE:
					default:
						break;
					}

				switch (videoVisitor.afterWeek(weekTitle, numberOfVideos)) {
				case SKIP_WEEK:
					continue;
				case TERMINATE:
					return;
				case CONTINUE:
				default:
					break;
				}
			}

			switch (videoVisitor.afterCourse(courseName, numberOfWeeks,
					totalCourseVideos)) {
			case TERMINATE:
				return;
			case CONTINUE:
			default:
				break;
			}
		}
		
		switch (videoVisitor.afterEverything(totalVideos)) {
		case TERMINATE:
			return;
		case CONTINUE:
		default:
			break;
		}

	}
	
	private static int totalVideos(Course course) {
		int totalVideos = 0;
		for(List<Video> videos : course.values())
			totalVideos += videos.size();
		return totalVideos;
	}
}
