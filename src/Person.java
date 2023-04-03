import java.awt.*;
import java.util.LinkedList;

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

//    public Person(int x, int y, int doubtLevel) {
//        this.location = new Location(x, y);
//        this.doubtLevel = doubtLevel;
//    }

//    public Person(int x, int y, int doubtLevel, boolean isSpreading) {
//        this(x, y, doubtLevel);
//        this.isSpreading = isSpreading;
//    }

    public Person(Location l, int doubtLevel, int rumorBound) {
        this.location = l;
        this.doubtLevel = doubtLevel;
        this.l = rumorBound;
    }

    public void startSpreading(int roundNumber) {
        isSpreading = true;
        this.timeStamp = roundNumber;
    }

    public void forgetRumor() {
        isSpreading = false;
    }

    // person hears a rumor
    private void hearRumor(int roundNumber) {
        if (timeStamp == -1 || roundNumber - timeStamp >= l) {
            this.isSpreading = true;
            this.timeStamp = roundNumber;
        }
    }

    public void incCountRumors() {
        this.countRumors++;
    }

    public boolean belivesRumor(int roundNumber) {
        int currentDoubtLevel = this.doubtLevel;
        if (this.countRumors > 1 && currentDoubtLevel > 1) {
            currentDoubtLevel--;
        }
        this.countRumors = 0;

        switch (currentDoubtLevel) {
            case 1 -> {
                hearRumor(roundNumber);
                return this.isSpreading;
            }
            case 2 -> {
                double random = Math.random();
                if (random > (2.0 / 3.0)) {
                    hearRumor(roundNumber);
                }
                return this.isSpreading;
            }
            case 3 -> {
                double random = Math.random();
                if (random > (1.0 / 3.0)) {
                    hearRumor(roundNumber);
                }
                return this.isSpreading;
            }
            default -> {
                return this.isSpreading; // should be false
            }
        }
    }

    public Color getColor() {
        if (this.isSpreading) {
            return Color.RED;
        }
        return switch(this.doubtLevel) {
            case 1 -> Color.green;
            case 2 -> Color.YELLOW;
            case 3 -> Color.ORANGE;
            default -> Color.BLACK;
        };
    }

    public Location getLocation() {
        return location;
    }

    public LinkedList<Location> findNeighbors() {
        int x = this.location.getX();
        int y = this.location.getY();

        LinkedList<Location> neighbors = new LinkedList<>();

        neighbors.add(new Location((x - 1) % 100, (y - 1) % 100));
        neighbors.add(new Location((x) % 100, (y - 1) % 100));
        neighbors.add(new Location((x + 1) % 100, (y - 1) % 100));
        neighbors.add(new Location((x - 1) % 100, (y) % 100));
        neighbors.add(new Location((x + 1) % 100, (y) % 100));
        neighbors.add(new Location((x - 1) % 100, (y + 1) % 100));
        neighbors.add(new Location((x) % 100, (y + 1) % 100));
        neighbors.add(new Location((x + 1) % 100, (y + 1) % 100));

        return neighbors;

//        if (x > 0 && y > 0 && x < 100 && y < 100) {
//            // ...
//        }
//        else if (x == 0 && y > 0 && y < 100) {
//            // ...
//        }
//        else if (x == 100 && y > 0 && y < 100) {
//
//        }
//        else if (y == 0 && x > 0 && x < 100) {
//
//        }
//        else if (y == 100 && x > 0 && x < 100) {
//
//        }
    }


}


