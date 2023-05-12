import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class MainClass{
    private static volatile int counter = 0;
    private static final Object lock = new Object();
    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("result.txt"))) {
            Thread thread1 = new Thread(new WriterTask("Поток 1", 250, writer));
            Thread thread2 = new Thread(new WriterTask("Поток 2", 500, writer));
            Thread thread3 = new Thread(new WriterTask("Поток 3", 1000, writer));

            thread1.start();
            thread2.start();
            thread3.start();

            while (counter < 240) {
                Thread.sleep(100);
            }

            thread1.interrupt();
            thread2.interrupt();
            thread3.interrupt();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static class WriterTask implements Runnable {

        private final String name;
        private final int interval;
        private final PrintWriter writer;

        public WriterTask(String name, int interval, PrintWriter writer) {
            this.name = name;
            this.interval = interval;
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LocalDateTime now = LocalDateTime.now();
                    synchronized (lock) {
                        writer.println(name + " - " + now + " - " + counter);
                    }
                    counter++;
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                
            }
        }
    }   
}