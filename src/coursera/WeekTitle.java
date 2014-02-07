package coursera;

public class WeekTitle {

	private String title;
	private String titleFileName;
	private int index;
	
	public WeekTitle(String title, String titleFileName, int index) {
		this.title = title;
		this.titleFileName = titleFileName;
		this.index = index;
	}
	
	public String toString() {
		return title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleFileName() {
		return titleFileName;
	}

	public void setTitleFileName(String titleFileName) {
		this.titleFileName = titleFileName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
