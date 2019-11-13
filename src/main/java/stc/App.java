package stc;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class App {
    public static void main(String[] args) throws IOException {
        String filepath = "life.txt";
        int N = 10000;

        Life simpleLife = new Life(filepath);
        MultithreadedLife multithreadedLife = new MultithreadedLife(filepath);

        Instant simpleStart = Instant.now();
        for (int i = 0; i < N; i++)
            simpleLife.step();
        Instant simpleEnd = Instant.now();

        Instant multiStart = Instant.now();
        for (int i = 0; i < N; i++) {
            multithreadedLife.step();
            //multithreadedLife.draw();
        }
        multithreadedLife.service.shutdown();
        Instant multiEnd = Instant.now();

        System.out.println(Duration.between(simpleStart, simpleEnd).toMillis()+" ms");
        System.out.println(Duration.between(multiStart, multiEnd).toMillis()+" ms");

        //life.saveToFile("life1.txt");
    }
}


