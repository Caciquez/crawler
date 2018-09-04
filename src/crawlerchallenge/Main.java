package crawlerchallenge;


public class Main {
  
  private static final ThreadControl threadControl = new ThreadControl();

  public static void main(String[] args) {
    threadControl.start();
  }

  private static void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Stop ThreadControl ...");
        threadControl.stop();
      }
    });
  }


}
