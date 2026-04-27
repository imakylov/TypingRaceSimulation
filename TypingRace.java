import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A typing race simulation. A maximum of three typists race to complete a passage of text,
 * advancing character by character, sliding backwards when they mistype or burning out if they push too hard.
 *
 * @author Adil Akylov
 * @version 1.0
 */
public class TypingRace<T extends Typist>
{
    protected int passageLength;
    private int steps_since_start;
    protected ArrayList<T> typists;
    protected int seatsTaken;
    
    private static final double CHARACTERS_IN_WORD = 5;
    public int getMaxTypists(){return 3;}
    public int getStepDurationMS(){return 200;}
    public int getMinsToFinishRace(){return 2;}

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated and the steps since start is 0
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(int passageLength){
        this.typists = new ArrayList<>();
        this.seatsTaken = 0;
        this.steps_since_start = 0;
        this.passageLength = passageLength;
    }

    /**
     * @return how many typist seats are taken
     */
    public int getSeatsTaken(){
        return this.typists.size();
    }

    /**
     * Seats a typist.
     *
     * @param typist  the typist to seat
     * @throws RulesException throws exception if trying to add past typist limit
     */
    public void addTypist(T typist) throws RulesException{
        if(this.seatsTaken >= this.getMaxTypists()){
            throw new RulesException("trying to add typists past limit");
        }
        this.typists.add(typist);
    }

    public void checkRaceValid() throws RulesException{
        if(this.getSeatsTaken() < 2){
            throw new RulesException("Not enough people for a race");
        }
        if(this.passageLength == 0){
            throw new RulesException("Passage is not set");
        }
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until at least one typist completes the full passage.
     */
    public void startRace() throws RulesException{
        this.checkRaceValid();
        
        this.steps_since_start = 0;
        boolean finished = false;
        while (!finished){
            // Advance each typist by one turn
            for (T typist : this.typists) {
                typist.advance();
            }

            // Print the current state of the race
            printRace();

            // Check if any typist has finished the passage
            for(T typist : this.typists){
                if(raceFinishedBy(typist)){
                    finished = true;
                }
            }

            this.steps_since_start++;
            if(TimeUnit.MILLISECONDS.toMinutes(this.steps_since_start * this.getStepDurationMS()) >= this.getMinsToFinishRace()){
                this.printSystemMessage("Race finished by timeout!");
                break;
            }
            // Wait 200ms between turns so the animation is visible
            try {
                TimeUnit.MILLISECONDS.sleep(this.getStepDurationMS());
            } catch (InterruptedException e) {
                throw new RulesException("Race interrupted!");
            }
        }
        this.handleWinningTypists();
        this.printSystemMessage("The race went on for " + this.timeSinceStart());
    }
    
    /**
     * does all the preparations for all winning typists.
     * More specificallty calls winRace for all winners and prints a winning message
    */
    protected void handleWinningTypists(){
        for(T typist : this.typists){
            if(raceFinishedBy(typist)){
                String oldAcc = typist.formattedAccuracy(3);
                typist.winRace();
                String newAcc = typist.formattedAccuracy(3);
                this.printGoodMessage(typist.getName() + " won!\nAccuracy improved from " + oldAcc + " to " + newAcc);
            }
        }
    }

    /**
     * calculates and formats the time since start of the race. if 0 minutes passed only shows seconds
     * @return time since start as a string in form "([0-9]+ minutes and )?[0-9]+ seconds"
     */
    protected String timeSinceStart(){
        int ms_since_start = this.steps_since_start * this.getStepDurationMS();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms_since_start);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        seconds -= TimeUnit.MINUTES.toSeconds(minutes);
        String message = "";
        if(minutes != 0){
            message += minutes + " minutes and ";
        }
        message += seconds + " seconds.";
        return message;
    }

    /**
     * same as printMessage, can be used by subclasses to signify a positive message
     * @param message the message to be printed
     */
    protected void printGoodMessage(String message){
        printMessage(message);
    }

    /**
     * same as printMessage, can be used by subclasses to signify a system message
     * @param message the message to be printed
     */
    protected void printSystemMessage(String message){
        printMessage(message);
    }

    /**
     * prints the message to the user
     * @param message the message to be printed
     */
    protected void printMessage(String message){
        System.out.println(message);
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param typist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    protected boolean raceFinishedBy(T typist){
        return typist != null && typist.getProgress() >= passageLength;
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
    public void printRace(){
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        for(T typist : this.typists){
            printSeat(typist);
        }

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  ~ = burnt out    [<] = just mistyped");
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    A~                 | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * @param typist the typist whose lane to print
     */
    protected void printSeat(T typist){
        if(typist == null)return;
        int spacesBefore = typist.getProgress();
        int spacesAfter  = passageLength - typist.getProgress();

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        System.out.print(typist.getSymbol());
        spacesAfter -= typist.getPostfix().length();
        System.out.print(typist.getPostfix());

        multiplePrint(' ', spacesAfter);
        System.out.print("| ");

        // Print name, wpm and accuracy
        System.out.print(typist.getName() + " | " + this.calculateWPM(typist) + " WPM");
        System.out.print(" (Accuracy: " + typist.formattedAccuracy(2) + ")");
        if (typist.isBurntOut()){
            System.out.print(" BURNT OUT (" + typist.getBurnoutTurnsRemaining() + " turns)");
        }System.out.println();
    }

    protected int calculateWPM(T typist){
        if(this.steps_since_start == 0)return 0;
        double wordsProgress = typist.getProgress() / CHARACTERS_IN_WORD;
        double minutesPassed = this.steps_since_start * this.getStepDurationMS() / 1000.0 / 60.0;
        double wpm = wordsProgress / minutesPassed;
        return (int) wpm;
    }

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times){
        int i = 0;
        while (i < times){
            System.out.print(aChar);
            i = i + 1;
        }
    }
}