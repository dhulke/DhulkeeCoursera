package coursera;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Connector {

	private static final String SIGNINURL = "https://accounts.coursera.org/api/v1/login";

	private int responseCode;
	private String cauthCookie;

	private String email;
	private String password;

	public Connector(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String connect() {

		try {
			String postData = String.format("email=%s&password=%s",
					URLEncoder.encode(email, "UTF-8"),
					URLEncoder.encode(password, "UTF-8"));

			URL url = new URL(SIGNINURL);

			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("X-CSRFToken", "a");
			connection.setRequestProperty("X-CSRF2-Cookie", "b");
			connection.setRequestProperty("X-CSRF2-Token", "c");
			connection.setRequestProperty("Cookie", "csrftoken=a; b=c");// Content-Type:
			connection.setRequestProperty("Referer", "Hell");
			connection.setRequestProperty("User-Agent", "Hades");

			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write(postData);
			out.flush();
			out.close();
			
			responseCode = connection.getResponseCode();

			Map<String, List<String>> headerMap = connection.getHeaderFields();
			
			List<String> cookies = headerMap.get("Set-Cookie");
			
			if(cookies == null)
				cauthCookie = null;
			else
				cauthCookie = getCAUTH(cookies);		

		} catch (IOException e) {
			e.printStackTrace();
		}

		return cauthCookie;
	}
	
	public int getResponseCode() {
		return responseCode;
	}

	private String getCAUTH(List<String> cookies) {

		String cookieCAUTH = null;

		for (String cookie : cookies) {
			if (cookie.startsWith("CAUTH="))
				cookieCAUTH = cookie;
		}

		if (cookieCAUTH == null)
			return null;

		int endIndex = cookieCAUTH.indexOf(";");

		cookieCAUTH = cookieCAUTH.substring(0, endIndex);

		return cookieCAUTH;
	}
}
