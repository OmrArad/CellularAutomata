import java.awt.*;
import java.util.LinkedList;

/**
 * Person class.
 */
public class Person {
    // location in grid:
    private final Location location;

    // Doubt level
    private final int doubtLevel;

    // Spreading the rumor
    private boolean isSpreading = false;

    // Number of Rumors Heard
    private int countRumors = 0;

    // Rumor time stamp
    private int timeStamp = -1;

    // Rumor Bound
    private final int l;

    // the person's neighbors
    private final LinkedList<Location> neighbors;

    /**
     * Constructor.
     * @param l the person's location.
     * @param doubtLevel the person's doubt level.
     * @param rumorBound the rumor's lifespan.
     * @param gridSize the size of the grid.
     */
    public Person(Location l, int doubtLevel, int rumorBound, int gridSize) {
        this.location = l;
        this.doubtLevel = doubtLevel;
        this.l = rumorBound;

        int x = this.location.getX();
        int y = this.location.getY();

        LinkedList<Location> neighbors = new LinkedList<>();
        // generate the neighbors, using pacman like boundaries.
        if (gridSize > 1) {
            neighbors.add(new Location((x - 1) % gridSize, (y - 1) % gridSize));
            neighbors.add(new Location(x, (y - 1) % gridSize));
            neighbors.add(new Location((x + 1) % gridSize, (y - 1) % gridSize));
            neighbors.add(new Location((x - 1) % gridSize, y));
            neighbors.add(new Location((x + 1) % gridSize, y));
            neighbors.add(new Location((x - 1) % gridSize, (y + 1) % gridSize));
            neighbors.add(new Location(x, (y + 1) % gridSize));
            neighbors.add(new Location((x + 1) % gridSize, (y + 1) % gridSize));
        }

        this.neighbors = neighbors;

    }

    /**
     * set the person as rumor spreader.
     * @param roundNumber the current round.
     */
    public void startSpreading(int roundNumber) {
        isSpreading = true;
        // save when the person accepted the rumor.
        this.timeStamp = roundNumber;
    }

    /**
     * Set the isSpreading field to false, which means that the person does not hold the rumor anymore.
     */
    public void forgetRumor() {
        isSpreading = false;
    }

    /**
     * handle the case which a person hears a rumor
     * @param roundNumber the round in which the rumour was accepted.
     */
    private void hearRumor(int roundNumber) {
        // check if the rumour can be accepted.
        if (timeStamp == -1 || roundNumber - timeStamp >= l) {
            this.isSpreading = true;
            this.timeStamp = roundNumber;
        }
    }

    /**
     * Increment the number of neighbors of gave this person the rumour.
     */
    public void incCountRumors() {
        this.countRumors++;
    }

    /**
     * check if the person accepts the rumour that was received.
     * @param roundNumber the round in which it was received
     * @return true if it is accepted, false otherwise.
     */
    public boolean believesRumor(int roundNumber) {
        // change the current doubt level if needed (2 or move people sent it)
        int currentDoubtLevel = this.doubtLevel;
        if (this.countRumors > 1 && currentDoubtLevel > 1) {
            currentDoubtLevel--;
        }
        // reset the field.
        this.countRumors = 0;

        // run according to the doubt level.
        switch (currentDoubtLevel) {
            case 1 -> {
                // automatically accepts the rumour
                hearRumor(roundNumber);
                return this.isSpreading;
            }
            case 2 -> {
                // accepts in 2/3 chance
                double random = Math.random();
                if (random > (1.0 / 3.0)) {
                    hearRumor(roundNumber);
                }
                return this.isSpreading;
            }
            case 3 -> {
                // accepts in 1/3 chance
                double random = Math.random();
                if (random > (2.0 / 3.0)) {
                    hearRumor(roundNumber);
                }
                return this.isSpreading;
            }
            default -> {
                // automatically rejects
                return this.isSpreading; // should be false
            }
        }
    }

    /**
     * get the colour that represents this person.
     * @param roundNumber the current round number.
     * @return Color object.
     */
    public Color getColor(int roundNumber) {
        // check if the person is infected.
        if (this.isSpreading) {
            return new Color(222, 57, 5);
        }
        // check if the person still has not "recovered" (if l rounds passed since the rumour was accepted)
        if (roundNumber != 0 && timeStamp >= 0 && roundNumber - timeStamp < l) {
            return new Color(246, 136, 106);
        }
        // return colour according to the doubt level.
        return switch(this.doubtLevel) {
            case 1 -> new Color(170, 230, 250);
            case 2 -> new Color(100, 200, 250);
            case 3 -> new Color(30, 120, 250);
            default -> new Color(0, 50, 250);
        };
    }

    /**
     * Get the person's location.
     * @return Location object.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * get the neighbors locations.
     * @return a list of type Location.
     */
    public LinkedList<Location> findNeighbors() {
        return this.neighbors;
    }


}


