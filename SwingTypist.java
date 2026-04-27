public class SwingTypist extends Typist {
    public SwingTypist(char typistSymbol, String typistName, double typistAccuracy) {
        super(typistSymbol, typistName, typistAccuracy);
        System.err.println("typistSymbol does not affect SwingTypist");
    }
    public SwingTypist(String typistName, double typistAccuracy){
        super(' ', typistName, typistAccuracy);
    }
}
