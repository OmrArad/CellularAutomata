import java.util.Objects;

/**
 * Location class, defines (x,y) pairs.
 */
public class Location {
    private int x;
    private int y;

    /**
     * Constructor.
     * @param x value of x.
     * @param y value of y.
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * get the value of x.
     * @return int.
     */
    public int getX() {
        return x;
    }

    /**
     * get the value of y.
     * @return int.
     */
    public int getY() {
        return y;
    }

    // we need to override the equals method so it takes the values of x and y into account.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
