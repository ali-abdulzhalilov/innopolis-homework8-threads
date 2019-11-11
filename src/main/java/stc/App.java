package stc;

import java.io.IOException;

public class App
{
    public static void main( String[] args ) throws IOException {
        Life life = new Life("life.txt");
        life.draw();

        int N = 10;
        for (int i = 0; i < N; i++) {
            life.step();
            life.draw();
        }

        life.saveToFile("life1.txt");
    }
}
