package j2eeapplication;

import java.util.ArrayList;
import java.util.List;







import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class getYelpToken {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

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
		System.out.println(authorization);
		
		url = "https://api.yelp.com/v3/transactions/delivery/search?latitude=43.083319599999996&longitude=-89.3724769";//&access_token="+access_token;
		httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		//httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		httpget.setHeader("Authorization", authorization);
		System.out.println(httpget.getHeaders("Authorization"));
		for(Header i: httpget.getAllHeaders()) {
			System.out.println(i.getName());
		}

		response = httpclient.execute(httpget);

		System.out.println(EntityUtils.toString(response.getEntity()));
		System.out.println(httpget.getURI());

		httpclient.close();

	}

}
