package coursera;

public class CourseAttributes {

	private Number status;
	//private List<Instructor> instructors;
	private boolean active;
	private boolean deployed;
	private String home_link;
	
	//These are in the topics subtree of list2
	
	private String name;
	private String short_name;
	
	
	public String toString() {
		return String.format("name = %-70s " +
				"status = %-3s " +
				"active = %-8s " +
				"deployed = %-8s " +
				"home_link = %s", name, status, active, deployed, home_link);
	}


	public Number getStatus() {
		return status;
	}


	public void setStatus(Number status) {
		this.status = status;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public boolean isDeployed() {
		return deployed;
	}


	public void setDeployed(boolean deployed) {
		this.deployed = deployed;
	}


	public String getHome_link() {
		return home_link;
	}


	public void setHome_link(String home_link) {
		this.home_link = home_link;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getShort_name() {
		return short_name;
	}


	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	

	
	
}
