import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * GUI class, implementation of the visual display.
 */
public class GUI implements ActionListener {
    // constant values.
    private static final int L = 30;
    private static final double P = 0.8;
    private static final double S = 0.25;
    private static final int DIMENSION = 100;
    private static final int FRAME_SIZE = 5000;

    // the frame that binds everything together.
    private final JFrame frame = new JFrame();
    // panel holding the buttons and controls.
    private final JPanel controls = new JPanel();
    // panel that displays the simulation itself.
    private final JPanel table = new JPanel();
    // mapping location -> button, used for the simulation display.
    private final HashMap<Location, JButton> jbs = new HashMap<>();
    // buttons, part of the control panel
    private JButton playButton;
    private JButton stopButton;
    private JButton resetButton;
    private JButton skipButton;
    // spinners, part of the control panel
    private JSpinner pValueSpinner;
    private JSpinner lValueSpinner;
    private JSpinner speedValueSpinner;
    private JSpinner skipValueSpinner;
    private JSpinner s1Spinner;
    private JSpinner s2Spinner;
    private JSpinner s3Spinner;
    private JSpinner s4Spinner;
    // label, part of the control panel.
    private JLabel countLabel;

    private int count = 0;
    private boolean shouldStop = false;
    private int lastL = L;
    private double lastP = P;
    private double lastS1 = S;
    private double lastS2 = S;
    private double lastS3 = S;
    private double lastS4 = S;
    private final Simulator sim;

    /**
     * Constructor.
     */
    public GUI() {
        // initialize new simulation using the default values of p,l and grid size.
        this.sim = new Simulator(P, L, DIMENSION);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set frame size
        this.frame.setSize(FRAME_SIZE,FRAME_SIZE);
        //set frame layout
        this.frame.setLayout(new BorderLayout());
        // set the controls and the simulation panel.
        this.setControlsPanel();
        this.setSimulationPanel();
        // initialize the data.
        this.initData(this.sim.getInfoMap());
    }

    /**
     * set the adequate colours for each spot.
     * @param data map of location -> person.
     */
    private void initData(Map<Location, Person> data) {
        for (Person p : data.values()) {
            JButton jb = this.jbs.get(p.getLocation());
            jb.setBackground(p.getColor(0));
        }
    }

    /**
     * update the needed spots.
     * @param changed a set of type Person. the people that changed in the last simulation step.
     */
    private void update(Set<Person> changed) {
        for (Person p : changed) {
            JButton jb = this.jbs.get(p.getLocation());
            jb.setBackground(p.getColor(this.sim.getCurrentRound()));
        }
    }

    /**
     * update the needed spots.
     * @param data a map of data that changed in the last simulation run.
     * @param round the current round number.
     */
    private void update(Map<Location, Person> data, int round) {
        for (Person p : data.values()) {
            JButton jb = this.jbs.get(p.getLocation());
            jb.setBackground(p.getColor(round));
        }
    }

    /**
     * set the frame as visible. present it.
     */
    public void play() {
        frame.setVisible(true);
    }

