import java.util.*;

public class Simulator {

    private final int gridSize;
    // Probability of Person creation
    private double p;

    // Rumor Bound
    private int l;

    private int currentRound;

    private final LinkedList<Person> infected;

    private final Map<Location, Person> peopleMap;

    private final HashSet<Person> potentialInfected;

    private final Set<Person> changed;

    private final Set<Person> allTimeInfected;


    public Simulator(double p, int l, int gridSize) {
        this(p, l, gridSize, 0.25, 0.25, 0.25, 0.25);
    }

    public Simulator(double p, int l, int gridSize, double s1, double s2, double s3, double s4) {
        this.gridSize = gridSize;
        this.p = p;
        this.l = l;
        this.currentRound = 0;
        this.infected = new LinkedList<>();
        this.peopleMap = new HashMap<>();
        this.potentialInfected = new HashSet<>();
        this.allTimeInfected = new HashSet<>();
        this.changed = new HashSet<>();
        this.init(s1, s2, s3, s4);
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    private void createPeople(double s1, double s2, double s3, double s4) {
        LinkedList<Location> locations = new LinkedList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double random = Math.random();
                if (random <= this.p) {
                    locations.add(new Location(i, j));
                }
            }
        }
        int x1 = (int) Math.round(s1 * locations.size());
        int x2 = (int) Math.round(s2 * locations.size());
        int x3 = (int) Math.round(s3 * locations.size());
        int x4 = (int) Math.round(s4 * locations.size());

        int sum = x1 + x2 + x3 + x4;
        if (sum < locations.size()) {
            x1 += locations.size() - sum;
        }
        Collections.shuffle(locations);
        int count = 0;
        for (Location location : locations) {
            int doubt = 4;
            if (count < x1) {
                doubt = 1;
            } else if (count < x1 + x2) {
                doubt = 2;
            } else if (count < x1 + x2 + x3) {
                doubt = 3;
            }
            Person person = new Person(location, doubt, l, this.gridSize);
            peopleMap.put(location, person);
            count++;
        }
    }

    private void init() {
        this.init(0.25, 0.25, 0.25, 0.25);
    }

    private void init(double s1, double s2, double s3, double s4) {
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
            if (i.believesRumor(this.currentRound)) {
                this.infected.add(i);
                this.allTimeInfected.add(i);
            }
        }
        this.potentialInfected.clear();
        this.changed.addAll(this.infected);
        this.currentRound++;
    }

    public void reset(double p, int l, double s1, double s2, double s3, double s4) {
        this.l = l;
        this.p = p;
        this.currentRound = 0;
        this.init(s1, s2, s3, s4);
    }

    public double getInfectionRate() {
        if (this.peopleMap.size() == 0) {
            return 0.0;
        }
        return ((double) this.allTimeInfected.size()) / ((double)this.peopleMap.size());
    }

}
