package stc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Implements a multi-threaded version of Conway's Game of Life using fixed thread pool
 */

class MultithreadedLife extends Life {
    private ExecutorService service;

    MultithreadedLife(String filepath, ExecutorService executorService) throws IOException {
        super(filepath);
        this.service = executorService;

    }

    @Override
    void step() throws InterruptedException, ExecutionException {
        ArrayList<RowCalculator> rowCalculators = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            rowCalculators.add(new RowCalculator(map, width, i));
        }

        ArrayList<Future<Row>> futures = (ArrayList<Future<Row>>) service.invokeAll(rowCalculators);
        int[] newMap = new int[width * height];

        for (int i = 0; i < futures.size(); i++) {
            Row row = futures.get(i).get();

            for (int j = 0; j < width; j++) {
                newMap[pointToIndex(width, j, row.rowIndex)] = row.row[j];
            }
        }

        map = newMap;
    }

    class RowCalculator implements Callable<Row> {
        private int[] map;
        private int width;
        private int rowIndex;

        RowCalculator(int[] map, int width, int rowIndex) {
            this.map = map;
            this.width = width;
            this.rowIndex = rowIndex;
        }

        @Override
        public Row call() throws Exception {
            int[] row = new int[width];

            for (int i = 0; i < width; i++) {
                int count = aliveNeighboursCount(getNeighbours(map, width, i, rowIndex));
                row[i] = newState(map[pointToIndex(width, i, rowIndex)], count);
            }

            return new Row(rowIndex, row);
        }
    }

    class Row {
        int rowIndex;
        int[] row;

        public Row(int rowIndex, int[] row) {
            this.rowIndex = rowIndex;
            this.row = row;
        }
    }
}