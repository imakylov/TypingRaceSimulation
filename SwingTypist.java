
import java.awt.Color;

/**
 * An extension of Typist that is used for swing typing races.
 *
 * @author Adil Akylov
 * @version 1.1
 */
public class SwingTypist extends Typist {
    private Color color;
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given name, and accuracy rating.
     *
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public SwingTypist(String typistName, double typistAccuracy, Color color){
        super(' ', typistName, typistAccuracy);
        this.color = color;
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
}
