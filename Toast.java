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
class Toast extends JPanel {
    private double opacity = 0f;
    public final long creationTime;
    
    // Constants can be tweaked
    static final double LERP_COEF = .1;
    static final int SET_WIDTH = 200;
    static final int SET_HEIGHT = 100;
    static final int MS_TO_APPEAR = 500;
    static final int MS_TO_START_DISAPPEARING = 2000;
    static final int DISAPPEARING_HEIGHT = 50;
    static final int MS_TO_DISAPPEAR = 500;

    /**
     * Constructor for objects of class Toast.
     * Creates a new toast notification with a given message and configures its look.
     *
     * @param message text that should be displayed in the toast
     */
    public Toast(String message) {
        this.creationTime = System.currentTimeMillis();
        this.setSize(SET_WIDTH, SET_HEIGHT);
        this.setOpaque(false);
        this.setLayout(new BorderLayout());

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
        double disappearingProgress = (double)(now - this.creationTime - MS_TO_START_DISAPPEARING) / MS_TO_DISAPPEAR;
        if(disappearingProgress < 0)return 0;
        return -(int)(disappearingProgress * DISAPPEARING_HEIGHT);
    }

    /**
     * @return true if toast has fully disappeared
     */
    public boolean isFinished(){
        return Utils.isPast(this.creationTime + MS_TO_START_DISAPPEARING + MS_TO_DISAPPEAR);
    }

    /**
     * Updates opacity for appearing and disappearing animtions
     */
    public void updateOpacity(){
        if(this.isFinished()){
            this.setOpacity(0);
        }else if(Utils.isPast(this.creationTime + MS_TO_START_DISAPPEARING)){
            this.setOpacity(1 - (double)(Utils.now() - this.creationTime - MS_TO_START_DISAPPEARING) / MS_TO_DISAPPEAR);
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
     * @return background color of toast. Will be overriden for subclasses
     */
    protected Color bg(){
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

        g2.setColor(this.bg());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        g2.dispose();
        super.paintComponent(g);
    }
}

/**
 * A class to represent Bad Toast messages.
 *
 * @author Adil Akylov
 * @version 1.2
 */
class BadToast extends Toast {
    public BadToast(String message) {
        super(message);
    }
    protected Color bg(){
        return new Color(255, 100, 100);
    }
}

/**
 * A class to represent Good Toast messages.
 *
 * @author Adil Akylov
 * @version 1.2
 */
class GoodToast extends Toast {
    public GoodToast(String message) {
        super(message);
    }
    protected Color bg(){
        return new Color(50, 200, 100);
    }
}

/**
 * A class to represent System Toast messages.
 *
 * @author Adil Akylov
 * @version 1.2
 */
class SystemToast extends Toast {
    public SystemToast(String message) {
        super(message);
    }
    protected Color bg(){
        return new Color(240, 240, 100);
    }
}