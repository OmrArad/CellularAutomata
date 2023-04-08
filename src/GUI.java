import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GUI implements ActionListener {
    private static final int L = 30;
    private static final double P = 0.8;
    private static final int DIMENSION = 100;
    private static final int FRAME_SIZE = 5000;

    private final JFrame frame = new JFrame();
    private final JPanel controls = new JPanel();
    private final JPanel table = new JPanel();
    private final HashMap<Location, JButton> jbs = new HashMap<>();
    private JButton playButton;
    private JButton stopButton;
    private JButton resetButton;
    private JButton skipButton;
    private JSpinner pValueSpinner;
    private JSpinner lValueSpinner;
    private JSpinner speedValueSpinner;
    private JSpinner skipValueSpinner;
    private boolean shouldStop = false;
    private int lastL = L;
    private double lastP = P;
    private final Simulator sim;


    public GUI() {
        this.sim = new Simulator(P, L, DIMENSION);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_SIZE,FRAME_SIZE);
        this.frame.setLayout(new BorderLayout());
        this.setControlsPanel();
        this.setSimulationPanel();
        this.initData(this.sim.getInfoMap());
    }

    private void initData(Map<Location, Person> data) {
        for (Person p : data.values()) {
            JButton jb = this.jbs.get(p.getLocation());
            jb.setBackground(p.getColor());
        }
    }

    private void update(Set<Person> changed) {
        for (Person p : changed) {
            JButton jb = this.jbs.get(p.getLocation());
            jb.setBackground(p.getColor());
        }
    }

    public void play() {
        frame.setVisible(true);
    }

    private void setControlsPanel() {
        this.controls.setLayout(new FlowLayout());

        JButton pb = new JButton("Play");
        pb.setIcon(new ImageIcon("icons/play.png"));
        pb.addActionListener(this);
        this.playButton = pb;
        this.controls.add(this.playButton);

        JButton pause = new JButton("Pause");
        pause.setIcon(new ImageIcon("icons/pause.png"));
        pause.addActionListener(this);
        pause.setEnabled(false);
        this.stopButton = pause;
        this.controls.add(this.stopButton);

        JButton reset = new JButton("Reset");
        reset.setIcon(new ImageIcon("icons/reset.png"));
        reset.addActionListener(this);
        this.resetButton = reset;
        this.controls.add(this.resetButton);

        JLabel l1 = new JLabel("L:");
        l1.setBorder(new EmptyBorder(5,25,5,2));
        this.controls.add(l1);

        SpinnerModel model1 = new SpinnerNumberModel(L, 0, Integer.MAX_VALUE, 1);
        JSpinner spinner1 = new JSpinner(model1);
        this.controls.add(spinner1);
        this.lValueSpinner = spinner1;

        JLabel l2 = new JLabel("P:");
        l2.setBorder(new EmptyBorder(5,25,5,2));
        this.controls.add(l2);

        SpinnerModel model2 = new SpinnerNumberModel(P, 0.00, 1.00, 0.01);
        JSpinner spinner2 = new JSpinner(model2);
        spinner2.setPreferredSize(new Dimension(50, spinner2.getMinimumSize().height));
        this.controls.add(spinner2);
        this.pValueSpinner = spinner2;

        JLabel l3 = new JLabel("Speed:");
        l3.setIcon(new ImageIcon("icons/speedometer.png"));
        l3.setBorder(new EmptyBorder(5,25,5,2));
        this.controls.add(l3);

        SpinnerModel model3 = new SpinnerNumberModel(1.0, 0.25, 5.00, 0.25);
        JSpinner spinner3 = new JSpinner(model3);
        this.controls.add(spinner3);
        this.speedValueSpinner = spinner3;


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

        this.frame.add(this.controls, BorderLayout.NORTH);
    }

    private void setSimulationPanel() {
        this.table.setLayout(new GridLayout(GUI.DIMENSION, GUI.DIMENSION));
        for (int i = 0; i < GUI.DIMENSION; i++) {
            for (int j = 0; j < GUI.DIMENSION; j++) {
                JButton jb = new JButton();
                this.table.add(jb);
                jb.setOpaque(true);
                jb.setBackground(Color.WHITE);
                this.jbs.put(new Location(i, j), jb);
            }
        }
        this.frame.add(this.table, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.playButton) {
            this.pValueSpinner.setEnabled(false);
            this.lValueSpinner.setEnabled(false);
            this.playButton.setEnabled(false);
            this.stopButton.setEnabled(true);
            this.shouldStop = false;
            new Thread(this::run).start();
        } else if (e.getSource() == this.stopButton) {
            this.pValueSpinner.setEnabled(true);
            this.lValueSpinner.setEnabled(true);
            this.playButton.setEnabled(true);
            this.stopButton.setEnabled(false);
            this.shouldStop = true;
        } else if (e.getSource() == this.resetButton) {
            this.pValueSpinner.setEnabled(true);
            this.lValueSpinner.setEnabled(true);
            this.shouldStop = true;
            this.playButton.setEnabled(true);
            this.stopButton.setEnabled(false);
            double p = Double.parseDouble(this.pValueSpinner.getValue().toString());
            this.lastP = p;
            int l = Integer.parseInt(this.lValueSpinner.getValue().toString());
            this.lastL = l;
            this.sim.reset(p, l);
            for (JButton b : this.jbs.values()) {
                b.setBackground(Color.WHITE);
            }
            this.initData(this.sim.getInfoMap());
        } else if (e.getSource() == this.skipButton) {
            this.shouldStop = true;
            this.pValueSpinner.setEnabled(true);
            this.lValueSpinner.setEnabled(true);
            this.playButton.setEnabled(false);
            this.stopButton.setEnabled(false);
            this.resetButton.setEnabled(false);
            this.lValueSpinner.setValue(this.lastL);
            this.pValueSpinner.setValue(this.lastP);
            int steps = Integer.parseInt(this.skipValueSpinner.getValue().toString());
            for (int i = 0; i < steps; i++) {
                this.sim.makeStep();
                this.update(this.sim.getChanged());
            }
            this.update(this.sim.getChanged());
            this.playButton.setEnabled(true);
            this.resetButton.setEnabled(true);
        }
    }

    private static void pause(double seconds)
    {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {}
    }

    private void run() {
        this.lValueSpinner.setValue(this.lastL);
        this.pValueSpinner.setValue(this.lastP);
        while(!this.shouldStop) {
            double speed = Double.parseDouble(this.speedValueSpinner.getValue().toString());
            this.sim.makeStep();
            this.update(this.sim.getChanged());
            pause(1 / speed);
        }
    }
}
