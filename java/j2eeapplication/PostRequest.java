package j2eeapplication;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PostRequest {

	public static void main(String[] args) throws Exception {

		// TODO Auto-generated method stub
		String url ="http://localhost:8080/j2eeapplication/test";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);

		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("location", "C02G8416DRJM"));
		urlParameters.add(new BasicNameValuePair("distance", "1"));
		httppost.setEntity(new UrlEncodedFormEntity(urlParameters));

		CloseableHttpResponse response = httpclient.execute(httppost);
		System.out.println(httppost.getURI());
		System.out.println(EntityUtils.toString(response.getEntity()));
		httpclient.close();
		response.close();
	}

}
