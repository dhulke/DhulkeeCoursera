package coursera;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.net.ssl.HttpsURLConnection;

public class Web {
	
	private static final String LIST2JSON = "https://www.coursera.org/maestro/api/topic/list2_combined";
	
	private String cookies;
	
	private Downloader downloader;
	
	private WebListener webListener;
	private boolean connectionProblem = false;
	private String connectionProblemMessage;
	
	private int contentLength = -1;
	
	
	/**
	 * 
	 * @param cookies For now, this is how we authenticate. Will be changed in the future
	 */
	
	public Web(String cookies, Downloader mainDownloader) {
		this.cookies = cookies;
		this.downloader = mainDownloader;
	}
	
	public Web(String cookies) {
		this(cookies, new Downloader());
	}
	
	/**
	 * 
	 * @return json file from https://www.coursera.org/maestro/api/topic/list2_combined
	 */
	public String getList2Json() {
		String page = "";
		try {
			page = getPage(new URL(LIST2JSON));
		} catch (MalformedURLException e) { e.printStackTrace();}
		return page;
	}
	
	public String getCourseHtmlPage(String courseUrl) {
		URL courseHtmlPage = null;
		try {
			 courseHtmlPage = new URL(courseUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return getCourseHtmlPage(courseHtmlPage);
	}
	
	public String getCourseHtmlPage(URL courseUrl) {
		URL courseHtmlPage = null;
		try {
			courseHtmlPage = new URL(courseUrl, "lecture");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return getPage(courseHtmlPage);
	}
	
	
	private String getPage(URL url) {
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
	    try {
	    	download(url, ba);
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    return ba.toString();
	}
	

	private void download(URL url, Path destination) throws IOException {
		download(url, Files.newOutputStream(destination));
	}
	
	//Still have to implement events or something like that for the progressbar
	private void download(URL url, OutputStream os) throws IOException {
		InputStream is = getInputStream(url);
		downloader.setInputStream(is);
		downloader.setOutputStream(os);
		downloader.download();
	}
	
	InputStream getInputStream(URL url) throws IOException {
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.addRequestProperty("Cookie", cookies);
		
		String contentLength = connection.getHeaderField("Content-Length");
		try {
			this.contentLength = Integer.parseInt(contentLength);
		} catch (NumberFormatException e1) {
			//in case we don't get a size
			this.contentLength = -1;
		}
		
    	InputStream is = null;
		try {//to be able to tell the user that a problem has happened
			is = connection.getInputStream();
		} catch (ConnectException | UnknownHostException e) {
			connectionProblem = true;
			connectionProblemMessage = "\n*****A connection problem has just happened*****\n" +
								"Either Coursera is out or your internet is down.\n" +
								"For further assistance, please contact me: dhulkeecourseradownloader@gmail.com";
			fireConnectionTimeOutOccurred();
		}
	    return is;
	}
	
	public int getContentLength() {
		return contentLength;
	}

	public Downloader getDownloader(URL source, Path destination, int chunckSize) throws IOException {
		downloader.setChunckSize(chunckSize);
		return getDownloader(source, destination);
	}
	
	public Downloader getDownloader(URL source, Path destination) throws IOException {
		InputStream is = getInputStream(source);
		OutputStream os = Files.newOutputStream(destination);
		downloader.setInputStream(is);
		downloader.setOutputStream(os);
		return downloader;
	}

	public Downloader getDownloader() {
		return downloader;
	}
	
	private void fireConnectionTimeOutOccurred() {
		if(webListener != null)
			webListener.connectionProblemOccurred();
	}

	public WebListener getWebListener() {
		return webListener;
	}

	public void setWebListener(WebListener webListener) {
		this.webListener = webListener;
	}

	public boolean isConnectionProblem() {
		return connectionProblem;
	}
	
	public String getConnectionProblemMessage() {
		return connectionProblemMessage;
	}
	
}
