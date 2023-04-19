import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Simulator class.
 */
public class Simulator {

    private int gridSize;
    // Probability of Person creation
    private double p;

    // Rumor Bound
    private int l;

    private int currentRound = 0;

    private final LinkedList<Person> infected = new LinkedList<>();

    private Map<Location, Person> peopleMap;

    private final HashSet<Person> potentialInfected = new HashSet<>();

    private final Set<Person> changed = new HashSet<>();

    private final Set<Person> allTimeInfected = new HashSet<>();

    /**
     * Constructor.
     * @param p the probability value.
     * @param l the l value (rumour lifespan)
     * @param gridSize the size of the simulation grid.
     */
    public Simulator(double p, int l, int gridSize) {
        // set the distribution to default (1/4 each)
        this(p, l, gridSize, 0.25, 0.25, 0.25, 0.25);
    }

    /**
     * Constructor.
     * @param p the probability value.
     * @param l the l value (rumour lifespan)
     * @param gridSize the size of the simulation grid.
     * @param s1 the s1 ratio.
     * @param s2 the s2 ratio.
     * @param s3 the s3 ratio.
     * @param s4 the s4 ratio.
     */
    public Simulator(double p, int l, int gridSize, double s1, double s2, double s3, double s4) {
        this.gridSize = gridSize;
        this.p = p;
        this.l = l;
        this.currentRound = 0;
        // make needed data structures
        this.peopleMap = new HashMap<>();
        // initialise the simulation.
        this.init(s1, s2, s3, s4);
    }

    public Simulator(Solution s) {
        this.l = s.getL();
        this.init(s.getMap(), s.getFirst());
    }

    /**
     * get the current round number.
     * @return int.
     */
    public int getCurrentRound() {
        return this.currentRound;
    }

    /**
     * Generate people - simulation cells.
     * @param s1 ratio of s1.
     * @param s2 ratio of s2.
     * @param s3 ratio of s3.
     * @param s4 ratio of s4.
     */
    private void createPeople(double s1, double s2, double s3, double s4) {
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
            Person person = new Person(location, doubt, l, this.gridSize);
            // save in map.
            peopleMap.put(location, person);
            count++;
        }
    }

    /**
     * Initialize the simulation, using default distribution (1/4 each).
     */
    private void init() {
        this.init(0.25, 0.25, 0.25, 0.25);
    }

    /**
     * Initialize the simulation.
     * @param s1 ratio of s1
     * @param s2 ratio of s2
     * @param s3 ratio if s3
     * @param s4 ratio of s4
     */
    private void init(double s1, double s2, double s3, double s4) {
        // clear data
        this.infected.clear();
        this.peopleMap.clear();
        this.potentialInfected.clear();
        this.allTimeInfected.clear();
        // people creation
        this.createPeople(s1, s2, s3, s4);
        // choose random person to spread rumor
        Random rand = new Random();
        ArrayList<Person> personList = new ArrayList<>(peopleMap.values());
        if (personList.size() > 0) {
            Person start = personList.get(rand.nextInt(personList.size()));
            start.startSpreading(0);
            this.infected.add(start);
            this.allTimeInfected.add(start);
        }
    }

    private void init(HashMap<Location, Person> m, Person i) {
        this.infected.clear();
        this.potentialInfected.clear();
        this.allTimeInfected.clear();
        this.peopleMap = m;
        i.startSpreading(0);
        this.infected.add(i);
        this.allTimeInfected.add(i);
    }

    /**
     * get the data map.
     * @return Map of Location -> Person.
     */
    public Map<Location, Person> getInfoMap() {
        return this.peopleMap;
    }

    /**
     * get a set of the cells that changed their status during the last round.
     * @return a Set of Person
     */
    public Set<Person> getChanged() {
        return this.changed;
    }

    /**
     * make a single simulation step.
     */
    public void makeStep(byte ofType) {
        // clear the changed set.
        this.changed.clear();
        // for every infected Person, tell its neighbors the rumour.
        for (Person i : infected) {
            LinkedList<Location> neighbors = i.findNeighbors(ofType);
            for (Location l : neighbors) {
                if (peopleMap.containsKey(l)) {
                    Person p = peopleMap.get(l);
                    p.incCountRumors();
                    // add person to potentially infected list
                    this.potentialInfected.add(p);
                }
            }
            // forget the rumour
            i.forgetRumor();
        }
        // update changed and infected.
        this.changed.addAll(this.infected);
        infected.clear();

        // check in the potentially infected list who accepted the rumor.
        for (Person i : potentialInfected) {
            if (i.believesRumor(this.currentRound)) {
                // add to infected and all time infected.
                this.infected.add(i);
                this.allTimeInfected.add(i);
            }
        }
        // update fields.
        this.potentialInfected.clear();
        this.changed.addAll(this.infected);
        this.currentRound++;
    }

    /**
     * reset the simulation.
     * @param p probability
     * @param l rumour lifespan
     * @param s1 s1 ratio
     * @param s2 s2 ratio
     * @param s3 s3 ratio
     * @param s4 s4 ratio
     */
    public void reset(double p, int l, double s1, double s2, double s3, double s4) {
        this.l = l;
        this.p = p;
        this.currentRound = 0;
        this.init(s1, s2, s3, s4);
    }

    public void reset(int l, HashMap<Location, Person> m, Person start) {
        this.l = l;
        this.currentRound = 0;
        this.init(m, start);
    }

    /**
     * get the percentage of infected people (at any time)
     * @return double [0,1]
     */
    public double getInfectionRate() {
        if (this.peopleMap.size() == 0) {
            return 0.0;
        }
        return ((double) this.allTimeInfected.size()) / ((double)this.peopleMap.size());
    }

}
