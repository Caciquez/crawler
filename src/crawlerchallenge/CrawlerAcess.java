package crawlerchallenge;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class CrawlerAcess{ 

  private static final String USER = "default";
  private static final String PASS = "default";

  private static List<String> proxyHost = new ArrayList<>();
  private static List<String> agentValue = new ArrayList<>();

  private int timeout = 9000;
  private List<Proxy> proxies = new ArrayList<>();
  private Random rand = new Random();

  static {

    agentValue.add("Mozilla/5.0 (Macitosh; Itel Mac OS X 10_11_5 AppleWebKit/601.6.17 (KHTML like Gecko Versio/9.1.1 Safari/601.6.17");
    agentValue.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
    agentValue.add("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
    agentValue.add("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0");
    agentValue.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML like Gecko) Chrome/37.0.2062.120 Safari/537.36");
    agentValue.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML like Gecko) Chrome/36.0.1985.143 Safari/537.36");
    agentValue.add("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
    agentValue.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
    agentValue.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.11 (KHTML like Gecko) Chrome/23.0.1271.95 Safari/537.11");

    proxyHost.add("*******Insert default proxy's******");

  }
  
  public CrawlerAcess() {
    for(String host : proxyHost) {
      if(pingHost(host,80,timeout)) {
        this.proxies.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, 80)));
      }
    }
  }

  /**
   * AcessHTTP - create HTTPURLConnection and get input Stream;
   * @param url - url used for the connection
   */
  public Document fetchDocument(String url) {  
    Document doc = new Document("");
    //http://www.oracle.com/technetwork/java/javase/8u111-relnotes-3124969.html
    //Disable basic authentication for HTTPS tunneling 
    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "''");
    System.out.println("Acessing url " + url + " ...");

    try {
      URL robotsTxT = new URL(url);

      Proxy proxy = proxies.get(this.rand.nextInt(proxies.size()));
      
      Authenticator authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return (new PasswordAuthentication(USER, PASS.toCharArray()));
        }
      };
      Authenticator.setDefault(authenticator);
      
      HttpURLConnection httpConn = (HttpURLConnection) robotsTxT.openConnection(proxy);
      httpConn.setRequestProperty("User-Agent", agentValue.get(rand.nextInt(agentValue.size())));

      Scanner httpResponse = new Scanner(httpConn.getInputStream());

      ByteArrayOutputStream response = new ByteArrayOutputStream();
      while(httpResponse.hasNextLine()) {
        response.write(httpResponse.nextLine().getBytes());
      }       

      response.close();
      httpResponse.close();   

      String robotsData = response.toString();

      if(robotsData != null) {
        doc = Jsoup.parse(robotsData);
      }

    } catch (Exception ex) {
      System.err.println("ERROR: " + ex.getMessage());
    }

    return doc;
  }

  /**
   * PingHost - check if the host proxy to be used its OK!
   * @param host - ip number
   * @param port - port number 
   * @param timeout - timeout of socket connection
   * @return true - IF host is able to be used.
   */
  public static boolean pingHost(String host, int port, int timeout) {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(host, port), timeout);
      return true;
    } catch (IOException e) {
      return false; 
    }
  }

}
