import java.util.*;

public class Simulator {

    private int gridSize;
    // Probability of Person creation
    private double p;

    // Rumor Bound
    private int l;

    private int currentRound;

    private final LinkedList<Person> infected;

    private final Map<Location, Person> peopleMap;

    private final HashSet<Person> potentialInfected;

    private Set<Person> changed;


    public Simulator(double p, int l, int gridSize) {
        this.gridSize = gridSize;
        this.p = p;
        this.l = l;
        this.currentRound = 0;
        this.infected = new LinkedList<>();
        this.peopleMap = new HashMap<>();
        this.potentialInfected = new HashSet<>();
        this.changed = new HashSet<>();
        this.init();
    }



    public void init() {
        this.infected.clear();
        this.peopleMap.clear();
        this.potentialInfected.clear();
        // people creation
        LinkedList<Person> people = new LinkedList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double random = Math.random();
                if (random <= this.p) {
                    Location location = new Location(i, j);
                    Person person = new Person(location, (int)(Math.random() * 4 + 1), l);
                    peopleMap.put(location, person);
                    people.add(person);
                }
            }
        }
        // choose random person to spread rumor
        Random rand = new Random();
        ArrayList<Person> personList = new ArrayList<>(peopleMap.values());
        if (personList.size() > 0) {
            Person start = personList.get(rand.nextInt(personList.size()));
            start.startSpreading(0);
            this.infected.add(start);
        }
    }

    public Map<Location, Person> getInfoMap() {
        return this.peopleMap;
    }


    public Set<Person> getChanged() {
        return this.changed;
    }

    public void makeStep() {
        this.changed.clear();
        for (Person i : infected) {
            LinkedList<Location> neighbors = i.findNeighbors();
            for (Location l : neighbors) {
                if (peopleMap.containsKey(l)) {
                    Person p = peopleMap.get(l);
                    p.incCountRumors();
                    this.potentialInfected.add(p);
                }
            }
            i.forgetRumor();
        }
        this.changed.addAll(this.infected);
        infected.clear();

        for (Person i : potentialInfected) {
            if (i.belivesRumor(this.currentRound)) {
                this.infected.add(i);
            }
        }
        this.potentialInfected.clear();
        this.changed.addAll(this.infected);
        this.currentRound++;
    }



    public void reset() {
        this.currentRound = 0;
        this.init();
    }

    public void reset(double p, int l) {
        this.l = l;
        this.p = p;
        this.currentRound = 0;
        this.init();
    }

}
