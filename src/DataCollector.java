import java.io.FileWriter;
import java.io.IOException;

/**
 * DataCollector class, used for generating data.
 */
public class DataCollector {
    // constant values
    private static final int ROUNDS = 120;
    private static final int ITERATIONS = 10;
    private static final int GRID_SIZE = 100;

    private final byte nType;
    private double p;
    private int l;
    private double s1;
    private double s2;
    private double s3;
    private double s4;
    private double[] ratesSum = new double[ROUNDS + 1];
    private final Simulator simulator;

    /**
     * Constructor.
     * @param p probability.
     * @param l rumor lifespan.
     * @param s1 distribution of s1.
     * @param s2 distribution of s2.
     * @param s3 distribution of s3.
     * @param s4 distribution of s4.
     */
    public DataCollector(double p, int l, double s1, double s2, double s3, double s4) {
        this(p, l, s1, s2, s3, s4, Person.ALL);
    }

    /**
     * Constructor.
     * @param p probability.
     * @param l rumor lifespan.
     * @param s1 distribution of s1.
     * @param s2 distribution of s2.
     * @param s3 distribution of s3.
     * @param s4 distribution of s4.
     * @param ofType type of neighbors.
     */
    public DataCollector(double p, int l, double s1, double s2, double s3, double s4, byte ofType) {
        this.simulator = new Simulator(p, l, GRID_SIZE, s1, s2, s3, s4);
        this.p = p;
        this.l = l;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.nType = ofType;
    }

    public DataCollector(Solution s, byte ofType) {
        this.l = s.getL();
        this.s1 = s.getS1();
        this.s2 = s.getS2();
        this.s3 = s.getS3();
        this.s4 = s.getS4();
        this.nType = ofType;
        this.simulator = new Simulator(s);
    }

    public DataCollector(Solution s) {
        this(s, Person.ALL);
    }

    /**
     * run the data collecting process.
     */
    public void play() {
        for(int j = 0; j < ITERATIONS; j++) {
            this.ratesSum[0] += this.simulator.getInfectionRate() / (double) ITERATIONS;
            for (int i = 1; i <= ROUNDS; i++) {
                this.simulator.makeStep(this.nType);
                this.ratesSum[i] += this.simulator.getInfectionRate() / (double) ITERATIONS;
            }
            this.simulator.reset(this.p, this.l, this.s1, this.s2, this.s3, this.s4);
        }
    }

    /**
     * export the collected data to csv file.
     */
    public void exportToCSV() {
        StringBuilder dataBuilder = new StringBuilder();
        StringBuilder nameBuilder = new StringBuilder();
        dataBuilder.append("x,y\n");
        for (int i = 0; i <= ROUNDS; i++) {
            dataBuilder.append(i);
            dataBuilder.append(",");
            dataBuilder.append(String.format("%.4f", this.ratesSum[i]));
            dataBuilder.append("\n");
        }
        nameBuilder.append("csv/");
        nameBuilder.append("p-");
        nameBuilder.append(this.p);
        nameBuilder.append("l-");
        nameBuilder.append(this.l);
        nameBuilder.append("s1-");
        nameBuilder.append(this.s1);
        nameBuilder.append("s2-");
        nameBuilder.append(this.s2);
        nameBuilder.append("s3-");
        nameBuilder.append(this.s3);
        nameBuilder.append("s4-");
        nameBuilder.append(this.s4);
        nameBuilder.append(".csv");

        try (FileWriter writer = new FileWriter(nameBuilder.toString())) {
            writer.append(dataBuilder.toString());
            writer.flush();
            System.out.println("Data written to CSV file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing data to CSV file: " + e.getMessage());
        }
    }

    /**
     * tool used for generating sets of data.
     * @param pStart start value of p
     * @param pEnd end value of p
     * @param pInc increment value for p
     * @param lStart start value of l
     * @param lEnd end value of l
     * @param lInc increment value of l
     * @param s1 distribution of s1.
     * @param s2 distribution of s2.
     * @param s3 distribution of s3.
     * @param s4 distribution of s4.
     * @param ofType type of neighbors.
     */
    public static void Iterate(double pStart, double pEnd, double pInc, int lStart, int lEnd, int lInc, double s1, double s2, double s3, double s4, byte ofType) {
        for(double pCurrent = pStart; pCurrent <= pEnd; pCurrent += pInc) {
            for (int lCurrent = lStart; lCurrent <= lEnd; lCurrent += lInc) {
                DataCollector dc = new DataCollector(pCurrent, lCurrent, s1, s2, s3, s4, ofType);
                dc.play();
                dc.exportToCSV();
            }
        }
    }

    public static void Iterate(int lStart, int lEnd, int lInc, byte ofType) {
        for (int lCurrent = lStart; lCurrent <= lEnd; lCurrent += lInc) {
            Solution s = new OnionSolution(GRID_SIZE, lCurrent);
            DataCollector dc = new DataCollector(s, ofType);
            dc.play();
            dc.exportToCSV();
        }
    }
}
