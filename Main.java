import java.util.Scanner;

/**
 * Entry point for starting a simple race defined in TypingRace.java.
 * Creates a TypingRace with three typists with different accuracies and starts the race.
 *
 * @author Adil Akylov
 * @version 1.0
 */
public class Main {
    /**
     * Entry point when running Main.java.
     * Creates a TypingRace with three typists with different accuracies and starts the race.
     *
     */
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        TypingRace race = new TypingRace(askInt("What is the passage length?"));
        showCommands();
        String command = ask("What would you like to do?");
        while(!command.equals("e")){
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
                    }
                }
                case "q" -> {
                    race = new TypingRace(40);
                    race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
                    race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60), 2);
                    race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30), 3);
                }
                case "a" -> {
                    int seatNumber = askInt("What seat should the typist occupy? (1-3)");
                    String typistName = ask("What should the typist be named?");
                    double accuracy = askDouble("How accurate are they? (0.0-1.0)");
                    Typist t = new Typist((char)('①'+seatNumber-1), typistName, accuracy);
                    race.addTypist(t, seatNumber);
                }
                case "c" -> {
                    race = new TypingRace(askInt("What is the passage length?"));
                }
                default -> {
                    System.out.println("Command unrecognized");
                }
            }command = ask("What would you like to do?");
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
