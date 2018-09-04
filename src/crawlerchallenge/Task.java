package crawlerchallenge;

public class Task implements Runnable {
  
  private Scrapper crawler;
  
  public Task(Scrapper crawler) {
    this.crawler = crawler;
  }
  
  @Override
  public void run() {
    this.crawler.startCrawler();
  }

}
