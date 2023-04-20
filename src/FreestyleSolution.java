import java.util.Collections;
import java.util.LinkedList;

/**
 * FreestyleSolution class. draws cells randomly.
 */
public class FreestyleSolution extends Solution{
    private double p;
    /**
     * Constructor.
     * @param gridSize size of the grid
     * @param p probability
     * @param l rumour lifespan
     * @param s1 s1 distribution
     * @param s2 s2 distribution
     * @param s3 s3 distribution
     * @param s4 s4 distribution
     */
    public FreestyleSolution(int gridSize, double p, int l, double s1, double s2, double s3, double s4) {
        this.p = p;
        this.l = l;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        // create people
        this.createPeople(gridSize, s1, s2, s3, s4);
        // set first
        this.setFirstRandomly();
    }

    /**
     * Constructor with default distribution.
     * @param gridSize the size of the grid.
     * @param p probability.
     * @param l rumour lifespan.
     */
    public FreestyleSolution(int gridSize, double p, int l) {
        this(gridSize, p, l, 0.25, 0.25, 0.25, 0.25);
    }

    /**
     * Generate people - simulation cells.
     * @param gridSize the size of the grid.
     * @param s1 ratio of s1.
     * @param s2 ratio of s2.
     * @param s3 ratio of s3.
     * @param s4 ratio of s4.
     */
    private void createPeople(int gridSize, double s1, double s2, double s3, double s4) {
        LinkedList<Location> locations = new LinkedList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double random = Math.random();
                // check if a new person should be generated (probability based)
                if (random <= this.p) {
                    locations.add(new Location(i, j));
                }
            }
        }
        // set the amounts of each type of doubt level.
        int x1 = (int) Math.round(s1 * locations.size());
        int x2 = (int) Math.round(s2 * locations.size());
        int x3 = (int) Math.round(s3 * locations.size());
        int x4 = (int) Math.round(s4 * locations.size());

        // check if the amount is valid, fix it if necessary.
        int sum = x1 + x2 + x3 + x4;
        if (sum < locations.size()) {
            x1 += locations.size() - sum;
        }
        // shuffle the locations (in order to avoid getting predictable locations for each type)
        Collections.shuffle(locations);
        int count = 0;
        for (Location location : locations) {
            // get the right doubt level.
            int doubt = 4;
            if (count < x1) {
                doubt = 1;
            } else if (count < x1 + x2) {
                doubt = 2;
            } else if (count < x1 + x2 + x3) {
                doubt = 3;
            }
            // initialise a new person in that spot.
            Person person = new Person(location, doubt, l, gridSize);
            // save in map.
            this.m.put(location, person);
            count++;
        }
    }
}
