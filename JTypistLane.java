
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * A class that holds and displays whole lane on one typist.
 * Lane includes their info on the left and passage with displayed progress.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class JTypistLane extends JPanel{
    public final SwingTypist typist;
    private JTextPane track;
    private Style writtenStyle;
    private Style unwrittenStyle;
    private int lastProgress;
    private JTypistInfo info;
    private JLabel WPM;

    /**
     * Constructor for objects of class JTypistLane.
     * Creates a JPanel.
     * 
     * @param typist typist whose lane it will be
     */
    public JTypistLane(SwingTypist typist){
        super();
        this.typist = typist;
        this.lastProgress = 0;
    }

    /**
     * places into JPanel info about the typist and the track with the passage
     *
     * @param passage passage that typist will compete with
     * @return JPanel containing the whole lane
     */
    public JPanel buildLane(String passage){
        this.info = new JTypistInfo(this.typist);
        this.add(this.info);
        this.add(this.buildTrack(passage));
        this.WPM = new JLabel();
        Utils.setStableSize(this.WPM, 100, 30);
        this.add(this.WPM);
        return this;
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

    /**
     * updates all info about the typist and their progress
     */
    public void update(int wpm){
        this.info.update();
        this.updateTrackProgress();
        this.WPM.setText("Current WPM: " + wpm);
    }

    /**
     * updates the progress of typist to appear in the track with color and underlining.
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