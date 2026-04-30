import java.awt.*;
import javax.swing.*;

class Toast extends JPanel {
    private float opacity = 0f;
    static final double LERP_COEF = .1;
    public final long creationTime;

    static final int SET_WIDTH = 100;
    static final int SET_HEIGHT = 50;
    static final int MS_TO_APPEAR = 500;
    static final int MS_TO_START_DISAPPEARING = 2000;
    static final int DISAPPEARING_HEIGHT = 50;
    static final int MS_TO_DISAPPEAR = 500;

    public Toast(String message) {
        this.creationTime = System.currentTimeMillis();
        this.setSize(SET_WIDTH, SET_HEIGHT);
        this.setOpaque(false);
        this.setLayout(new BorderLayout());

        JLabel label = new JLabel(message);
        label.setForeground(this.fg());

        this.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        this.add(label, BorderLayout.CENTER);
    }

    public void setBounds(int x, int y){
        this.setBounds(x, y, SET_WIDTH, SET_HEIGHT);
    }

    public int getOffset(){
        long now = System.currentTimeMillis();
        double disappearingProgress = (double)(now - this.creationTime - MS_TO_START_DISAPPEARING) / MS_TO_DISAPPEAR;
        if(disappearingProgress < 0)return 0;
        return -(int)(disappearingProgress * DISAPPEARING_HEIGHT);
    }

    public boolean isFinished(){
        return Utils.isPast(this.creationTime + MS_TO_START_DISAPPEARING + MS_TO_DISAPPEAR);
    }

    public void updateOpacity(){
        if(this.isFinished()){
            this.setOpacity(0);
        }else if(Utils.isPast(this.creationTime + MS_TO_START_DISAPPEARING)){
            this.setOpacity(1f - (float)(Utils.now() - this.creationTime - MS_TO_START_DISAPPEARING) / MS_TO_DISAPPEAR);
        }else if(Utils.isPast(this.creationTime + MS_TO_APPEAR)){
            this.setOpacity(1);
        }else{
            this.setOpacity((float)(Utils.now() - this.creationTime) / MS_TO_APPEAR);
        }
    }

    protected void setOpacity(float opacity) {
        if(opacity == this.opacity)return;
        this.opacity = opacity;
        this.repaint();
    }

    protected Color bg(){
        return new Color(50, 50, 50);
    }
    protected Color fg(){
        return Color.WHITE;
    }

    public void lerpToY(int y){
        this.setLocation(this.getX(), (int) (LERP_COEF * y + (1-LERP_COEF) * this.getY()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, this.opacity));

        g2.setColor(this.bg());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        g2.dispose();
        super.paintComponent(g);
    }
}