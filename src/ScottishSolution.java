import java.util.Arrays;

/**
 * God Save the Class!
 */
public class ScottishSolution extends Solution{
    // X width
    private static final int WIDTH = 4;
    /**
     * Constructor.
     * @param gridSize the size of the grid.
     * @param l rumour lifespan
     */
    public ScottishSolution(int gridSize, int l) {
        super(gridSize, l);
    }

    @Override
    protected void setCells() {
        int[] s_counters = new int[DOUBT_LEVELS];
        int doubt = 0;

        // color the grid, count all people by doubt level
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                if (Math.abs(i - j) >= WIDTH && (i + j + 1 >= WIDTH + gridSize || i + j + 1 <= gridSize - WIDTH)
                        && (i > 0 && i < gridSize - 1) && (j > 0 && j < gridSize - 1)) {
                    doubt = 2;
                    if (i > j && i + j + 1 >= WIDTH + gridSize) {
                        doubt = 3;
                    } else if (i < j && i + j + 1 <= gridSize - WIDTH) {
                        doubt = 1;
                    }
                } else {
                    doubt = 4;
                }
                s_counters[doubt - 1]++;
                Location loc = new Location(i, j);
                Person p = new Person(loc, doubt, this.l, gridSize);
                this.m.put(loc,p);
            }
        }
        // pick first randomly.
        this.setFirstRandomly();

        // set S-values
        int sum = 0;
        for (int d : s_counters) {
            sum += d;
        }
        this.s1 = (double) s_counters[0]/sum;
        this.s2 = (double) s_counters[1]/sum;
        this.s3 = (double) s_counters[2]/sum;
        this.s4 = (double) s_counters[3]/sum;
    }
}
