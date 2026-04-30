import java.util.Objects;

/**
 * A class for creating typists for races.
 * Holds all the relevant information to the typist and is responsible for keeping all fields proper through public methods.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class Typist
{
    private char typistSymbol;
    final String typistName;
    private String specialPostfix;

    private double typistAccuracy;
    private int raceProgress;
    private boolean isBurntOut;
    private int burnOutTurnsLeft;

    // Accuracy thresholds and penalties for mistype and burnout events
    protected double getMistypeBaseChance(){return .3;}
    protected double getBurnoutCapChance(){return .05;}
    protected int getSlideBackAmount(){return 2;}
    protected int getBurnoutDuration(){return 3;}

    /**
     * Constructor for objects of class Typist.
     * Creates a new typist with a given symbol, name, and accuracy rating.
     *
     * @param typistSymbol  a single Unicode character representing this typist (e.g. '①', '②', '③')
     * @param typistName    the name of the typist (e.g. "TURBOFINGERS")
     * @param typistAccuracy the typist's accuracy rating, between 0.0 and 1.0
     */
    public Typist(char typistSymbol, String typistName, double typistAccuracy)
    {
        this.typistSymbol = typistSymbol;
        this.typistName = typistName;
        this.specialPostfix = "";
        this.typistAccuracy = typistAccuracy;
        this.raceProgress = 0;
        this.isBurntOut = false;
        this.burnOutTurnsLeft = 0;
    }


    /**
     * Sets this typist into a burnout state for a given number of turns.
     * A burnt-out typist cannot type until their burnout has worn off.
     * Also adjusts typist's accuracy down 10 percent
     *
     * @param turns the number of turns the burnout will last, must be positive
     */
    public void burnOut(int turns) throws RulesException
    {
        if(turns <= 0) throw new RulesException("burnOut for negative or zero turns", true);
        this.isBurntOut = true;
        this.burnOutTurnsLeft = turns;
        this.setAccuracy(.90*this.typistAccuracy);
    }

    /**
     * Adjust the accuracy after winning the race.
     * The exact formula is new = .8*old + .2
     */
    public void winRace()
    {
        this.setAccuracy(.8*this.typistAccuracy+.2);
    }

    /**
     * Reduces the remaining burnout counter by one turn.
     * When the counter reaches zero, the typist recovers automatically.
     * Has no effect if the typist is not currently burnt out.
     */
    public void recoverFromBurnout()
    {
        if(!this.isBurntOut)return;
        this.burnOutTurnsLeft--;
        if(this.burnOutTurnsLeft <= 0){
            this.isBurntOut = false;
            this.burnOutTurnsLeft = 0;
        }
    }

    /**
     * Returns the typist's accuracy rating.
     *
     * @return accuracy as a double between 0.0 and 1.0
     */
    public double getAccuracy()
    {
        return this.typistAccuracy;
    }

    /**
     * formats accuracy into a string with set digits of precision
     * @param precision how many digits after decimal point
     * @return a formatted string with typist's accuracy
     */
    public String formattedAccuracy(int precision){
        return String.format("%." + precision + "f", this.getAccuracy());
    }

    /**
     * Returns the typist's current progress through the passage.
     * Progress is measured in characters typed correctly so far.
     * Note: this value can decrease if the typist mistypes.
     *
     * @return progress as a non-negative integer
     */
    public int getProgress()
    {
        return this.raceProgress;
    }

    /**
     * Returns the name of the typist.
     *
     * @return the typist's name as a String
     */
    public String getName()
    {
        return this.typistName;
    }

    /**
     * Returns the character symbol used to represent this typist.
     *
     * @return the typist's symbol as a char
     */
    public char getSymbol()
    {
        return this.typistSymbol;
    }

    /**
     * Returns the number of turns of burnout remaining.
     * Returns 0 if the typist is not currently burnt out.
     *
     * @return burnout turns remaining as a non-negative integer
     */
    public int getBurnoutTurnsRemaining()
    {
        return this.burnOutTurnsLeft;
    }

    /**
     * Resets the typist to their initial state, ready for a new race.
     * Progress returns to zero, burnout is cleared entirely.
     */
    public void resetToStart()
    {
        this.raceProgress = 0;
        this.isBurntOut = false;
        this.burnOutTurnsLeft = 0;
        this.specialPostfix = "";
    }

    /**
     * Simulates one turn.
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
     * @param typist the typist to advance
     */
    public void advance() throws RulesException{
        if (this.isBurntOut()){
            // Recovering from burnout — skip this turn
            this.recoverFromBurnout();
            return;
        }
        
        this.setPostfix("");
        // Attempt to type a character
        if (Math.random() < this.getAccuracy()){
            this.typeCharacter();
        }

        // Mistype check
        if (Math.random() < (1 - this.getAccuracy()) * this.getMistypeBaseChance()){
            this.slideBack(this.getSlideBackAmount());
            this.setPostfix("[<]");
        }

        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
        if (Math.random() < this.getBurnoutCapChance() * this.getAccuracy() * this.getAccuracy()){
            this.burnOut(this.getBurnoutDuration());
            this.setPostfix("~");
        }
    }

    /**
     * Returns true if this typist is currently burnt out, false otherwise.
     *
     * @return true if burnt out
     */
    public boolean isBurntOut()
    {
        return this.isBurntOut;
    }

    /**
     * Advances the typist forward by one character along the passage.
     * Should only be called when the typist is not burnt out.
     */
    public void typeCharacter() throws RulesException
    {
        if(this.isBurntOut)throw new RulesException("trying to type character while burned out", true);
        this.raceProgress++;
        this.setAccuracy(.99*this.typistAccuracy+.01);
    }

    /**
     * Moves the typist backwards by a given number of characters (a mistype).
     * Progress cannot go below zero — the typist cannot slide off the start.
     *
     * @param amount the number of characters to slide back (must be positive)
     */
    public void slideBack(int amount) throws RulesException
    {
        if(amount <= 0) throw new RulesException("slideBack amount is not positive", true);
        this.raceProgress -= amount;
        if(this.raceProgress < 0) this.raceProgress = 0;
    }

    /**
     * Sets the accuracy rating of the typist.
     * Values below 0.0 should be set to 0.0; values above 1.0 should be set to 1.0.
     *
     * @param newAccuracy the new accuracy rating
     */
    public void setAccuracy(double newAccuracy)
    {
        this.typistAccuracy = Math.clamp(newAccuracy, 0, 1);
    }

    /**
     * Sets the symbol used to represent this typist.
     *
     * @param newSymbol the new symbol character
     */
    public void setSymbol(char newSymbol)
    {
        this.typistSymbol = newSymbol;
    }

    /**
     * Sets the specialPostfix used to show state of the typist.
     *
     * @param newPostfix the new postfix string
     */
    public void setPostfix(String newPostfix)
    {
        this.specialPostfix = newPostfix;
    }

    /**
     * Returns the string used to represent this typist's state.
     *
     * @return the typist's state representation as a string
     */
    public String getPostfix(){
        return this.specialPostfix;
    }

    /**
     * checks if object's fields arethe same as this typist
     * 
     * @param obj object to compare
     * @return true if typist is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Typist)) return false;
        Typist other = (Typist) obj;
        if(this.typistSymbol != other.typistSymbol) return false;
        if(!this.typistName.equals(other.typistName)) return false;
        if(!this.specialPostfix.equals(other.specialPostfix)) return false;
        if(this.typistAccuracy != other.typistAccuracy) return false;
        if(this.raceProgress != other.raceProgress) return false;
        if(this.isBurntOut != other.isBurntOut) return false;
        return this.burnOutTurnsLeft == other.burnOutTurnsLeft;
    }

    /**
     * @return hash of typist's name
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.typistName);
    }
}
