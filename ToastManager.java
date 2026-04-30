import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A singleton class that manages all Toast messages. Toasts appear on Pop up layer of JLayeredPane.
 * Has to be supplied frame with ToastManager.init and then singleton can be accessed with ToastManager.get
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class ToastManager {
    private static ToastManager singleton = null;
    protected final JLayeredPane root;
    protected final ArrayList<Toast> toasts = new ArrayList<>();
    protected final Timer timer;

    // Constants, can be tweaked
    private final int GAP = 10;
    private int START_HEIGHT(){return this.root.getHeight();}
    private int BOTTOM(){return this.START_HEIGHT() - Toast.SET_HEIGHT - GAP;}
    private int X(){return root.getWidth() - Toast.SET_WIDTH - GAP;}

    /**
     * Constructor for objects of class ToastManager. Starts timer to update toasts each frame.
     * Should only be called once from ToastManager.init.
     *
     * @param root JLayeredPane of JFrame
     */
    private ToastManager(JLayeredPane root){
        this.root = root;
        this.timer = new Timer(1000 / Constants.FPS, e -> updateToasts());
        this.timer.start();
    }

    /**
     * @return singleton instance of ToastManager
     */
    public static ToastManager get(){
        return ToastManager.singleton;
    }

    /**
     * @param frame frame where to place toasts on JLayeredPane
     * @return singleton instance of ToastManager
     */
    public static ToastManager init(JFrame frame){
        if(ToastManager.singleton != null){
            throw new RuntimeException ("trying to init Toast Manager second time");
        }ToastManager.singleton = new ToastManager(frame.getLayeredPane());
        return ToastManager.singleton;
    } 

    /**
     * Updates animation and removes all inactive toasts.
     */
    protected void updateToasts(){
        removeFinished();
        for(int i=0;i<this.toasts.size();i++){
            Toast toast = this.toasts.get(i);
            toast.lerpToY(this.getYOfToast(i) + toast.getOffset());
            toast.updateOpacity();
        }
    }

    /**
     * Gets offset of a specific toast
     *
     * @param i index of the toast starting from bottom
     * @return target Y based on the index of the toast
     */
    private int getYOfToast(int i){
        return this.BOTTOM() - i * (Toast.SET_HEIGHT + GAP);
    }

    /**
     * Removes all toasts that finished their dissapearing animation and repaints JLayeredPane.
     */
    private void removeFinished(){
        ArrayList<Toast> toRemove = new ArrayList<>();
        for(Toast toast : this.toasts){
            if(toast.isFinished()){
                toRemove.add(toast);
            }
        }for(Toast toast : toRemove){
            this.toasts.remove(toast);
            this.root.remove(toast);
        }if(!toRemove.isEmpty()) this.root.repaint();
    }

    /**
     * Add a toast to messages queue
     * @param toast toast to add
     */
    public void push(Toast toast){
        toast.setLocation(this.X(), this.START_HEIGHT());
        this.root.add(toast, JLayeredPane.POPUP_LAYER);
        this.toasts.addFirst(toast);
    }
}
