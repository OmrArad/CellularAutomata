import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class GUI {
    private JFrame frame = new JFrame();
    private HashMap<Location, JButton> jbs = new HashMap<>();
    public  GUI() {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(5000,5000);
        this.frame.setLayout(new GridLayout(100,100));
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                JButton jb = new JButton();
                this.frame.add(jb);
                jb.setOpaque(true);
                this.jbs.put(new Location(i, j), jb);
            }
        }
    }

    public GUI(LinkedList<Person> people) {
        this();
        this.initData(people);
    }

    public void initData(LinkedList<Person> people) {
        for (Person p : people) {
            JButton jb = this.jbs.get(p.getLocation());
            jb.setBackground(p.getColor());
        }
    }

    public void update(HashSet<Person> changed) {
        for (Person p : changed) {
            JButton jb = this.jbs.get(p.getLocation());
            jb.setBackground(p.getColor());
        }
    }

    public void play() {
        frame.setVisible(true);
    }
}
