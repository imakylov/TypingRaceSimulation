import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.text.*;

/**
 * An extension of TypingRace that only holds SwingTypist typists. A maximum of 6 typists race using the same logic as in TypingRace,
 * but with a swing interface.
 *
 * @author Adil Akylov
 * @version 1.1
 */
public class SwingTypingRace extends TypingRace<SwingTypist> {
    private final String passage;
    private final JFrame frame;
    private JTextPane[] tracks;
    private Style[] writtenStyles;
    private Style[] unwrittenStyles;
    private int[] lastProgresses;
    private JLabel[] accuracies;

    private final Color[] COLORS = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.pink};
    private final int FRAME_WIDTH = 640+320;
    private final int FRAME_HEIGHT = 360+180;
    @Override
    public int getMaxTypists(){return 6;}

    /**
     * constructor of class SwingTypingRace. prepares the fields and automatically builds the race layout.
     *
     * @param passage the passage that the typists will compete for
     */
    public SwingTypingRace(String passage){
        if(passage == null)passage = "";
        super(passage.length());
        this.passage = passage;
        this.frame = new JFrame("Typing Race Simulation");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    private void prepareFields(){
        this.tracks = new JTextPane[this.getSeatsTaken()];
        this.accuracies = new JLabel[this.getSeatsTaken()];
        this.lastProgresses = new int[this.getSeatsTaken()];
        this.writtenStyles = new Style[this.getSeatsTaken()];
        this.unwrittenStyles = new Style[this.getSeatsTaken()];
        Arrays.fill(this.lastProgresses, 0);
    }

    /**
     * builds the race layout including all tracks and typist infos
     */
    private void buildLayout(){
        this.prepareFields();
        JPanel tracksPanel = new JPanel(new FlowLayout());
        tracksPanel.setSize(this.frame.getSize());
        for(int i=0;i<this.getSeatsTaken();i++){
            tracksPanel.add(this.buildTypistLane(i));
        }this.printRace();
        this.frame.add(tracksPanel);
        this.frame.setVisible(true);
    }
    
    private JPanel buildTypistLane(int i){
        JPanel typistPanel = new JPanel();
        typistPanel.add(this.buildTypistInfoPanel(i));
        typistPanel.add(buildTrack(i));
        return typistPanel;
    }

    private JPanel buildTypistInfoPanel(int i){
        JPanel infoPanel = new JPanel();
        JLabel typistLabel = new JLabel(this.typists.get(i).getName());
        this.accuracies[i] = new JLabel();
        infoPanel.add(typistLabel);
        infoPanel.add(this.accuracies[i]);
        return infoPanel;
    }

    /**
     * creates a JTextPane object that has appropriate styling for a typing race track
     *
     * @return empty JTextPane with track styling
     */
    private JTextPane buildTrack(int i){
        this.tracks[i] = new JTextPane();
        this.tracks[i].setFont(new Font("Monospace", 1, 15));
        this.tracks[i].setMaximumSize(new Dimension(500, 60));
        this.tracks[i].setEditable(false);
        this.writtenStyles[i] = this.tracks[i].addStyle("written", null);
        this.unwrittenStyles[i] = this.tracks[i].addStyle("unwritten", null);
        StyleConstants.setForeground(this.writtenStyles[i], this.COLORS[i]);
        StyleConstants.setUnderline(this.writtenStyles[i], true);
        return this.tracks[i];
    }

    /**
     * renders the current state of the race on tracks and user infos.
     */
    @Override
    public void printRace(){
        for(int i=0;i<this.getSeatsTaken();i++){
            this.renderTrackProgress(i);
            this.renderAccuracy(i);
        }
    }
    
    /**
     * renders current accuracy to the typist info with the given index
     *
     * @param i index of a typist whose accuracy to update
     */
    private void renderAccuracy(int i){
        this.accuracies[i].setText("Accuracy: " + (int)(100*this.typists.get(i).getAccuracy()) + "%");
    }

    /**
     * updates the progress of typist to appear in the track with color and underlining.
     *
     * @param i index of a typist whose track to update
     */
    private void renderTrackProgress(int i){
        int progress = Math.min(this.passageLength, this.typists.get(i).getProgress());
        if(this.lastProgresses[i] == progress)return;
        this.lastProgresses[i] = progress;
        this.tracks[i].setText("");
        String displayText = Utils.breakWords(this.passage, 60);
        StyledDocument doc = this.tracks[i].getStyledDocument();
        String written = displayText.substring(0, progress);
        String unwritten = displayText.substring(progress);
        try{
            doc.insertString(doc.getLength(), written, this.writtenStyles[i]);
            doc.insertString(doc.getLength(), unwritten, this.unwrittenStyles[i]);
        }catch (BadLocationException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Rudimentary entry point for swing typing races. quick starts a sample race.
     */
    public static void main(String[] args) throws RulesException{
        String passage;
        passage = "The last man of Earth sat alone in a room. There was a knock on the door";
        passage = "In the rigor which is space, this spacesuit was designed by engineers to maintain your life in space, and can be called the smallest spaceship";
        final SwingTypingRace race = new SwingTypingRace(passage);
        race.addTypist(new SwingTypist("TURBOFINGERS", 0.85));
        race.addTypist(new SwingTypist("QWERTY_QUEEN",  0.60));
        race.addTypist(new SwingTypist("HUNT_N_PECK",   0.30));
        race.buildLayout();
        race.startRace();
    }
}
