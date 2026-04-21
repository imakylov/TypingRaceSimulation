/**
 * Entry point for starting a simple race defined in TypingRace.java.
 * Creates a TypingRace with three typists with different accuracies and starts the race.
 *
 * @author Adil Akylov
 * @version 0.9
 */
public class Main {
    /**
     * Entry point when running Main.java.
     * Creates a TypingRace with three typists with different accuracies and starts the race.
     *
     */
    public static void main(String[] args) throws RulesException
    {
        TypingRace race = new TypingRace(40);
        race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
        race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60), 2);
        race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30), 3);
        try {
            race.startRace();
        } catch (RulesException e) {
            System.out.println("Rules Exception!");
            System.out.println(e.message);
            throw e;
        }
    }
}
