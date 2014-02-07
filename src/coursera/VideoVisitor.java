package coursera;

public interface VideoVisitor {

	public VideoVisitResult beforeAnything(int totalVideos);
	public VideoVisitResult afterEverything(int totalVideos);
	public VideoVisitResult beforeCourse(CourseName courseName, int numberOfWeeks, int totalCourseVideos);
	public VideoVisitResult afterCourse(CourseName courseName, int numberOfWeeks, int totalCourseVideos);
	public VideoVisitResult beforeWeek(WeekTitle weekTitle, int numberOfVideos);
	public VideoVisitResult afterWeek(WeekTitle weekTitle, int numberOfVideos);
	public VideoVisitResult video(Video video);
	
}
