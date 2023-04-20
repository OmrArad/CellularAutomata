/**
 * God Save the Class!
 */
public class ScottishSolution extends Solution{
    // X width
    private static final int WIDTH = 5;
    /**
     * Constructor.
     * @param gridSize the size of the grid.
     * @param l rumour lifespan
     */
    public ScottishSolution(int gridSize, int l) {
        this.l = l;

        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                if (Math.abs(i - j) >= WIDTH && (i + j + 1 >= WIDTH + gridSize || i + j + 1 <= gridSize - WIDTH)) {
                    Location loc = new Location(i, j);
                    Person p = new Person(loc, 2, this.l, gridSize);
                    this.m.put(loc,p);
                }
            }
        }
        // pick first randomly.
        this.setFirstRandomly();
        this.s2 = 1.0;
    }
}
