package coursera;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CourseHtmlPageParser {
	
	private Course course;
	
	
	public CourseHtmlPageParser(Course course) {
		this.course = course;
	}
	
	public void parse(Path file) {
		try {
			Document document = Jsoup.parse(file.toFile(), "UTF-8");
			_parse(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parse(String html) {
		try {
			Document document = Jsoup.parse(html);
			_parse(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void _parse(Document document) throws IOException {

		// Get the first course-item-list (there's only 1 per page
		Element divCourse = document.body().select("div.course-item-list").first();
		if(divCourse == null)
			return;
		// div.course-item-list-header h3

		int weekIndex = 0;
		int videoIndex = 0;		
		
		// goes through all the week headers
		Iterator<Element> headerDivIterator = divCourse.select("div.course-item-list-header").iterator();
		while (headerDivIterator.hasNext()) {

			Element divHeader = headerDivIterator.next();
			
			// week title getting past the character &nbsp; that is converted to 0xA0 and not cut off by trim()
			String weekTitle = divHeader.getElementsByTag("h3").first().text().substring(1);
			
			//Gathering data for the Video object
			weekIndex++;
			videoIndex = 0;
			String weekFileName = coursera.Utils.normalizeFileName(weekTitle);
			WeekTitle week = new WeekTitle(weekTitle, weekFileName, weekIndex);
			
			
			// right after each week header is the ul list of videos
			Element ulList = (Element) divHeader.nextSibling();
			//li's
			Iterator<Element> liIterator = ulList.children().iterator();
			while(liIterator.hasNext()) {
				Element li = liIterator.next();
				
				Element resourceDiv = li.children().last();
				if(resourceDiv.children().isEmpty() || !resourceDiv.children().last().attr("title").equals("Video (MP4)"))
					continue;

				// Video Name				
				String videoName = li.getElementsByTag("a").first().text();

				/*
				 * Download link last child of this li is the div with the
				 * resources and last child of this div is the link for the
				 * video
				 */
				String videoUrl = resourceDiv.children().last().attr("href");
				
				videoIndex++;
				
				CourseName courseName = course.getCourseName();
				String videoFileName = coursera.Utils.normalizeFileName(videoName + ".mp4");
				Video video = new Video(courseName, week, videoName, videoFileName, videoUrl, videoIndex);
				
				course.add(week, video);
			}
			
		}
	}
}
