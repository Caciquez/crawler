package crawler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL; 
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Crawler  {

	private static final String AGENT_VALUE = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/601.6.17 (KHTML, like Gecko) Version/9.1.1 Safari/601.6.17";
	private Proxy proxy;


	/**
	 * Creating Proxy Authentication
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 */
	public void setProxy(String ip, int port, String username, String password) {
		this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication(username, password.toCharArray()));
			}
		};
		Authenticator.setDefault(authenticator);

	}

	/**
	 * Getting Data from a Request
	 * @param url
	 * @throws Exception
	 */
	public void getData(String url) {
		try {

			URL robotsTxT = new URL(url.trim());

			/**
			 * http://www.oracle.com/technetwork/java/javase/8u111-relnotes-3124969.html
			 * Disable authetnication for HTTPS tunneling
			 */
			System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "''");

			HttpURLConnection urlConnection;
			urlConnection = (HttpURLConnection) robotsTxT.openConnection(this.proxy);
			urlConnection.setRequestProperty("User-Agent", AGENT_VALUE);

			Scanner httpResponse = new Scanner(urlConnection.getInputStream());
			ByteArrayOutputStream response = new ByteArrayOutputStream();

			while(httpResponse.hasNextLine()) {
				response.write(httpResponse.nextLine().getBytes());
			}	

			response.close();
			httpResponse.close();	


			String robotsData = response.toString();


			if(robotsData != null) {
				WallMartScrapperJsoup scrapper = new WallMartScrapperJsoup(robotsData, url);
				scrapper.parseDataFromHTML();
			} else {
				System.out.println("Crawling dind't found any data in the HTML");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {

		try {
			System.out.println("Crawler Starting....");	

			Crawler conn = new Crawler();
			conn.setProxy("93.103.94.84", 80, "default", "default");

			List<String> urlList = new ArrayList<>();
			urlList.add("https://www.walmart.com.br/smart-tv-philco-led-full-hd-24-wi-fi-ptv24n91sa/6877003/pr");
			urlList.add("https://www.walmart.com.br/smartphone-motorola-moto-g5s-32gb-dual-chip-tela-5-2-android-7-1-4g-camera-16mp-cinza/6863767/pr");

			for(String url: urlList) {
				System.out.println(url);
				conn.getData(url.trim());						
			}

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}



}
