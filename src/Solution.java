import java.util.HashMap;
import java.util.List;

public abstract class Solution {

    protected final HashMap<Location, Person> m = new HashMap<>();
    protected Person first;
    protected double s1 = 0;
    protected double s2 = 0;
    protected double s3 = 0;
    protected double s4 = 0;
    protected int l;






//        int size = 100;


//        for (int i = 0; i < size / 2; i++) {
//            int x1 = i;
//            int y1 = i;
//            int x2 = size - i - 1;
//            int y2 = size - i - 1;
//
//            for (int j = 0; j < 4; j++) {
//                int cx = size / 2;
//                int cy = size / 2;
//                int r = 2 * size / 3 - i - j;
//
//                for (int x = 0; x < size; x++) {
//                    for (int y = 0; y < size; y++) {
//                        int dx = x - cx;
//                        int dy = y - cy;
//                        if (dx * dx + dy * dy < r * r) {
//
//                            Location l = new Location(x, y);
//                            Person p = new Person(l, j + 1, 30, 100);
//                            this.m.put(l, p);
//                            this.first = p;
//                        }
//                    }
//                }
//            }
//        }



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
}

