package coursera;


public abstract class AbstractVideoVisitor implements VideoVisitor {
	
	
	
	@Override
	public VideoVisitResult beforeAnything(int totalVideos) {
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult afterEverything(int totalVideos) {
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult beforeCourse(CourseName courseName,
			int numberOfWeeks, int totalDownloadItems) {
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult afterCourse(CourseName courseName,
			int numberOfWeeks, int totalDownloadItems) {
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult beforeWeek(WeekTitle weekTitle,
			int numberOfDownloadItems) {
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult afterWeek(WeekTitle weekTitle,
			int numberOfDownloadItems) {
		return VideoVisitResult.CONTINUE;
	}

	@Override
	public VideoVisitResult video(Video video) {
		return VideoVisitResult.CONTINUE;
	}
}