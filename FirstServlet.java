

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Servlet implementation class FirstServlet
 */
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String HTML_START="<html><body>";
	public static final String HTML_END="</body></html>";

	/**
	 * Default constructor. 
	 */
	public FirstServlet() {
		// TODO Auto-generated constructor stub
	}
	//**
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("location")==null && request.getParameter("zip")==null) {
			response.sendRedirect("http://localhost:8080/j2eeapplication/index.jsp");
		}
		else {
			if((request.getParameter("location").equals("null") && request.getParameter("zip").equals(""))) {
				response.sendRedirect("http://localhost:8080/j2eeapplication/index.jsp");
			}
			else {
				doPost(request, response);
			}
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String location = request.getParameter("location");
		String distance = request.getParameter("distance");
		String zip = request.getParameter("zip");
		if((location.equals("null") && zip.equals("null"))) {
			response.sendRedirect("http://localhost:8080/j2eeapplication/index.jsp");
		}
		else {
			// Set response content type
			response.setContentType("text/html");
			try {
				 HttpSession session = request.getSession();
				String auth = getToken();
				String result;
				
				if(location.equals("null") ) {
					if(!request.getParameter("delivery").equals("Delivery")) {
						result =  getBusinesses(zip, auth, distance);
					}
					else {
						result =  getBusinessesDelivery(zip, auth, distance);
					}
					session.setAttribute("zip", zip);
				}
				else {
					if(!request.getParameter("delivery").equals("Delivery")) {
						result =  getBusinesses(location.substring(0, location.indexOf(",")), location.substring(location.indexOf(",")+1), auth, distance);
					}else {
						result = getBusinessesDelivery(location.substring(0, location.indexOf(",")), location.substring(location.indexOf(",")+1), auth, distance);
					}
					 session.setAttribute("location", location);
					
				}
				JSONObject jsonobject = (JSONObject) new JSONParser()
				.parse(result);

				if(jsonobject==null) request.getRequestDispatcher("/index.jsp").forward(request, response);
				JSONArray businesses = (JSONArray) jsonobject.get("businesses");
				
				 session.setAttribute("businesses", businesses);
				 session.setAttribute("auth", auth);
				
				 
				request.getRequestDispatcher("/bracket.jsp").forward(request, response);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	protected String getToken() throws Exception{
		String client_id = "iJUPQDxk-6lz7gz5CpfMCQ";
		String client_secret = "b1vKukbfvjb9hWeQXdai1sOqRH9Xqof2KEg2cjTwQ445tb2cLAAPPrvgqipF1iH5";
		String url ="https://api.yelp.com/oauth2/token";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);

		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("client_id", client_id));
		urlParameters.add(new BasicNameValuePair("client_secret", client_secret));
		httppost.setEntity(new UrlEncodedFormEntity(urlParameters));

		CloseableHttpResponse response = httpclient.execute(httppost);
		String responseString = EntityUtils.toString(response.getEntity());

		httpclient.close();
		response.close();

		JSONParser parser = new JSONParser();
		JSONObject tokenBearer =  (JSONObject) parser.parse(responseString);

		String access_token = (String) tokenBearer.get("access_token");
		String token_type = (String) tokenBearer.get("token_type");
		//Long expires_in = (Long) tokenBearer.get("expires_in");

		String authorization= token_type+" "+access_token;
		return authorization; 
	}

	protected String getBusinesses(String lat, String lon, String authorization, String distance) throws Exception {
		int radius = 1609;
		int dist = Integer.parseInt(distance);
		if(dist != 0) radius = 1609*dist;
		String url = "https://api.yelp.com/v3/businesses/search?term=restaurants&latitude="+lat+"&longitude="+lon+"&radius="+radius;
		//System.out.println(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		//httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		httpget.setHeader("Authorization", authorization);
		CloseableHttpResponse response = httpclient.execute(httpget);

		String responseString = EntityUtils.toString(response.getEntity());
		httpclient.close();
		//System.out.println(responseString);
		return responseString;
	}
	protected String getBusinesses(String zip, String authorization, String distance) throws Exception {
		int radius = 1609;
		int dist = Integer.parseInt(distance);
		if(dist != 0) radius = 1609*dist +3;
		String url = "https://api.yelp.com/v3/businesses/search?term=restaurants&location="+zip+"&radius="+radius;
		//System.out.println(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		//httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		httpget.setHeader("Authorization", authorization);
		CloseableHttpResponse response = httpclient.execute(httpget);
		String responseString = EntityUtils.toString(response.getEntity());
		//System.out.println(responseString);
		httpclient.close();
		return responseString;
	}
		protected String getBusinessesDelivery(String zip, String authorization, String distance) throws Exception {
		int radius = 1609;
		int dist = Integer.parseInt(distance);
		if(dist != 0) radius = 1609*dist +3;
		String url = "https://api.yelp.com/v3/transactions/delivery/search?term=restaurants&location="+zip+"&radius="+radius;
		//System.out.println(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		//httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		httpget.setHeader("Authorization", authorization);
		CloseableHttpResponse response = httpclient.execute(httpget);
		String responseString = EntityUtils.toString(response.getEntity());
		httpclient.close();
		System.out.println(responseString);
		return responseString;
		}
		protected String getBusinessesDelivery(String lat, String lon, String authorization, String distance) throws Exception {
			int radius = 1609;
			int dist = Integer.parseInt(distance);
			if(dist != 0) radius = 1609*dist;
			String url = "https://api.yelp.com/v3/transactions/delivery/search?term=restaurants&latitude="+lat+"&longitude="+lon+"&radius="+radius;
			//System.out.println(url);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			//httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
			httpget.setHeader("Authorization", authorization);
			CloseableHttpResponse response = httpclient.execute(httpget);

			String responseString = EntityUtils.toString(response.getEntity());
			httpclient.close();
			//System.out.println(responseString);
			return responseString;
		}
}
