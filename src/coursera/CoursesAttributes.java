package coursera;

import java.util.ArrayList;

public class CoursesAttributes extends ArrayList<CourseAttributes> {

	public CoursesAttributes(String list2Json) {
		new List2Parser(this).parse(list2Json);
	}
}
