package coursera;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * 
 * @author Dhulkee
 *
 * This class parses the Json file found at https://www.coursera.org/maestro/api/topic/list2_combined
 * This file contains information about all the courses the user currently logged in is or has been 
 * enrolled in. An example of this file is in the SampleResources folder.
 * 
 * It has this structure: 
 * 
 * {
 *	"enrollments" : [...], 
 *	"list2" : { 
 *  	"courses" : [{},...], 
 *  	"cats" : [{},...], 
 *  	"insts" : [{},...], 
 *  	"unis" : [{},...], 
 *  	"topics" : { 
 *  		"..." : {...},
 *  			...
 *  	}
 *  }
 * }
 * 
 * The parser just gets a list of all the courses in the "enrollments" attribute and uses the field
 * course_id to select the same courses in the array of the attribute "courses" under "list2". In the courses
 * array, it uses the field topic_id to get the name of the topic from the attribute "topics" under "list2".
 * This is necessary because the attribute "courses" contains links to the homepages of each course. This way we can
 * find the lectures page and proceed to download the videos and other resources.
 */

public class List2Parser {

	CoursesAttributes coursesAttributes;
	
	public List2Parser(CoursesAttributes coursesAttributes) {
		this.coursesAttributes = coursesAttributes;
	}

	public void parse(String json) {

		JSONObject courseraJson = (JSONObject) JSONValue.parse(json);
		_parse(courseraJson);
	}
	
	public void parse(Reader reader) {

		JSONObject courseraJson = (JSONObject) JSONValue.parse(reader);
		_parse(courseraJson);
	}

	private void _parse(JSONObject courseraJson) {

		/*
		 * Selects all the courses from list2["courses"] that are also in enrollments
		 * and gets the name of the topic from list2["topics"] through list2["courses"].topic_id
		 */
		JSONArray enrollments = (JSONArray) courseraJson.get("enrollments");
		JSONObject list2 = (JSONObject) courseraJson.get("list2");
		JSONArray courses = (JSONArray) list2.get("courses");
		JSONObject topics = (JSONObject) list2.get("topics");

		for (Object enrollmentObj : enrollments) {
			JSONObject enrollment = (JSONObject) enrollmentObj;
			Number course_id = (Number) enrollment.get("course_id");
			for (Object courseObj : courses) {
				JSONObject course = (JSONObject) courseObj;
				Number id = (Number) course.get("id");
				if (id.equals(course_id)) {

					CourseAttributes courseAttributes = new CourseAttributes();

					String topic_id = String.valueOf((Number) course.get("topic_id"));

					/*
					 * the topics subtree has an id as key, which is topic_id
					 */
					JSONObject topic = (JSONObject) topics.get(topic_id);

					courseAttributes.setName((String) topic.get("name"));
					courseAttributes.setShort_name((String) topic.get("short_name"));
					
					courseAttributes.setStatus((Number) course.get("status"));
					courseAttributes.setActive((boolean) course.get("active"));
					courseAttributes.setDeployed((boolean) course.get("deployed"));
					courseAttributes.setHome_link((String) course.get("home_link"));

					coursesAttributes.add(courseAttributes);
				}
			}
		}
	}
	
}
