/**
 * Used for cleaner access to GUI class, like a platypus, it doesn't do much.
 */
public class Facade {

    private final GUI gui;

    /**
     * Constructor.
     */
    public Facade() {
        this.gui = new GUI();
    }

    /**
     * run the gui.
     */
    public void play() {
        this.gui.play();
    }

}
