import java.util.concurrent.TimeUnit;

/**
 * A typing race simulation. A maximum of three typists race to complete a passage of text,
 * advancing character by character, sliding backwards when they mistype or burning out if they push too hard.
 *
 * @author Adil Akylov
 * @version 1.0
 */
public class TypingRace
{
    private final int passageLength;
    private int steps_since_start;
    private final Typist[] typists;
    private int seatsTaken;
    
    public static final int MAX_TYPISTS = 6;
    private static final int STEP_DURATION_MS = 200;
    private static final int MINS_TO_FINISH_RACE = 2;
    private static final double CHARACTERS_IN_WORD = 5;

    // Accuracy thresholds and penalties for mistype and burnout events
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final double BURNOUT_CAP_CHANCE = 0.05;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated and the steps since start is 0
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
        this.typists = new Typist[MAX_TYPISTS];
        this.seatsTaken = 0;
        this.steps_since_start = 0;
    }

    /**
     * @return how many typist seats are taken
     */
    public int getSeatsTaken(){
        return this.seatsTaken;
    }

    /**
     * @return an array of typists
     */
    public Typist[] getTypists(){
        Typist[] trimmed = new Typist[this.seatsTaken];
        System.arraycopy(this.typists, 0, trimmed, 0, this.seatsTaken);
        return trimmed;
    }

    /**
     * Seats a typist.
     *
     * @param theTypist  the typist to seat
     * @throws RulesException throws exception if trying to add past typist limit
     */
    public void addTypist(Typist theTypist) throws RulesException
    {
        if(this.seatsTaken >= MAX_TYPISTS){
            throw new RulesException("trying to add typists past limit");
        }
        this.typists[this.seatsTaken++] = theTypist;
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until at least one typist completes the full passage.
     */
    public void startRace() throws RulesException
    {
        boolean finished = false;
        if(this.seatsTaken < 2){
            throw new RulesException("Not enough people for a race");
        }

        this.steps_since_start = 0;
        while (!finished)
        {
            // Advance each typist by one turn
            for (Typist typist : this.typists) {
                this.advanceTypist(typist);
            }

            // Print the current state of the race
            printRace();

            // Check if any typist has finished the passage
            for(Typist typist : this.typists){
                if(raceFinishedBy(typist)){
                    finished = true;
                }
            }

            this.steps_since_start++;
            if(TimeUnit.MILLISECONDS.toMinutes(this.steps_since_start * STEP_DURATION_MS) >= MINS_TO_FINISH_RACE){
                this.printSystemMessage("Race finished by timeout!");
                break;
            }
            // Wait 200ms between turns so the animation is visible
            try {
                TimeUnit.MILLISECONDS.sleep(STEP_DURATION_MS);
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
    private void handleWinningTypists(){
        for(Typist typist : this.typists){
            if(raceFinishedBy(typist)){
                String oldAcc = String.format("%.3f", typist.getAccuracy());
                typist.winRace();
                String newAcc = String.format("%.3f", typist.getAccuracy());
                this.printGoodMessage(typist.getName() + " won!\nAccuracy improved from " + oldAcc + " to " + newAcc);
            }
        }
    }

    /**
     * calculates and formats the time since start of the race. if 0 minutes passed only shows seconds
     * @return time since start as a string in form "([0-9]+ minutes and )?[0-9]+ seconds"
     */
    private String timeSinceStart(){
        int ms_since_start = this.steps_since_start * STEP_DURATION_MS;
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
    private void printGoodMessage(String message){
        printMessage(message);
    }

    /**
     * same as printMessage, can be used by subclasses to signify a system message
     * @param message the message to be printed
     */
    private void printSystemMessage(String message){
        printMessage(message);
    }

    /**
     * prints the message to the user
     * @param message the message to be printed
     */
    private void printMessage(String message){
        System.out.println(message);
    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn's worth and skip typing.
     * Otherwise:
     *   - They may type a character (advancing progress) based on their accuracy.
     *   - They may mistype (sliding back) — the chance of a mistype should decrease
     *     for more accurate typists.
     *   - They may burn out — more likely for very high-accuracy typists
     *     who are pushing themselves too hard.
     *
     * state of the typist is symbolically stored in postfix and cleared if typist typed correctly.
     *
     * @param theTypist the typist to advance
     */
    private void advanceTypist(Typist theTypist) throws RulesException
    {
        if(theTypist == null)return;
        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }
        
        theTypist.setPostfix("");
        // Attempt to type a character
        if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
        }

        // Mistype check
        if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
            theTypist.setPostfix("[<]");
        }

        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
        if (Math.random() < BURNOUT_CAP_CHANCE * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
            theTypist.setPostfix("~");
        }
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        return theTypist != null && theTypist.getProgress() >= passageLength;
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
    public void printRace()
    {
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        for(Typist typist : this.typists){
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
     * @param theTypist the typist whose lane to print
     */
    private void printSeat(Typist theTypist)
    {
        if(theTypist == null)return;
        int spacesBefore = theTypist.getProgress();
        int spacesAfter  = passageLength - theTypist.getProgress();

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        System.out.print(theTypist.getSymbol());
        spacesAfter -= theTypist.getPostfix().length();
        System.out.print(theTypist.getPostfix());

        multiplePrint(' ', spacesAfter);
        System.out.print("| ");

        // Print name, wpm and accuracy
        System.out.print(theTypist.getName() + " | " + this.calculateWPM(theTypist) + " WPM");
        System.out.print(" (Accuracy: " + String.format("%.2f", theTypist.getAccuracy()) + ")");
        if (theTypist.isBurntOut()){
            System.out.print(" BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }System.out.println();
    }

    private int calculateWPM(Typist theTypist){
        if(this.steps_since_start == 0)return 0;
        double wordsProgress = theTypist.getProgress() / CHARACTERS_IN_WORD;
        double minutesPassed = this.steps_since_start * STEP_DURATION_MS / 1000.0 / 60.0;
        double wpm = wordsProgress / minutesPassed;
        return (int) wpm;
    }

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}