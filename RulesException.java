/**
 * An exception that is thrown when a method is called improperly according to the rules of the race.
 * Logging and displaying it to the user is handled at the highest method of the stack.
 *
 * @author Adil Akylov
 * @version 1.1
 */
public class RulesException extends Exception{
    public final String message;
    public final boolean fatal;
    /**
     * Constructor for objects of class RulesException.
     * The message should be displayed for debugging when the exception is caught.
     *
     * @param message the reason for the RulesException.
     */
    public RulesException(String message, boolean fatal){
        this.message = message;
        this.fatal = fatal;
    }
}