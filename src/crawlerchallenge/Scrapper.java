package crawlerchallenge;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {
  private static final String SKU_PAGE_CLASS = "a.product-brand[href]";
  private static final String SKU_HREF_CLASS = "a[href]";
  private static final int MAX_LIST_SIZE = 1000;
  private Document html;

  private List<String> skuURL = new ArrayList<>();
  private Map<String,Boolean> crawlingURLS = new HashMap<>();


  public void startCrawler() {
    crawlingURLS.put("https://www.walmart.com.br/", false);

    CrawlerAcess access = new CrawlerAcess();

    while(skuURL.size() <=  MAX_LIST_SIZE) {
      String url = randomHash(crawlingURLS);
      this.html = url != null ? access.fetchDocument(url) : new Document("");

      crawlingURLS.put(url,true);
      getAllPageURLS();

      if(isProductPage()) {
        skuURL.add(url);
        System.out.println("Number of product on thread: " + skuURL.size());
      }
    }

    System.out.println("ITS OVER MATE!");
    writeTXT();
  }

  public boolean isProductPage() {
    return !this.html.select(SKU_PAGE_CLASS).isEmpty();
  }

  /**
   * 
   */
  private void getAllPageURLS() {
    Elements link = this.html.select(SKU_HREF_CLASS);

    for(Element el: link) {
      String href = el.attr("href").trim();
      if(href.contains("www.walmart.com.br")) {

        if(!href.startsWith("http")) {
          href = "https:" + href;
        }

        crawlingURLS.put(href, false);
      }
    }

  }

  /**
   * RandomHash - get a random URL from the hashMap to Scrap
   * @param crawlingURLS2
   * @return randomURL to acess
   */
  public String randomHash(Map<String, Boolean> crawlingURLS) {

    Random random = new Random();
    List<String> keys = new ArrayList<>(crawlingURLS.keySet());
    String randomKey = keys.get(random.nextInt(keys.size()));
    Boolean randomvalue = crawlingURLS.get(randomKey);

    if(!randomvalue) {         
      return randomKey;          
    }
    return null;
  }

  /**
   * Write's data into a html file.
   * @param info 
   */
  public void writeTXT() {
    PrintWriter out;
    try {
      out = new PrintWriter(new FileWriter("wallmartSKUs.txt"),true);       
      for(String item: skuURL) {
        out.println(item);
      }
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Data scrapped and Saved!");   
  }



}
