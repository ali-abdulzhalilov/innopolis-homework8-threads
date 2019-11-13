package stc;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppTest 
{
    @Test
    public void testMain() throws IOException {
        String fIn = "life.txt";
        String fOut1 = "life0.txt";
        String fOut2 = "life1.txt";
        int N = 20000;

        App.main(new String[]{fIn, String.valueOf(N), fOut1, fOut2}); // <- this is bad, but I have to test it somehow, so...

        System.out.println(App.durationSingle + " ms");
        System.out.println(App.durationMulti + " ms");

        assertArrayEquals(Files.readAllBytes(Paths.get(fOut1)), Files.readAllBytes(Paths.get(fOut2)));
    }
}
