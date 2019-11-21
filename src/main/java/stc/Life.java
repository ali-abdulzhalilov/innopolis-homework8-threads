package stc;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Implements a single-threaded version of Conway's Game of Life
 */
class Life {
    int width, height;
    int[] map;

    Life(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new int[width * height];
        fillMapRandomly(0.4f);
    }

    // TODO: split the constructor to methods
    Life(String filepath) throws IOException {
        FileInputStream fileIn = new FileInputStream(filepath);

        byte[] buffer = new byte[fileIn.available()];
        fileIn.read(buffer);
        String mapAsString = new String(buffer);

        ArrayList<ArrayList<Integer>> tempMap = new ArrayList<>();

        for (String line : mapAsString.split("\n")) {
            ArrayList<Integer> row = new ArrayList<>();

            for (char ch : line.trim().toCharArray()) {
                row.add(Integer.parseInt(ch + ""));
            }

            tempMap.add(row);
        }

        fileIn.close();

        this.width = tempMap.get(0).size();
        this.height = tempMap.size();
        this.map = new int[this.width*this.height];

        for (int i = 0; i < this.height; i++) {
            ArrayList<Integer> row = tempMap.get(i);

            for (int j = 0; j < this.width; j++) {
                int number = row.get(j);
                this.map[pointToIndex(width, j, i)] = number;
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }



    /**
     * Fill map with ones and zeros randomly.
     * Ratio of ones to zeros is determined with probability p,
     * which should be in a range of [0, 1].
     */
    private void fillMapRandomly(float p) {
        float probability = (p > 1) ? 1 : (p < 0) ? 0 : p;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                map[pointToIndex(width, j, i)] = Math.random() < p ? 1 : 0;
        }
    }

    void saveToFile(String filepath) throws IOException {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                str.append(map[pointToIndex(width, j, i)]);
            }
            str.append('\n');
        }

        FileOutputStream fileOut = new FileOutputStream(filepath);
        fileOut.write(str.toString().getBytes());
        fileOut.close();
    }

    void step() throws InterruptedException, ExecutionException {
        int[] newMap = new int[width * height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int count = aliveNeighboursCount(getNeighbours(map, width, j, i));
                int index = pointToIndex(width, j, i);
                newMap[index] = newState(map[index], count);
            }
        }

        map = newMap;
    }

    void draw() {
        System.out.println("\n--------------------");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                System.out.print((map[pointToIndex(width, j, i)] == 1 ? '#' : '.')+" ");
            System.out.println();
        }
        System.out.println();
    }

    // static util functions
    static int pointToIndex(int rowWidth, int x, int y) {
        return y * rowWidth + x;
    }

    static int[] getNeighbours(int[] map, int width, int x, int y) {
        int[] neighbours = new int[8];

        int counter = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int xx = (x + i + width) % width;
                int height = map.length / width;
                int yy = (y + j + height) % height;
                neighbours[counter] = map[pointToIndex(width, xx, yy)];
                counter++;
            }
        }

        return neighbours;
    }

    static int aliveNeighboursCount(int[] neighbours) {
        int count = 0;

        for (int neighbour : neighbours)
            count += neighbour;

        return count;
    }

    static int newState(int oldState, int aliveNeighboursCount) {
        switch (aliveNeighboursCount) {
            case 2:
                return oldState;
            case 3:
                return 1;
            default:
                return 0;
        }
    }
}
