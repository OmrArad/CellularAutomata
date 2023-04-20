/**
 * CheckerboardSolution class.
 */
public class CheckerboardSolution extends Solution {
    /**
     * Constructor.
     * @param gridSize the size of the grid.
     * @param l rumour lifespan
     */
    public CheckerboardSolution(int gridSize, int l) {
        this.l = l;
        int half = DOUBT_LEVELS / 2;
        int[] s_counters = new int[DOUBT_LEVELS];
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                Location loc = new Location(i, j);
                int doubt = 1 + (j % half);
                if(i % half != 0) {
                    doubt += half;
                }
                Person p = new Person(loc, doubt, this.l, gridSize);
                s_counters[doubt - 1]++;
                this.m.put(loc, p);
            }
        }
        // set first
        this.first = this.m.get(new Location(gridSize/2, gridSize/2));
        // calculate distribution.
        if (gridSize != 0) {
            this.s1 = (double) s_counters[0] / gridSize / gridSize;
            this.s2 = (double) s_counters[1] / gridSize / gridSize;
            this.s3 = (double) s_counters[2] / gridSize / gridSize;
            this.s4 = (double) s_counters[3] / gridSize / gridSize;
        }
    }
}
