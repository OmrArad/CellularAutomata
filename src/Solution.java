import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Solution class.
 */
public abstract class Solution {
    protected static final int DOUBT_LEVELS = 4;

    protected final HashMap<Location, Person> m = new HashMap<>();
    protected Person first;
    protected double s1;
    protected double s2;
    protected double s3;
    protected double s4;
    protected int l;
    protected double p;
    protected int gridSize;

    /**
     * Constructor.
     * @param gridSize size of grid.
     * @param l rumour lifespan.
     */
    public Solution(int gridSize, int l) {
        this(gridSize, 1.0, l, 0, 0, 0, 0);
    }

    /**
     * Constructor.
     * @param gridSize size of grid.
     * @param p probability.
     * @param l rumour lifespan.
     * @param s1 s1 distribution.
     * @param s2 s2 distribution.
     * @param s3 s3 distribution.
     * @param s4 s4 distribution.
     */
    public Solution(int gridSize, double p, int l, double s1, double s2, double s3, double s4) {
        this.gridSize = gridSize;
        this.p = p;
        this.l = l;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.setCells();
    }

    /**
     * return the data map.
     * @return a <Location, Person> map.
     */
    public HashMap<Location, Person> getMap() {
        return this.m;
    }

    /**
     * get the first person to start from.
     * @return Person
     */
    public Person getFirst() {
        return this.first;
    }

    /**
     * get distribution of s1
     * @return double [0, 1]
     */
    public double getS1() {
        return this.s1;
    }

    /**
     * get distribution of s2
     * @return double [0, 1]
     */
    public double getS2() {
        return this.s2;
    }

    /**
     * get distribution of s3
     * @return double [0, 1]
     */
    public double getS3() {
        return this.s3;
    }

    /**
     * get distribution of s4
     * @return double [0, 1]
     */
    public double getS4() {
        return this.s4;
    }

    /**
     * return the rumour lifespan value.
     * @return int.
     */
    public int getL() {
        return this.l;
    }

    /**
     * return the value of p (probability)
     * @return
     */
    public double getP() {
        return this.p;
    }

    /**
     * Pick a random person from the map to be first.
     */
    protected void setFirstRandomly() {
        Random rand = new Random();
        ArrayList<Person> personList = new ArrayList<>(this.m.values());
        if (personList.size() > 0) {
            this.first = personList.get(rand.nextInt(personList.size()));
        }
    }

    /**
     * Resets the solution cells.
     */
    public void reset() {
        this.m.clear();
        this.first = null;

        this.setCells();
    }

    /**
     * Sets the initial solution cells and distribution.
     */
    protected abstract void setCells();
}

