/**
 * An extension of Typist that is used for swing typing races.
 *
 * @author Adil Akylov
 * @version 1.1
 */
public class SwingTypist extends Typist {
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given name, and accuracy rating.
     *
     * @param typistSymbol  not used
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public SwingTypist(char typistSymbol, String typistName, double typistAccuracy) {
        super(typistSymbol, typistName, typistAccuracy);
        System.err.println("typistSymbol does not affect SwingTypist");
    }
    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given name, and accuracy rating.
     *
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public SwingTypist(String typistName, double typistAccuracy){
        super(' ', typistName, typistAccuracy);
    }
}