    /**
     * set all the needed controls in the controls panel.
     */
    private void setControlsPanel() {
        // set layout for the panel
        this.controls.setLayout(new FlowLayout());

        // make play button
        JButton pb = new JButton("Play");
        pb.setIcon(new ImageIcon("icons/play.png"));
        pb.addActionListener(this);
        this.playButton = pb;
        this.controls.add(this.playButton);

        // make pause button
        JButton pause = new JButton("Pause");
        pause.setIcon(new ImageIcon("icons/pause.png"));
        pause.addActionListener(this);
        pause.setEnabled(false);
        this.stopButton = pause;
        this.controls.add(this.stopButton);

        // make reset button
        JButton reset = new JButton("Reset");
        reset.setIcon(new ImageIcon("icons/reset.png"));
        reset.addActionListener(this);
        this.resetButton = reset;
        this.controls.add(this.resetButton);

        // make l value spinner
        JLabel l1 = new JLabel("L:");
        l1.setBorder(new EmptyBorder(5,25,5,2));
        this.controls.add(l1);

        SpinnerModel model1 = new SpinnerNumberModel(L, 0, Integer.MAX_VALUE, 1);
        JSpinner spinner1 = new JSpinner(model1);
        this.controls.add(spinner1);
        this.lValueSpinner = spinner1;

        // make p value spinner
        JLabel l2 = new JLabel("P:");
        l2.setBorder(new EmptyBorder(5,25,5,2));
        this.controls.add(l2);

        SpinnerModel model2 = new SpinnerNumberModel(P, 0.00, 1.00, 0.01);
        JSpinner spinner2 = new JSpinner(model2);
        spinner2.setPreferredSize(new Dimension(50, spinner2.getMinimumSize().height));
        this.controls.add(spinner2);
        this.pValueSpinner = spinner2;

        // make speed value spinner
        JLabel l3 = new JLabel("Speed:");
        l3.setIcon(new ImageIcon("icons/speedometer.png"));
        l3.setBorder(new EmptyBorder(5,25,5,2));
        this.controls.add(l3);

        SpinnerModel model3 = new SpinnerNumberModel(1.0, 0.25, 5.00, 0.25);
        JSpinner spinner3 = new JSpinner(model3);
        this.controls.add(spinner3);
        this.speedValueSpinner = spinner3;

        // make skip steps spinner and button.
        JLabel l4 = new JLabel("    Steps:");
        this.controls.add(l4);


        SpinnerModel model4 = new SpinnerNumberModel(1, 1, 100000, 1);
        JSpinner spinner4 = new JSpinner(model4);
        this.controls.add(spinner4);
        this.skipValueSpinner = spinner4;

        JButton skip = new JButton("Skip");
        skip.setIcon(new ImageIcon("icons/skip.png"));
        skip.addActionListener(this);
        this.skipButton = skip;
        this.controls.add(this.skipButton);

        // s1,s2,s3,4 ratios
        JLabel l5 = new JLabel("    S1:");
        this.controls.add(l5);
        SpinnerModel model5 = new SpinnerNumberModel(0.25, 0, 1, 0.01);
        JSpinner s1Spinner = new JSpinner(model5);
        s1Spinner.setPreferredSize(new Dimension(50, s1Spinner.getMinimumSize().height));
        this.controls.add(s1Spinner);
        this.s1Spinner = s1Spinner;
        this.controls.add(this.s1Spinner);

        JLabel l6 = new JLabel("S2:");
        this.controls.add(l6);
        SpinnerModel model6 = new SpinnerNumberModel(0.25, 0, 1, 0.01);
        JSpinner s2spinner = new JSpinner(model6);
        s2spinner.setPreferredSize(new Dimension(50, s2spinner.getMinimumSize().height));
        this.controls.add(s2spinner);
        this.s2Spinner = s2spinner;
        this.controls.add(this.s2Spinner);

        JLabel l7 = new JLabel("S3:");
        this.controls.add(l7);
        SpinnerModel model7 = new SpinnerNumberModel(0.25, 0, 1, 0.01);
        JSpinner s3spinner = new JSpinner(model7);
        s3spinner.setPreferredSize(new Dimension(50, s3spinner.getMinimumSize().height));
        this.controls.add(s3spinner);
        this.s3Spinner = s3spinner;
        this.controls.add(this.s3Spinner);

        JLabel l8 = new JLabel("S4:");
        this.controls.add(l8);
        SpinnerModel model8 = new SpinnerNumberModel(0.25, 0, 1, 0.01);
        JSpinner s4spinner = new JSpinner(model8);
        s4spinner.setPreferredSize(new Dimension(50, s4spinner.getMinimumSize().height));
        this.controls.add(s4spinner);
        this.s4Spinner = s4spinner;
        this.controls.add(this.s4Spinner);

        // make round counter
        JLabel l9 = new JLabel("    Round:");
        this.controls.add(l9);
        JLabel countLabel = new JLabel("0");
        this.controls.add(countLabel);
        this.countLabel = countLabel;

        // add the panel to the frame
        this.frame.add(this.controls, BorderLayout.NORTH);
    }

