/**
 * OnionSolution class
 */
public class OnionSolution extends Solution{
    // how may lines is each layer
    private static final int LAYERS = 3;

    /**
     * Constructor.
     * @param gridSize the size of the grid.
     * @param l rumour lifespan
     */
    public OnionSolution(int gridSize, int l) {
        this.l = l;

        int doubt = 0;
        int rowStart = 0;
        int colStart = 0;
        int rowEnd = gridSize - 1;
        int colEnd = gridSize - 1;
        int count = 0;
        int[] s_counters = new int[DOUBT_LEVELS];

        while (rowStart <= rowEnd && colStart <= colEnd) {
            // doubt top row
            for (int c = colStart; c <= colEnd; c++) {
                Location location = new Location(rowStart, c);
                Person p = new Person(location, doubt + 1, this.l, gridSize);
                s_counters[doubt]++;
                this.m.put(location, p);
            }
            rowStart++;

            // doubt right column
            for (int r = rowStart; r <= rowEnd; r++) {
                Location location = new Location(r, colEnd);
                Person p = new Person(location, doubt + 1, this.l, gridSize);
                s_counters[doubt]++;
                this.m.put(location, p);
            }
            colEnd--;

            // doubt bottom row
            for (int c = colEnd; c >= colStart; c--) {
                Location location = new Location(rowEnd, c);
                Person p = new Person(location, doubt + 1, this.l, gridSize);
                s_counters[doubt]++;
                this.m.put(location, p);
            }
            rowEnd--;

            // doubt left column
            for (int r = rowEnd; r >= rowStart; r--) {
                Location location = new Location(r, colStart);
                Person p = new Person(location, doubt + 1, this.l, gridSize);
                s_counters[doubt]++;
                this.m.put(location, p);
            }
            colStart++;
            if (count % LAYERS == 0) {
                doubt = (doubt + 1) % DOUBT_LEVELS; // switch to the next doubt
            }
            count++;
        }
        // set first
        this.first = this.m.get(new Location(gridSize / 2, gridSize / 2));
        // calculate distribution.
        if (gridSize != 0) {
            this.s1 = Math.round(((double) s_counters[0] / gridSize / gridSize) * 100.0) / 100.0;
            this.s2 = Math.round(((double) s_counters[1] / gridSize / gridSize) * 100.0) / 100.0;
            this.s3 = Math.round(((double) s_counters[2] / gridSize / gridSize) * 100.0) / 100.0;
            this.s4 = Math.round(((double) s_counters[3] / gridSize / gridSize) * 100.0) / 100.0;
        }
    }
}
