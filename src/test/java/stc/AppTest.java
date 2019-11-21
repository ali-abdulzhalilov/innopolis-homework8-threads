package stc;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class AppTest 
{
    @Test
    public void testMain() throws IOException, ExecutionException, InterruptedException {
        String fIn = "life.txt";
        int N = 100000;
        String fOut1 = "life0.txt";
        String fOut2 = "life1.txt";

        App.main(new String[]{fIn, String.valueOf(N), fOut1, fOut2});

        System.out.println(App.durationSingle + " ms");
        System.out.println(App.durationMulti + " ms");

        assertArrayEquals(Files.readAllBytes(Paths.get(fOut1)), Files.readAllBytes(Paths.get(fOut2)));
    }
}
