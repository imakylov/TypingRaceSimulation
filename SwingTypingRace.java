import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.*;

public class SwingTypingRace extends TypingRace<SwingTypist> {
    private final String passage;
    private final JFrame frame;
    private JTextPane[] tracks;
    private JLabel[] accuracies;

    private final Color[] COLORS = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.pink};
    private final int FRAME_WIDTH = 640+320;
    private final int FRAME_HEIGHT = 360+180;
    @Override
    public int getMaxTypists(){return 6;}

    public SwingTypingRace(String passage, ArrayList<SwingTypist> typists){
        if(passage == null)passage = "";
        super(passage.length());
        this.passage = passage;
        this.typists = typists;
        this.frame = new JFrame("Typing Race Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.buildLayout();
        frame.setVisible(true);
    }

    @Override
    public void addTypist(SwingTypist typist){
        try{
            super.addTypist(typist);
        }catch (RulesException e){
            this.printSystemMessage(e.message);
        }
    }

    private void buildLayout(){
        JPanel tracksPanel = new JPanel(new FlowLayout());
        tracksPanel.setSize(this.frame.getSize());
        this.tracks = new JTextPane[this.getSeatsTaken()];
        this.accuracies = new JLabel[this.getSeatsTaken()];
        for(int i=0;i<this.getSeatsTaken();i++){
            JPanel typistPanel = new JPanel();
            JPanel infoPanel = new JPanel();
            JLabel typistLabel = new JLabel(this.typists.get(i).getName());
            this.accuracies[i] = new JLabel();
            infoPanel.add(typistLabel);
            infoPanel.add(this.accuracies[i]);
            typistLabel.setSize(100, 60);
            this.tracks[i] = buildTrack();
            typistPanel.add(infoPanel);
            typistPanel.add(this.tracks[i]);
            tracksPanel.add(typistPanel);
        }this.printRace();
        this.frame.add(tracksPanel);
    }
    
    private static JTextPane buildTrack(){
        JTextPane track = new JTextPane();
        track.setFont(new Font("Monospace", 1, 15));
        track.setMaximumSize(new Dimension(500, 60));
        track.setEditable(false);
        return track;
    }

    @Override
    public void printRace(){
        for(int i=0;i<this.getSeatsTaken();i++){
            this.renderTrackProgress(i);
            this.renderAccuracy(i);
        }
    }
    
    private void renderAccuracy(int i){
        this.accuracies[i].setText("Accuracy: " + (int)(100*this.typists.get(i).getAccuracy()) + "%");
    }
    private void renderTrackProgress(int i){
        this.tracks[i].setText("");
        String displayText = Utils.breakWords(this.passage, 60);
        Style written = this.tracks[i].addStyle("written", null);
        Style unwritten = this.tracks[i].addStyle("unwritten", null);
        StyleConstants.setForeground(written, this.COLORS[i]);
        StyleConstants.setUnderline(written, true);
        StyledDocument doc = this.tracks[i].getStyledDocument();
        int progress = Math.min(this.passageLength, this.typists.get(i).getProgress());
        try{
            doc.insertString(doc.getLength(), displayText.substring(0, progress), written);
            doc.insertString(doc.getLength(), displayText.substring(progress), unwritten);
        }catch (BadLocationException e){
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        final ArrayList<SwingTypist> typists = new ArrayList<>();
        typists.add(new SwingTypist("TURBOFINGERS", 0.85));
        typists.add(new SwingTypist("QWERTY_QUEEN",  0.60));
        typists.add(new SwingTypist("HUNT_N_PECK",   0.30));
        String passage;
        passage = "The last man of Earth sat alone in a room. There was a knock on the door";
        passage = "In the rigor which is space, this spacesuit was designed by engineers to maintain your life in space, and can be called the smallest spaceship";
        final SwingTypingRace race = new SwingTypingRace(passage, typists);
        try{
            race.startRace();
        }catch (RulesException e){
            System.out.println("Rules Exception: " + e.message);
        }
    }
}
