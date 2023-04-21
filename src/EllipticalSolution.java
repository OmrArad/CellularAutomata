/**
 * EllipticalSolution class.
 */
public class EllipticalSolution extends Solution{

    /**
     * Constructor.
     * @param gridSize the size of the grid.
     * @param l rumour lifespan.
     */
    public EllipticalSolution(int gridSize, int l) {
        super(gridSize, l);
    }

    @Override
    protected void setCells() {
        for (int i = 0; i < gridSize / 2; i++) {
            for (int j = 0; j < 4; j++) {
                int cx = gridSize / 2;
                int cy = gridSize / 2;
                int r = 2 * gridSize / 3 - i - j;

                for (int x = 0; x < gridSize; x++) {
                    for (int y = 0; y < gridSize; y++) {
                        int dx = x - cx;
                        int dy = y - cy;
                        if (dx * dx + dy * dy < r * r) {

                            Location loc = new Location(x, y);
                            Person p = new Person(loc, j + 1, l, gridSize);
                            this.m.put(loc, p);
                        }
                    }
                }
            }
        }
        // set first
        this.setFirstRandomly();
    }
}
