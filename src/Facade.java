public class Facade {
    private GUI gui;

    public Facade() {
        this.gui = new GUI();
    }

    public void play() {
        this.gui.play();
    }
}
