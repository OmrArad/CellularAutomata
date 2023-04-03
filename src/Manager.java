import java.util.*;

public class Manager {
    private static final int gridSize = 100;
    // Probability of Person creation
    private final double p;

    // Rumor Bound
    private final int l;

    private final LinkedList<Person> infected;

    private final Map<Location, Person> peopleMap;

    private final HashSet<Person> potentialInfected;

    private final GUI gui = new GUI();


    public Manager(double p, int l) {
        this.p = p;
        this.l = l;
        this.infected = new LinkedList<>();
        this.peopleMap = new HashMap<>();
        this.potentialInfected = new HashSet<>();
    }

    public void Run() {
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
        this.gui.initData(people);
        HashSet<Person> changed = new HashSet<>();
        // choose random person to spread rumor
        Random rand = new Random();
        ArrayList<Person> personList = new ArrayList<>(peopleMap.values());
        Person start = personList.get(rand.nextInt(personList.size()));
        start.startSpreading(0);
        this.infected.add(start);
        changed.add(start);
        this.gui.update(changed);
        changed.clear();

        boolean stop = false;
        int rounds = 0;
        while(!stop) {
            this.gui.play();
            // display
            // update csv

            for (Person i : infected) {
//                Location location = i.getLocation();
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
            changed.addAll(this.infected);
            infected.clear();

            for (Person i : potentialInfected) {
                if (i.belivesRumor(rounds)) {
                    this.infected.add(i);
                }
            }

            this.potentialInfected.clear();
            changed.addAll(this.infected);
            this.gui.update(changed);
            changed.clear();
            rounds++;
            stop = rounds > 100;
            this.pause(1);
        }

        // export csv

    }

    private static void pause(double seconds)
    {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {}
    }


}
