import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.*;

public class ToastManager {
    protected final JLayeredPane root;
    protected final ArrayList<Toast> toasts = new ArrayList<>();
    protected final Timer timer;

    private final int GAP = 10;
    private int START_HEIGHT(){return this.root.getHeight();}
    private int BOTTOM(){return this.START_HEIGHT() - Toast.SET_HEIGHT - GAP;}
    private int X(){return root.getWidth() - Toast.SET_WIDTH - GAP;}

    public ToastManager(JLayeredPane root){
        this.root = root;
        this.timer = new Timer(16, e -> updateToasts());
        this.timer.start();
    }

    protected void updateToasts(){
        removeFinished();
        for(int i=0;i<this.toasts.size();i++){
            Toast toast = this.toasts.get(i);
            toast.lerpToY(this.getOffset(i) + toast.getOffset());
            toast.updateOpacity();
        }
    }

    private int getOffset(int i){
        return this.BOTTOM() - i * (Toast.SET_HEIGHT + GAP);
    }

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

    public void push(Toast toast){
        toast.setBounds(this.X(), this.START_HEIGHT());
        this.root.add(toast, JLayeredPane.POPUP_LAYER);
        this.toasts.addFirst(toast);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Toast Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new FlowLayout());

        ToastManager manager = new ToastManager(frame.getLayeredPane());
        
        JButton btn = new JButton("Show message");
        btn.addActionListener(e -> manager.push(new Toast("Message")));

        frame.add(btn);
        frame.setVisible(true);
    }
}
