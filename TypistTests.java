import java.util.Random;

public class TypistTests {
    final static Random rand = new Random();
    public static void main(String[] args)
    {
        printSeparator();
        if(!testSlideBack())failed();
        printSeparator();
        if(!testRecoverFromBurnout())failed();
        printSeparator();
        if(!testResetToStart())failed();
        printSeparator();
        if(!testSetAccuracy())failed();
        printSeparator();
        if(!testTypeCharacter())failed();
        printSeparator();
    }
    private static void failed()
    {
        System.out.println("+++++++++++++++++++++++++++++++\nTest Failed!!");
    }
    private static void printSeparator()
    {
        System.out.println("\n===============================\n");
    }
    private static void printTestTitle(String title)
    {
        System.out.println(title + "\n-------------------------------");
    }
    private static Typist getTypist()
    {
        return new Typist('1', "TestName", 1);
    }

    static void advanceTypist(Typist t, int characters)
    {
        for(int curProgress=0;curProgress<characters;curProgress++){
            t.typeCharacter();
        }
    }

    static boolean testSlideBack()
    {
        printTestTitle("Testing slideBack not making progress negative");
        Typist t = getTypist();
        int maxProgress = rand.nextInt(10);
        int amount;
        advanceTypist(t, maxProgress);
        System.out.println("Current progress is " + t.getProgress());
        while(t.getProgress() != 0){
            if(t.getProgress() < 0)return false;
            amount = rand.nextInt(1, 3);
            System.out.println("Attepting slideBack " + amount + " characters");
            t.slideBack(amount);
            System.out.println("Resulting progress: " + t.getProgress());
        }amount = rand.nextInt(5);
        System.out.println("Attepting slideBack " + amount + " characters");
        t.slideBack(amount);
        System.out.println("Resulting progress: " + t.getProgress());
        return t.getProgress() == 0;
    }
    static boolean testRecoverFromBurnout()
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
    static boolean testResetToStart()
    {
        printTestTitle("Testing resetToStart correctly clearing progress and burnout");
        Typist t = getTypist();
        int progress = rand.nextInt(10);
        boolean isBurnout = rand.nextBoolean();
        int burnout = rand.nextInt(1, 5);
        advanceTypist(t, progress);
        if(isBurnout)t.burnOut(burnout);
        System.out.println("Currect progress is " + t.getProgress() + ", and current burnout is " + t.getBurnoutTurnsRemaining());
        System.out.println("Attempting reset to start");
        t.resetToStart();
        System.out.println("Currect progress is " + t.getProgress() + ", and current burnout is " + t.getBurnoutTurnsRemaining());
        return t.getProgress() == 0 && !t.isBurntOut() && t.getBurnoutTurnsRemaining() == 0;
    }
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
        return accuracy == t.getAccuracy();
    }
    static boolean testTypeCharacter()
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