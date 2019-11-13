package stc;

import sun.reflect.misc.FieldUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

class MultithreadedLife extends Life {
    ExecutorService service;

    MultithreadedLife(String filepath) throws IOException {
        super(filepath);
        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-2);
    }

    @Override
    void step() {
        ArrayList<RowCalculator> rowCalculators = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            rowCalculators.add(new RowCalculator(map, width, height, i));
        }

        ArrayList<Future<int[]>> futures = new ArrayList<>();
        
        for (RowCalculator rowCalc : rowCalculators)
            futures.add(service.submit(rowCalc));

        int[] newMap = new int[width*height];

        for (int i = 0; i < futures.size(); i++) {
            int[] row = null;
            try {
                row = futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < width; j++) {
                newMap[pointToIndex(j, i)] = row[j];
            }
        }

        map = newMap;
    }
}

class RowCalculator implements Callable<int[]> {
    
    private int[] map;
    private int width;
    private int height;
    private int rowIndex;
    
    RowCalculator(int[] map, int width, int height, int rowIndex) {
        this.map = map;
        this.width = width;
        this.height = height;
        this.rowIndex = rowIndex;
    }

    @Override
    public int[] call() throws Exception {
        int[] row = new int[width];
        
        for (int i = 0; i < width; i++) {
            int count = aliveNeighboursCount(getNeighbours(i, rowIndex));
            row[i] = newState(map[pointToIndex(i, rowIndex)], count);
        }

        return row;
    }

    private int[] getNeighbours(int x, int y) {
        int[] neighbours = new int[8];

        int counter = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int xx = (x + i + width) % width;
                int yy = (y + j + height) % height;
                neighbours[counter] = map[pointToIndex(xx, yy)];
                counter++;
            }
        }

        return neighbours;
    }

    private int aliveNeighboursCount(int[] neighbours) {
        int count = 0;

        for (int neighbour : neighbours)
            count += neighbour;

        return count;
    }

    private int newState(int oldState, int aliveNeighboursCount) {
        switch (aliveNeighboursCount) {
            case 2:
                return oldState;
            case 3:
                return 1;
            default:
                return 0;
        }
    }

    private int pointToIndex(int x, int y) {
        return y * width + x;
    }
}