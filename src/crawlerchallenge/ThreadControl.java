package crawlerchallenge;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadControl {
  private static final int MAX_THREADS = 5;
  private ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
  
  public ThreadControl() {
    super();
  }
  
  public void start() {
    for (int i = 1; i <= MAX_THREADS; i++) {
      Scrapper scrapper = new Scrapper();
      Task scrapperRunner = new Task(scrapper);
      this.executor.submit(scrapperRunner);
    }
  }
  
  public void stop() {
    this.executor.shutdown();
  }
}
  