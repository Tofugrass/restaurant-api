

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		if(request.getParameter("location")==null && request.getParameter("zip")==null) {String redirect = request.getRequestURL().toString();
		//we check to make sure the user is tunneled correctly
		redirect = redirect.substring(0, redirect.lastIndexOf("/")+1);
		redirect+="index.jsp";
		response.sendRedirect(redirect);}
		//we make sure the redirect works properly on local host or heroku
		else {
			if((request.getParameter("location").equals("null") && request.getParameter("zip").equals(""))) {String redirect = request.getRequestURL().toString();
			redirect = redirect.substring(0, redirect.lastIndexOf("/")+1);
			redirect+="index.jsp";
			response.sendRedirect(redirect);
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
		String term = request.getParameter("term");
		if((location.equals("null") && zip.equals("null"))) {String redirect = request.getRequestURL().toString();
		redirect = redirect.substring(0, redirect.lastIndexOf("/")+1);
		redirect+="index.jsp";
		response.sendRedirect(redirect);
		}
		else {
			// Set response content type
			response.setContentType("text/html");
			try {
				 HttpSession session = request.getSession();
				String auth = getToken();
				String result;
				if(term==null ) {
					//if the user doesnt specify a term, we default it to restaurants
					term = "restaurant";
				}
				if(location.equals("null") ) {
					if(!request.getParameter("delivery").equals("Delivery")) {
						result =  getBusinesses(zip, auth, distance, term);
					}
					else {
						result =  getBusinessesDelivery(zip, auth, distance, term);
					}
					session.setAttribute("zip", zip);
				}
				else {
					if(!request.getParameter("delivery").equals("Delivery")) {
						result =  getBusinesses(location.substring(0, location.indexOf(",")), location.substring(location.indexOf(",")+1), auth, distance, term);
					}else {
						result = getBusinessesDelivery(location.substring(0, location.indexOf(",")), location.substring(location.indexOf(",")+1), auth, distance, term);
					}
					 session.setAttribute("location", location);
				}
				//we get our search result from the api and immediately parse it into jsonobject
				JSONObject jsonobject = (JSONObject) new JSONParser()
				.parse(result);

				if(jsonobject==null) {
					String redirect = request.getRequestURL().toString();
					redirect = redirect.substring(0, redirect.lastIndexOf("/")+1);
					redirect+="index.jsp";
					response.sendRedirect(redirect);
					
				}
				JSONArray businesses = (JSONArray) jsonobject.get("businesses");
				
				 session.setAttribute("businesses", businesses);
				 session.setAttribute("auth", auth);
				//for the rest of the session, the bussinesses array will be an attribute. 
				 
				request.getRequestDispatcher("/bracket.jsp").forward(request, response);
				//we move the user forward
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @return the token type and token string 
	 * @throws Exception
	 */
	protected String getToken() throws Exception{
		String client_id = "iJUPQDxk-6lz7gz5CpfMCQ";
		String client_secret = "b1vKukbfvjb9hWeQXdai1sOqRH9Xqof2KEg2cjTwQ445tb2cLAAPPrvgqipF1iH5";
		String url ="https://api.yelp.com/oauth2/token";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
//we make a post request to the api auth server for our token
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("client_id", client_id));
		urlParameters.add(new BasicNameValuePair("client_secret", client_secret));
		httppost.setEntity(new UrlEncodedFormEntity(urlParameters));
//we add the client id and secret asa encoded entities. 
		CloseableHttpResponse response = httpclient.execute(httppost);
		String responseString = EntityUtils.toString(response.getEntity());

		httpclient.close();
		response.close();

		JSONParser parser = new JSONParser();
		JSONObject tokenBearer =  (JSONObject) parser.parse(responseString);
//we get the token type and token and place it into a string
		String access_token = (String) tokenBearer.get("access_token");
		String token_type = (String) tokenBearer.get("token_type");
		//Long expires_in = (Long) tokenBearer.get("expires_in");

		String authorization= token_type+" "+access_token;
		//we return the string
		return authorization; 
	}
/**
 * 
 * @param lat the users latitude
 * @param lon the users longitude
 * @param authorization the token
 * @param distance the users search radius
 * @param term the users search term
 * @return the json string of search results
 * @throws Exception
 */
	protected String getBusinesses(String lat, String lon, String authorization, String distance, String term) throws Exception {
		int radius = 1609;
		//we initialize the search radius to one mile
		int dist = Integer.parseInt(distance);
		if(dist != 0) radius = 1609*dist;
		//if distance isnt zero, we change search radius to whatever the users specifies
		String url = "https://api.yelp.com/v3/businesses/search?term="+term+"&latitude="+lat+"&longitude="+lon+"&radius="+radius;
		//System.out.println(url);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		//httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		httpget.setHeader("Authorization", authorization);
		CloseableHttpResponse response = httpclient.execute(httpget);
//this is the json string we return
		String responseString = EntityUtils.toString(response.getEntity());
		httpclient.close();
		//System.out.println(responseString);
		//this we use for debugging
		return responseString;
	}
	/**
	 * 
	 * @param zip the users zip code
	 * @param authorization the token
	 * @param distance the users search radius
	 * @param term the users search term
	 * @return the json string of search results
	 * @throws Exception
	 */
	protected String getBusinesses(String zip, String authorization, String distance, String term) throws Exception {
		int radius = 1609;
		int dist = Integer.parseInt(distance);
		if(dist != 0) radius = 1609*dist +3;
		String url = "https://api.yelp.com/v3/businesses/search?term="+term+"restaurants&location="+zip+"&radius="+radius;
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
	/**
	 * 
	 * @param zip the users zip code
	 * @param authorization the token
	 * @param distance the users search radius
	 * @param term the users search term
	 * @return the json string of search results
	 * @throws Exception
	 */
		protected String getBusinessesDelivery(String zip, String authorization, String distance, String term) throws Exception {
		int radius = 1609;
		int dist = Integer.parseInt(distance);
		if(dist != 0) radius = 1609*dist +3;
		String url = "https://api.yelp.com/v3/transactions/delivery/search?term="+term+"&location="+zip+"&radius="+radius;
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
		/**
		 * 
		 * @param lat the users latitude
		 * @param lon the users longitude
		 * @param authorization the token
		 * @param distance the users search radius
		 * @param term the users search term
		 * @return the json string of search results
		 * @throws Exception
		 */
		protected String getBusinessesDelivery(String lat, String lon, String authorization, String distance, String term) throws Exception {
			int radius = 1609;
			int dist = Integer.parseInt(distance);
			if(dist != 0) radius = 1609*dist;
			String url = "https://api.yelp.com/v3/transactions/delivery/search?term="+term+"&latitude="+lat+"&longitude="+lon+"&radius="+radius;
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
