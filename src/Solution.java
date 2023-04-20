import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Solution {
    protected static final int DOUBT_LEVELS = 4;
    protected final HashMap<Location, Person> m = new HashMap<>();
    protected Person first;
    protected double s1 = 0;
    protected double s2 = 0;
    protected double s3 = 0;
    protected double s4 = 0;
    protected double p = 1;
    protected int l;

    public HashMap<Location, Person> getMap() {
        return this.m;
    }

    public Person getFirst() {
        return this.first;
    }

    public double getS1() {
        return this.s1;
    }

    public double getS2() {
        return this.s2;
    }

    public double getS3() {
        return this.s3;
    }

    public double getS4() {
        return this.s4;
    }

    public int getL() {
        return this.l;
    }

    public double getP() {
        return this.p;
    }

    protected void setFirstRandomly() {
        Random rand = new Random();
        ArrayList<Person> personList = new ArrayList<>(this.m.values());
        if (personList.size() > 0) {
            this.first = personList.get(rand.nextInt(personList.size()));
        }
    }
}

