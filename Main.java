import java.util.Scanner;

/**
 * Entry point for starting a simple race defined in TypingRace.java.
 * Creates a TypingRace with three typists with different accuracies and starts the race.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);
    /**
     * Entry point when running Main.java.
     * Creates a TypingRace with three typists with different accuracies and starts the race.
     *
     */
    public static void main(String[] args) throws RulesException{
        TypingRace<Typist> race = new TypingRace<>(askInt("What is the passage length?"));
        showCommands();
        String command = "";
        while(!command.equals("e")){
            command = ask("What would you like to do?");
            switch(command) {
                case "h" -> {
                    showCommands();
                }
                case "p" -> {
                    race.printRace();
                }
                case "s" -> {
                    try{
                        race.startRace();
                    }catch (RulesException e){
                        System.out.println("Rules Exception!!");
                        System.out.println(e.message);
                        if(e.fatal) throw e;
                    }
                }
                case "q" -> {
                    race = new TypingRace<>(40);
                    try{
                        race.addTypist(new Typist('①', "TURBOFINGERS", 0.85));
                        race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60));
                        race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30));
                    }catch (RulesException e){
                        System.out.println("Rules Exception!!");
                        System.out.println(e.message);
                        if(e.fatal)throw e;
                    }
                }
                case "a" -> {
                    if(race.getSeatsTaken() == race.getMaxTypists()){
                        System.out.println("All seats taken");
                        continue;
                    }
                    String typistName = ask("What should the typist be named?");
                    double accuracy = askDouble("How accurate are they? (0.0-1.0)");
                    Typist t = new Typist((char)('①'+race.getSeatsTaken()), typistName, accuracy);
                    try {
                        race.addTypist(t);
                    }catch (RulesException e) {
                        System.out.println("Rules Exception!!");
                        System.out.println(e.message);
                        if(e.fatal)throw e;
                    }
                }
                case "c" -> {
                    race = new TypingRace<>(askInt("What is the passage length?"));
                }
                default -> {
                    System.out.println("Command unrecognized");
                }
            }
        }System.out.println("Closing the program");
    }
    private static void showCommands(){
        System.out.println("(h)elp: shows this message again");
        System.out.println("(p)rint: shows current typists");
        System.out.println("(s)tart: starts game with current typists");
        System.out.println("(q)uick: configure default race");
        System.out.println("(a)dd: adds a typist");
        System.out.println("(c)lear: remove all typists and reassign passage length");
        System.out.println("(e)xit: stops the program");
    }
    private static int askInt(String prompt){
        return Integer.parseInt(ask(prompt));
    }
    private static double askDouble(String prompt){
        return Double.parseDouble(ask(prompt));
    }
    private static String ask(String prompt){
        System.out.println(prompt);
        return scanner.nextLine();
    }
}