    /**
     * set the needed elements of the simulation data.
     */
    private void setSimulationPanel() {
        // set panel layout
        this.table.setLayout(new GridLayout(GUI.DIMENSION, GUI.DIMENSION));
        // make buttons, each button represents a Person.
        for (int i = 0; i < GUI.DIMENSION; i++) {
            for (int j = 0; j < GUI.DIMENSION; j++) {
                JButton jb = new JButton();
                this.table.add(jb);
                jb.setOpaque(true);
                jb.setBackground(Color.WHITE);
                // save (Location, Person) pair in the map.
                this.jbs.put(new Location(i, j), jb);
            }
        }
        // add panel to frame
        this.frame.add(this.table, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // check which button triggered the method.
        if (e.getSource() == this.playButton) {
            // set play button functionality
            // turn off buttons.
            this.pValueSpinner.setEnabled(false);
            this.lValueSpinner.setEnabled(false);
            this.s1Spinner.setEnabled(false);
            this.s2Spinner.setEnabled(false);
            this.s3Spinner.setEnabled(false);
            this.s4Spinner.setEnabled(false);
            this.playButton.setEnabled(false);
            // turn on the pause button.
            this.stopButton.setEnabled(true);
            this.shouldStop = false;
            // run the simulation and update the display in a new thread.
            new Thread(this::run).start();
        } else if (e.getSource() == this.stopButton) {
            // set pause button functionality
            // turn on buttons
            this.pValueSpinner.setEnabled(true);
            this.lValueSpinner.setEnabled(true);
            this.s1Spinner.setEnabled(true);
            this.s2Spinner.setEnabled(true);
            this.s3Spinner.setEnabled(true);
            this.s4Spinner.setEnabled(true);
            this.playButton.setEnabled(true);
            // turn off the pause button
            this.stopButton.setEnabled(false);
            this.shouldStop = true;
        } else if (e.getSource() == this.resetButton) {
            // set reset button functionality
            //reset the count (current round) value to 0
            this.count = 0;
            this.countLabel.setText("0");
            // turn on
            this.pValueSpinner.setEnabled(true);
            this.lValueSpinner.setEnabled(true);
            this.shouldStop = true;
            this.playButton.setEnabled(true);
            // turn off pause button.
            this.stopButton.setEnabled(false);
            // turn on ratio spinners.
            this.s1Spinner.setEnabled(true);
            this.s2Spinner.setEnabled(true);
            this.s3Spinner.setEnabled(true);
            this.s4Spinner.setEnabled(true);
            // get and display the updated values.
            double s1 = Double.parseDouble(this.s1Spinner.getValue().toString());
            this.lastS1 = s1;
            double s2 = Double.parseDouble(this.s2Spinner.getValue().toString());
            this.lastS2 = s2;
            double s3 = Double.parseDouble(this.s3Spinner.getValue().toString());
            this.lastS3 = s3;
            double s4 = Double.parseDouble(this.s4Spinner.getValue().toString());
            this.lastS4 = s4;
            double sum = s1 + s2 + s3 + s4;
            // check if the distribution is valid.
            if (sum < 1.0) {
                // in case it does not add up to 1, add the difference to s1.
                this.lastS1 += Math.abs(1.0 - sum);
                this.s1Spinner.setValue(this.lastS1);
            } else if (sum > 1.0) {
                // in case it adds up to more than 1, subtract from s1.
                this.lastS1 -= Math.abs(1.0 - sum);
                // check that s1 is still non negative.
                if (this.lastS1 < 0) {
                    // update values to default in case s1 in negative (previous update failed).
                    this.lastS1 = S;
                    this.lastS2 = S;
                    this.lastS3 = S;
                    this.lastS4 = S;
                    this.s2Spinner.setValue(this.lastS2);
                    this.s3Spinner.setValue(this.lastS3);
                    this.s4Spinner.setValue(this.lastS4);
                }
                this.s1Spinner.setValue(this.lastS1);
            } else {
                // valid distribution case
                this.s1Spinner.setValue(this.lastS1);
                this.s2Spinner.setValue(this.lastS2);
                this.s3Spinner.setValue(this.lastS3);
                this.s4Spinner.setValue(this.lastS4);
            }
            // update p and l values.
            double p = Double.parseDouble(this.pValueSpinner.getValue().toString());
            this.lastP = p;
            int l = Integer.parseInt(this.lValueSpinner.getValue().toString());
            this.lastL = l;
            // reset the simulation.
            this.sim.reset(p, l, this.lastS1, this.lastS2, this.lastS3, this.lastS4);
            // reset all cells colours to white.
            for (JButton b : this.jbs.values()) {
                b.setBackground(Color.WHITE);
            }
            // set the correct colours using the initData method.
            this.initData(this.sim.getInfoMap());
        } else if (e.getSource() == this.skipButton) {
            // set skip button functionality
            this.shouldStop = true;
            // turn on buttons
            this.pValueSpinner.setEnabled(true);
            this.lValueSpinner.setEnabled(true);
            // turn off buttons
            this.playButton.setEnabled(false);
            this.stopButton.setEnabled(false);
            this.resetButton.setEnabled(false);
            // set l, p and steps values.
            this.lValueSpinner.setValue(this.lastL);
            this.pValueSpinner.setValue(this.lastP);
            int steps = Integer.parseInt(this.skipValueSpinner.getValue().toString());
            // run the needed steps.
            for (int i = 0; i < steps; i++) {
                this.count++;
                this.sim.makeStep();
                this.update(this.sim.getChanged());
            }
            // update the visual component.
            this.update(this.sim.getInfoMap(), this.sim.getCurrentRound());
            // update the counter visual component
            this.countLabel.setText(String.valueOf(this.count));
            // turn on needed buttons.
            this.playButton.setEnabled(true);
            this.resetButton.setEnabled(true);
        }
    }

    /**
     * used for delaying the visual progression of the simulation.
     * @param seconds the number of seconds to delay.
     */
    private static void pause(double seconds)
    {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {}
    }

    /**
     * run the simulation.
     */
    private void run() {
        // display the current values of l and p.
        this.lValueSpinner.setValue(this.lastL);
        this.pValueSpinner.setValue(this.lastP);
        while(!this.shouldStop) {
            // update round counter and label.
            this.count++;
            this.countLabel.setText(String.valueOf(this.count));
            double speed = Double.parseDouble(this.speedValueSpinner.getValue().toString());
            // make a single simulation step.
            this.sim.makeStep();
            // update the cells colours.
            this.update(this.sim.getInfoMap(), this.sim.getCurrentRound());
            // delay presentation.
            pause(1 / speed);
        }
    }
}
