package coursera;


public class Video {

	private CourseName courseName;
	private WeekTitle weekTitle;

	private String videoName;
	private String videoUrl;
	
	private String videoFileName;
	
	private int index;
	
	
	public Video(CourseName courseName, WeekTitle weekTitle, String videoName,
			String videoFileName, String videoUrl, int index) {

		this.index = index;
		
		this.courseName = courseName;
		this.weekTitle = weekTitle;
		this.videoName = videoName;
		this.videoUrl = videoUrl;
		this.videoFileName = videoFileName;
	}

	public String toString() {
		return videoName;
	}

	public CourseName getCourseName() {
		return courseName;
	}

	public void setCourseName(CourseName courseName) {
		this.courseName = courseName;
	}

	public WeekTitle getWeekTitle() {
		return weekTitle;
	}

	public void setWeekTitle(WeekTitle weekTitle) {
		this.weekTitle = weekTitle;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
}
