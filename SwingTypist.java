
import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * An extension of Typist that is used for swing typing races.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class SwingTypist extends Typist {
    private Color color;
    private int charactersTyped = 0;
    private int MS_played = 0;
    private static ArrayList<SwingTypist> allTypists = null;

    /**
     * @return default list of SwingTypists
    */
    private static ArrayList<SwingTypist> defaultRotation(){
        ArrayList<SwingTypist> typists = new ArrayList<>();
        typists.add(SwingTypist.make("TURBOFINGERS", 0.85, Color.RED));
        typists.add(SwingTypist.make("QWERTY_QUEEN",  0.60, Color.ORANGE));
        typists.add(SwingTypist.make("HUNT_N_PECK",   0.30, Color.YELLOW));
        typists.add(SwingTypist.make("Ivan",   0.50, Color.GREEN));
        typists.add(SwingTypist.make("Alex",   0.51, Color.BLUE));
        typists.add(SwingTypist.make("Monawar",   0.61, new Color(250, 0, 250)));
        typists.add(SwingTypist.make("Oliver",   0.70, Color.PINK));
        return typists;
    }

    public static ArrayList<SwingTypist> getAll(){
        if(allTypists == null){
            defaultRotation();
        }
        return allTypists;
    }
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given name, and accuracy rating.
     *
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    private SwingTypist(String typistName, double typistAccuracy, Color color){
        super(' ', typistName, typistAccuracy);
        this.color = color;
    }

    public static SwingTypist make(String typistName, double typistAccuracy, Color color){
        if(SwingTypist.allTypists == null) SwingTypist.allTypists = new ArrayList<>();
        return SwingTypist.make(() -> new SwingTypist(typistName, typistAccuracy, color));
    }

    public static <T extends SwingTypist> T make(Supplier<T> constructor){
        T typist = constructor.get();
        allTypists.add(typist);
        return typist;
    }

    public static SwingTypist makeRandom(){
        String randomName = "Sample Typist #" + (int)(Math.random()*10);
        return SwingTypist.make(randomName, Math.random(), Utils.randomColor());
    }

    @Override
    public void finishRace(int passageLength, int MS_since_race_start){
        this.charactersTyped += this.getProgress();
        this.MS_played += MS_since_race_start;
        if(passageLength <= this.getProgress()){
            JTypistSelection.updateAll();
        }
    }

    public int getWPM(){
        if(this.MS_played == 0)return 0;
        double wordsProgress = this.charactersTyped / Constants.CHARACTERS_IN_WORD;
        double minutesPassed = this.MS_played / 1000.0 / 60.0;
        double wpm = wordsProgress / minutesPassed;
        return (int) wpm;
    }

    /**
     * @return the color associated with this typist
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Sets typist's color
     * @param color color to set
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * checks if object's fields arethe same as this typist
     * 
     * @param obj object to compare
     * @return true if typist is equal
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof SwingTypist)) return false;
        SwingTypist other = (SwingTypist) obj;
        if(!super.equals(other))return false;
        return this.getColor().equals(other.getColor());
    }

    /**
     * @return hash of typist's name
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
