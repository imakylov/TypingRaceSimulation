import java.util.concurrent.TimeUnit;

/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus, who left this project to "focus on his
 * two-finger technique". He assured us the code was "basically done".
 * We have found evidence to the contrary.
 *
 * @author TyPosaurus
 * @version 0.7 (the other 0.3 is left as an exercise for the reader)
 */
public class TypingRace
{
    final private int passageLength;   // Total characters in the passage to type
    private int steps_since_start;
    private Typist seat1Typist;
    private Typist seat2Typist;
    private Typist seat3Typist;

    private static final int STEP_DURATION_MS = 200;
    private static final double CHARACTERS_IN_WORD = 5;

    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
        this.seat1Typist = null;
        this.seat2Typist = null;
        this.seat3Typist = null;
        this.steps_since_start = 0;
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
    public void addTypist(Typist theTypist, int seatNumber)
    {
        switch (seatNumber) {
            case 1 -> seat1Typist = theTypist;
            case 2 -> seat2Typist = theTypist;
            case 3 -> seat3Typist = theTypist;
            default -> {
                System.out.println("Cannot seat typist at seat " + seatNumber + " — there is no such seat.");
            }
        }
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     *
     * Note from Ty: "I didn't bother printing the winner at the end,
     * you can probably figure that out yourself."
     */
    public void startRace() throws RulesException
    {
        boolean finished = false;
        boolean isRaceEmpty = true;
        // Reset all typists to the start of the passage
        // (Ty was in a hurry here)
        if(seat1Typist != null){
            seat1Typist.resetToStart();
            isRaceEmpty = false;
        }
        if(seat2Typist != null){
            seat2Typist.resetToStart();
            isRaceEmpty = false;
        }
        if(seat3Typist != null){
            seat3Typist.resetToStart();
            isRaceEmpty = false;
        }

        if(isRaceEmpty){
            throw new RulesException("Race is empty");
        }
        this.steps_since_start = 0;

        while (!finished)
        {
            // Advance each typist by one turn
            advanceTypist(seat1Typist);
            advanceTypist(seat2Typist);
            advanceTypist(seat3Typist);

            // Print the current state of the race
            printRace();

            // Check if any typist has finished the passage
            if ( raceFinishedBy(seat1Typist) || raceFinishedBy(seat2Typist) || raceFinishedBy(seat3Typist) )
            {
                finished = true;
            }

            // Wait 200ms between turns so the animation is visible
            try {
                this.steps_since_start++;
                TimeUnit.MILLISECONDS.sleep(STEP_DURATION_MS);
            } catch (InterruptedException e) {
                System.out.println("Race interrupted!");
            }
        }
        Typist[] typists = {seat1Typist, seat2Typist, seat3Typist};
        for(Typist typist : typists){
            if(raceFinishedBy(typist)){
                System.out.println(typist.getName() + " won!");
            }
        }
        int ms_since_start = getMSSinceStart();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms_since_start);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        seconds -= TimeUnit.MINUTES.toSeconds(minutes);
        System.out.println("The race went on for " + minutes + " minutes and " + seconds + " seconds.");
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

        // Attempt to type a character
        if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
            theTypist.setPostfix("");
        }

        // Mistype check — the probability should reflect the typist's accuracy
        if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
            theTypist.setPostfix("[<]");
        }
        
        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
        if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
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
        return theTypist.getProgress() >= passageLength;
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
    private void printRace()
    {
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();

        printSeat(seat1Typist);
        printSeat(seat2Typist);
        printSeat(seat3Typist);

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [zz] = burnt out    [<] = just mistyped");
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    [zz]              | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * Note: Ty forgot to show when a typist has just mistyped. That would
     * be a nice improvement — perhaps a [<] marker after their symbol.
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

        // Always show the typist's symbol so they can be identified on screen.
        // Append ~ when burnt out so the state is visible without hiding identity.
        System.out.print(theTypist.getSymbol());
        spacesAfter -= theTypist.getPostfix().length();
        System.out.print(theTypist.getPostfix());

        multiplePrint(' ', spacesAfter);
        System.out.print("| ");

        double wpm = 0;
        if(this.steps_since_start != 0){
            double wordsProgress = theTypist.getProgress() / CHARACTERS_IN_WORD;
            double minutesPassed = getMSSinceStart() / 1000.0 / 60.0;
            wpm = wordsProgress / minutesPassed;
        }

        // Print name, wpm and accuracy
        System.out.print(theTypist.getName() + " | " + (int) wpm + " WPM");
        System.out.print(" (Accuracy: " + theTypist.getAccuracy() + ")");
        if (theTypist.isBurntOut()){
            System.out.print(" BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }System.out.println();
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
    private int getMSSinceStart()
    {
        return this.steps_since_start*STEP_DURATION_MS;
    }
}