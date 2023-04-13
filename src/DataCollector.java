import java.io.FileWriter;
import java.io.IOException;

public class DataCollector {
    private static final int ROUNDS = 120;
    private static final int ITERATIONS = 10;
    private static final int GRID_SIZE = 100;

    private double p;
    private int l;
    private double s1;
    private double s2;
    private double s3;
    private double s4;
    private double[] ratesSum = new double[ROUNDS + 1];
    private final Simulator simulator;

    public DataCollector(double p, int l, double s1, double s2, double s3, double s4) {
        this.simulator = new Simulator(p, l, GRID_SIZE, s1, s2, s3, s4);
        this.p = p;
        this.l = l;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
    }

    public void play() {
        for(int j = 0; j < ITERATIONS; j++) {
            this.ratesSum[0] += this.simulator.getInfectionRate() / (double) ITERATIONS;
            for (int i = 1; i <= ROUNDS; i++) {
                this.simulator.makeStep();
                this.ratesSum[i] += this.simulator.getInfectionRate() / (double) ITERATIONS;
            }
            this.simulator.reset(this.p, this.l, this.s1, this.s2, this.s3, this.s4);
        }
    }

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

    public static void Iterate(double pStart, double pEnd, double pInc, int lStart, int lEnd, int lInc, double s1, double s2, double s3, double s4) {
        for(double pCurrent = pStart; pCurrent <= pEnd; pCurrent += pInc) {
            for (int lCurrent = lStart; lCurrent <= lEnd; lCurrent += lInc) {
                DataCollector dc = new DataCollector(pCurrent, lCurrent, s1, s2, s3,s4);
                dc.play();
                dc.exportToCSV();
            }
        }
    }
}
