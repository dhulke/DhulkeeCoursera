package coursera;

public class CourseName {

	private String name;
	private String nameFileName;
	
	public CourseName(String name, String nameFileName) {
		this.name = name;
		this.nameFileName = nameFileName;
	}
	
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameFileName() {
		return nameFileName;
	}

	public void setNameFileName(String nameFileName) {
		this.nameFileName = nameFileName;
	}

}
