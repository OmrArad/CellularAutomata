import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Simulator class.
 */
public class Simulator {

    private int currentRound = 0;

    private final LinkedList<Person> infected = new LinkedList<>();

    private Map<Location, Person> peopleMap;

    private final HashSet<Person> potentialInfected = new HashSet<>();

    private final Set<Person> changed = new HashSet<>();

    private final Set<Person> allTimeInfected = new HashSet<>();


    /**
     * Constructor
     * @param s Solution.
     */
    public Simulator(Solution s) {
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
     * initialise the needed data for the simulator.
     * @param m a <Location, Person> map.
     * @param i a Person to begin from.
     */
    private void init(HashMap<Location, Person> m, Person i) {
        this.peopleMap = m;
        if (i != null) {
            i.startSpreading(0);
            this.infected.add(i);
            this.allTimeInfected.add(i);
        }
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
     * Reset the simulator's values.
     * @param m the new map.
     * @param start the first person.
     */
    public void reset(HashMap<Location, Person> m, Person start) {
        // reset values
        this.currentRound = 0;
        this.infected.clear();
        this.potentialInfected.clear();
        this.allTimeInfected.clear();
        // set new values
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
