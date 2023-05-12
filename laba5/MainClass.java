import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class MainClass{
    private static final Object lock = new Object();

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("result.txt"))) {
            Thread thread1 = new Thread(new WriterTask("Thread 1", 250, writer));
            Thread thread2 = new Thread(new WriterTask("Thread 2", 500, writer));
            Thread thread3 = new Thread(new WriterTask("Thread 3", 1000, writer));

            thread1.start();
            thread2.start();
            thread3.start();

            thread1.join();
            thread2.join();
            thread3.join();

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
            int counter = 0;
            try {
                while (counter < 240) {
                    LocalDateTime now = LocalDateTime.now();
                    synchronized (lock) {
                        writer.println(name + " - " + now + " - " + counter);
                    }
                    counter++;
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}