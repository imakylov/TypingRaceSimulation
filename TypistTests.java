import java.util.Random;

/**
 * An entry point to all tests for Typist class.
 * Tests public methods for keeping all fields proper and displays the test failed if the fields are improper.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class TypistTests {
    final static Random rand = new Random();
    public static void main(String[] args)
    {
        printSeparator();
        try {
            if(!testSlideBack())failed();
            printSeparator();
            if(!testRecoverFromBurnout())failed();
            printSeparator();
            if(!testResetToStart())failed();
            printSeparator();
            if(!testSetAccuracy())failed();
            printSeparator();
            if(!testTypeCharacter())failed();
        } catch (RulesException e) {
            System.out.println("Rules Exception!");
            System.out.println(e.message);
            return;
        }
        printSeparator();
        System.out.println("All Tests Passed!!");
    }

    /**
     * Print message about a failed test
     */
    private static void failed()
    {
        System.out.println("+++++++++++++++++++++++++++++++\nTest Failed!!");
    }

    /**
     * Print a line to separate tests
     */
    private static void printSeparator()
    {
        System.out.println("\n===============================\n");
    }

    /**
     * Print a title for the test with a small separator for emphasis
     *
     * @param title title of the test that needs to be printed
     */
    private static void printTestTitle(String title)
    {
        System.out.println(title + "\n-------------------------------");
    }

    /**
     * Get a sample typist for testing.
     * @return an object of class Typist with name TestName, symbol 1 and accuracy 1
     */
    private static Typist getTypist()
    {
        return new Typist('1', "TestName", 1);
    }

    /**
     * Advance typist some characters for testing.
     *
     * @param t the typist to be advanced
     * @param characters the amount of character to advance
     * @throws RulesException RulesException is thrown if the typist is burned out.
     */
    static void advanceTypist(Typist t, int characters) throws RulesException
    {
        for(int curProgress=0;curProgress<characters;curProgress++){
            t.typeCharacter();
        }
    }

    /**
     * Test if slideBack method works properly.
     * Advances a typist a random amount and slides them back until they are at the start and then tries sliding back again. If the progress is negative at any point returns false
     *
     * @return true if no problems were encountered during slideBack tests
     * @throws RulesException throws an exception if there are problems
     */
    static boolean testSlideBack() throws RulesException
    {
        printTestTitle("Testing slideBack not making progress negative");
        Typist t = getTypist();
        int maxProgress = rand.nextInt(1, 10);
        int amount;
        advanceTypist(t, maxProgress);
        System.out.println("Current progress is " + t.getProgress());
        while(t.getProgress() != 0){
            if(t.getProgress() < 0)return false;
            amount = rand.nextInt(1, 3);
            System.out.println("Attepting slideBack " + amount + " characters");
            t.slideBack(amount);
            System.out.println("Resulting progress: " + t.getProgress());
        }amount = rand.nextInt(1, 5);
        System.out.println("Attepting slideBack " + amount + " characters");
        t.slideBack(amount);
        System.out.println("Resulting progress: " + t.getProgress());
        return t.getProgress() == 0;
    }

    /**
     * Test if recoverFromBurnout method works properly.
     * Burns out a typist and recovers them until they are not burned out. If they are fully recovered before they should be or if the getBurnoutTurnsRemaining is negative returns false.
     *
     * @return true if no problems were encountered during recoverFromBurnout tests
     * @throws RulesException throws an exception if there are other problems
     */
    static boolean testRecoverFromBurnout() throws RulesException
    {
        printTestTitle("Testing recoverFromBurnout correctly clearing burnout");
        Typist t = getTypist();
        int maxBurnout = rand.nextInt(1, 5);
        t.burnOut(maxBurnout);
        System.out.println("Player is" + (t.isBurntOut() ? "" : " not") +  " in burnout with " + t.getBurnoutTurnsRemaining() + " turns left");
        for(int turnsLeft=maxBurnout;turnsLeft>0;turnsLeft--){
            if(!t.isBurntOut())return false;
            if(t.getBurnoutTurnsRemaining() <= 0)return false;
            System.out.println("Attempting recovery from burnout");
            t.recoverFromBurnout();
            System.out.println("Player is" + (t.isBurntOut() ? "" : " not") +  " in burnout with " + t.getBurnoutTurnsRemaining() + " turns left");
        }return !t.isBurntOut() && t.getBurnoutTurnsRemaining() == 0;
    }

    /**
     * Test if resetToStart method works properly.
     * Puts typist in a random state, resets them and checks if all the fields are cleared.
     *
     * @return true if resetToStart correctly cleared and reset all fields
     * @throws RulesException throws an exception if there are other problems
     */
    static boolean testResetToStart() throws RulesException
    {
        printTestTitle("Testing resetToStart correctly clearing progress and burnout");
        Typist t = getTypist();
        int progress = rand.nextInt(10);
        boolean isBurnout = rand.nextBoolean();
        boolean didMistype = rand.nextBoolean();
        int burnout = rand.nextInt(1, 5);
        advanceTypist(t, progress);
        if(didMistype){
            t.setPostfix("[<]");
        }else if(isBurnout){
            t.burnOut(burnout);
            t.setPostfix("~");
        }
        System.out.println("Currect progress is " + t.getProgress() + ", and current burnout is " + t.getBurnoutTurnsRemaining() + " (" + t.getPostfix() + ")");
        System.out.println("Attempting reset to start");
        t.resetToStart();
        System.out.println("Currect progress is " + t.getProgress() + ", and current burnout is " + t.getBurnoutTurnsRemaining() + " (" + t.getPostfix() + ")");
        return t.getProgress() == 0 && !t.isBurntOut() && t.getBurnoutTurnsRemaining() == 0 && t.getPostfix().length() == 0;
    }

    /**
     * Test if setAccuracy method works properly.
     * tries to set accuracy to a random value until the recorded accuracy is not clamped.
     * If the accuracy is clamped incorrectly, when not needed or not clamped returns false.
     *
     * @return true if accuracy is clamped correctly.
     */
    static boolean testSetAccuracy()
    {
        printTestTitle("Testing setAccuracy correctly clamps values between 0 and 1");
        Typist t = getTypist();
        double accuracy = rand.nextDouble(-2, 2);
        System.out.println("Setting accuracy to " + accuracy);
        t.setAccuracy(accuracy);
        while(t.getAccuracy() == 1 || t.getAccuracy() == 0){
            System.out.println("Current accuracy is " + t.getAccuracy());
            if(0 < accuracy && accuracy < 1)return false;
            if((accuracy < 0 && t.getAccuracy() != 0) || (accuracy > 1 && t.getAccuracy() != 1))return false;
            accuracy = rand.nextDouble(-2, 2);
            System.out.println("Setting accuracy to " + accuracy);
            t.setAccuracy(accuracy);
        }System.out.println("Current accuracy is " + t.getAccuracy());
        return accuracy == t.getAccuracy() && (0 < accuracy && accuracy < 1);
    }

    /**
     * Test if typeCharacter method works properly.
     * Calls typeCharacter a random amount of times and checks if the resulting progress is equal to the amount.
     *
     * @return true if typeCharacter correctly advances progress
     * @throws RulesException throws an exception if there are other problems
     */
    static boolean testTypeCharacter() throws RulesException
    {
        printTestTitle("Testing typeCharacter correctly advances progress");
        Typist t = getTypist();
        int maxProgress = rand.nextInt(10);
        System.out.println("Current progress is " + t.getProgress());
        for(int curProgress=0;curProgress<maxProgress;curProgress++){
            if(t.getProgress() != curProgress)return false;
            System.out.println("Typing a character");
            t.typeCharacter();
            System.out.println("Current progress is " + t.getProgress());
        }return t.getProgress() == maxProgress;
    }
}