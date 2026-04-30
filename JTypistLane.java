
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class JTypistLane extends JPanel{
    private final SwingTypist typist;
    private JTextPane track;
    private Style writtenStyle;
    private Style unwrittenStyle;
    private int lastProgress;
    private JLabel accuracy;
    public JTypistLane(SwingTypist typist){
        this.typist = typist;
        this.lastProgress = 0;
    }
    public JPanel buildLane(String passage){
        this.add(this.buildInfo());
        this.add(this.buildTrack(passage));
        return this;
    }
    public JPanel buildInfo(){
        JPanel infoPanel = new JPanel();
        JLabel typistLabel = new JLabel(this.typist.getName());
        this.accuracy = new JLabel();
        infoPanel.add(typistLabel);
        infoPanel.add(this.accuracy);
        return infoPanel;
    }

    /**
     * creates a JTextPane object that has appropriate styling for a typing race track
     *
     * @return empty JTextPane with track styling
     */
    public JTextPane buildTrack(String passage){
        this.track = new JTextPane();
        this.track.setFont(new Font("Monospace", 1, 15));
        this.track.setMaximumSize(new Dimension(500, 60));
        this.track.setEditable(false);
        this.writtenStyle = this.track.addStyle("written", null);
        this.unwrittenStyle = this.track.addStyle("unwritten", null);
        StyleConstants.setForeground(this.writtenStyle, this.typist.getColor());
        StyleConstants.setUnderline(this.writtenStyle, true);
        this.track.setText(Utils.breakWords(passage, 60));
        return this.track;
    }

    public void update(){
        this.updateAccuracy();
        this.updateTrackProgress();
    }

    /**
     * renders current accuracy to the typist info with the given index
     *
     * @param i index of a typist whose accuracy to update
     */
    private void updateAccuracy(){
        this.accuracy.setText("Accuracy: " + (int)(100*this.typist.getAccuracy()) + "%");
    }

    /**
     * updates the progress of typist to appear in the track with color and underlining.
     *
     * @param i index of a typist whose track to update
     */
    private void updateTrackProgress(){
        int progress = Math.min(this.track.getText().length(), this.typist.getProgress());
        if(this.lastProgress == progress)return;
        StyledDocument doc = this.track.getStyledDocument();
        int startOfChange = Math.min(progress, this.lastProgress);
        int lengthToChange = Math.abs(progress - this.lastProgress);
        Style styleToChange = this.lastProgress < progress ? this.writtenStyle : this.unwrittenStyle;
        doc.setCharacterAttributes(startOfChange, lengthToChange, styleToChange, true);
        this.lastProgress = progress;
    }
}