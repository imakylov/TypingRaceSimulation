import java.awt.*;
import javax.swing.*;

/**
 * A class that represent a toast notification. Has to be used in ToastManager.
 * Position is handled by the container, offset for appearing and disappearing animation is given by getOffset.
 * Opacity is handled by Toast with updateOpacity.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class Toast extends JPanel {
    private double opacity = 0f;
    public final long creationTime;
    private Color bg; 
    private int type = 0;
    private final String message;
    
    // Constants can be tweaked
    static final double LERP_COEF = .1;
    static final int SET_WIDTH = 200;
    static final int SET_HEIGHT = 100;
    static final int MS_TO_APPEAR = 500;
    static final double SEC_TO_START_DISAPPEARING = 6;
    static final int DISAPPEARING_HEIGHT = 50;
    static final int MS_TO_DISAPPEAR = 500;

    /**
     * Constructor for objects of class Toast.
     * Creates a new toast notification with a given message and configures its look.
     *
     * @param message text that should be displayed in the toast
     */
    public Toast(String message) {
        super(new BorderLayout());
        this.creationTime = System.currentTimeMillis();
        this.bg = null;
        this.message = message;
        this.setSize(SET_WIDTH, SET_HEIGHT);
        this.setOpaque(false);

        this.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        this.add(Utils.getSeemlessTextArea(message));
    }

    /**
     * Calculates Y offset of toast for dissappearing animation. If the toast is yet to disappear returns 0
     * all other animation are handled with lerpToY automatically.
     *
     * @return y offset caused by disappearing animation
     */
    public int getOffset(){
        long now = System.currentTimeMillis();
        double disappearingProgress = (now - this.creationTime - 1000 * SEC_TO_START_DISAPPEARING) / MS_TO_DISAPPEAR;
        if(disappearingProgress < 0)return 0;
        return -(int)(disappearingProgress * DISAPPEARING_HEIGHT);
    }

    /**
     * @return true if toast has fully disappeared
     */
    public boolean isFinished(){
        return Utils.isPast(this.creationTime + (int)(1000 * SEC_TO_START_DISAPPEARING) + MS_TO_DISAPPEAR);
    }

    /**
     * Updates opacity for appearing and disappearing animtions
     */
    public void updateOpacity(){
        if(this.isFinished()){
            this.setOpacity(0);
        }else if(Utils.isPast(this.creationTime + (int)(1000 * SEC_TO_START_DISAPPEARING))){
            this.setOpacity(1 - (Utils.now() - this.creationTime - 1000 * SEC_TO_START_DISAPPEARING) / MS_TO_DISAPPEAR);
        }else if(Utils.isPast(this.creationTime + MS_TO_APPEAR)){
            this.setOpacity(1);
        }else{
            this.setOpacity((double)(Utils.now() - this.creationTime) / MS_TO_APPEAR);
        }
    }

    /**
     * Sets toast's opacity to value given and repaints toast. If opacity is the same does nothing
     * @param opacity opacity to set
     */
    protected void setOpacity(double opacity) {
        if(opacity == this.opacity)return;
        this.opacity = opacity;
        this.repaint();
    }

    /**
     * @return message of this toast
     */
    public String getMessage(){
        return this.message;
    }

    /**
     * @param type numerical representation of type, eg 1 = bad, 2 = good
     */
    protected void setType(int type){
        this.type = type;
    }

    /**
     * @return type of the toast represented as an int, eg 1 = bad, 2 = good
     */
    public int getType(){
        return this.type;
    }

    /**
     * @param color color to set background. Should only be called at creation
     */
    protected void setBG(Color color){
        this.bg = color;
    }

    /**
     * @return background color of toast. can only be set at creation with Toast.good, bad, etc.
     */
    protected Color getBG(){
        if(this.bg != null) return this.bg;
        return new Color(80, 150, 220);
    }

    /**
     * Moves toast to given y position 10%. When called each frame creates smooth ease in animation
     * @param y target y level
     */
    public void lerpToY(int y){
        this.setLocation(this.getX(), (int) (LERP_COEF * y + (1-LERP_COEF) * this.getY()));
    }

    /**
     * Creates background for toast message. Has to be done this way for float opacity support.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, (float)this.opacity));

        g2.setColor(this.getBG());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        g2.dispose();
        super.paintComponent(g);
    }

    /**
     * create a Toast object from a log record
     *
     * @param log string representing toast
     * @return toast object with the same text and correct type
     */
    public static Toast fromLog(String log){
        int type = log.charAt(0) - '0';
        String message = log.substring(1);
        return switch (type) {
            case 1 -> Toast.good(message);
            case 2 -> Toast.bad(message);
            case 3 -> Toast.system(message);
            default -> new Toast(message);
        };
    }

    /**
     * @param message text that should be displayed in the toast
     * @return toast with with background of GOOD_COLOR
     */
    public static Toast good(String message){
        Toast toast = new Toast(message);
        toast.setType(1);
        toast.setBG(Constants.GOOD_COLOR);
        return toast;
    }

    /**
     * @param message text that should be displayed in the toast
     * @return toast with with background of BAD_COLOR
     */
    public static Toast bad(String message){
        Toast toast = new Toast(message);
        toast.setType(2);
        toast.setBG(Constants.BAD_COLOR);
        return toast;
    }

    /**
     * @param message text that should be displayed in the toast
     * @return toast with with background of SYSTEM_COLOR
     */
    public static Toast system(String message){
        Toast toast = new Toast(message);
        toast.setType(3);
        toast.setBG(Constants.SYSTEM_COLOR);
        return toast;
    }
}