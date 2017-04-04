package j2eeapplication;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 <%@ page import="java.io.IOException"%>
 <%@ page import="org.json.simple.JSONArray"%>
<%@ page import="org.json.simple.JSONObject"%>
<%@ page import="org.json.simple.parser.JSONParser"%>
<%@ page import="org.apache.http.client.ClientProtocolException"%>
<%@ page import="org.apache.http.client.methods.CloseableHttpResponse"%>
<%@ page import="org.apache.http.client.methods.HttpGet"%>
<%@ page import="org.apache.http.impl.client.CloseableHttpClient"%>
<%@ page import="org.apache.http.impl.client.HttpClients"%>
<%@ page import="org.apache.http.util.EntityUtils"%>
//*/
public class GetRequest {

	public static void main(String[] args) {
		String api_key = "eHZ8ALetEIB8d67RuqoK5whEgcZENEy1KCKO1X4y";
		String tourneyString = "t9viao7h";
		//String url = "https://"+username+":"+api_key+"@api.challonge.com/v1/tournaments/"+tourneyString+".json?include_participants=1&include_matches=1";
		String url = "https://api.challonge.com/v1/tournaments/"+tourneyString+".json?api_key="+api_key+"&include_participants=1&include_matches=1";
		//String paramString = URLEncodedUtils.format(urlParameters, "UTF-8");
		//url += paramString;

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(url);

		getRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
		getRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		//	getRequest.setHeader("include_participants", "1");
		//getRequest.setHeader("include_matches", "1");

		//.setEntity(new UrlEncodedFormEntity(urlParameters));
		try {
			CloseableHttpResponse response = client.execute(getRequest);
			System.out.println(EntityUtils.toString(response.getEntity()));
			System.out.println(getRequest.getURI());
			client.close();
			response.close();
		}catch(ClientProtocolException e) {
			System.out.println("ClientProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	}

}
