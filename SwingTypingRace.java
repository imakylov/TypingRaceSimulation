import java.awt.*;
import javax.swing.*;

/**
 * An extension of TypingRace that only holds SwingTypist typists. A maximum of 6 typists race using the same logic as in TypingRace,
 * but with a swing interface.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class SwingTypingRace extends TypingRace<SwingTypist> {
    private final JPanel viewRoot;
    private String passage;
    private JPanel tracksPanel = null;
    private JPanel controlPanel;
    private JTypistLane[] lanes = null;
    private boolean finilised = false;
    private Thread raceThread;

    @Override
    public int getMaxTypists(){return 6;}

    /**
     * constructor of class SwingTypingRace. prepares the fields and automatically builds the race layout.
     *
     * @param passage the passage that the typists will compete for
     */
    public SwingTypingRace(String passage, JComponent parent, JButton backBtn){
        if(passage == null)passage = "";
        super(passage.length());
        this.passage = passage;
        this.viewRoot = Utils.getBoxPanel(BoxLayout.Y_AXIS);
        this.viewRoot.add(this.buildControlPanel(backBtn));
        parent.add(this.viewRoot, "RACE");
    }

    /**
     * clears race for later restart
     */
    public void prepareForClose(){
        this.viewRoot.remove(this.tracksPanel);
        this.tracksPanel = null;
        this.lanes = null;
        this.finilised = false;
        if(this.raceThread == null)return;
        this.raceThread.interrupt();
        this.raceThread = null;
    }

    /**
     * prepares race to be opened. builds lanes and finilises the layout and typists seatings.
     */
    public void prepareForOpen(){
        this.viewRoot.add(this.buildTypistLanes());
        this.prepareForRace();
        this.printRace();
    }

    /**
     * Starts a thread that runs the main race work. All exceptions that occur go to toast messages.
     */
    @Override
    public void startRace(){
        if(!this.finilised)this.buildTypistLanes();
        this.raceThread = new Thread(FI.toastExceptionsIgnore(() -> super.startRace()));
        this.raceThread.start();
    }

    /**
     * @return JPanel holding control panel with buttons like start race or go back.
     */
    private JPanel buildControlPanel(JButton backBtn){
        this.controlPanel = new JPanel(new FlowLayout());
        JButton startBtn = new JButton("Start race");
        startBtn.addActionListener(e -> this.startRace());
        this.controlPanel.add(startBtn);
        this.controlPanel.add(backBtn);
        return this.controlPanel;
    }

    /**
     * builds JPanel that holds one JTypistLane for each seated typist. finilises the layout and typist seatings.
     * @return panel with typist lanes
     */
    private JPanel buildTypistLanes(){
        this.tracksPanel = Utils.getBoxPanel(BoxLayout.Y_AXIS);
        this.lanes = new JTypistLane[this.getSeatsTaken()];
        for(int i=0;i<this.getSeatsTaken();i++){
            this.lanes[i] = new JTypistLane(this.typists.get(i));
            this.tracksPanel.add(this.lanes[i].buildLane(this.passage));
        }this.finilised = true;
        return this.tracksPanel;
    }

    /**
     * renders the current state of the race on tracks and user infos.
     * Uses invokeLater to move all ui work to ui thread.
     */
    @Override
    public void printRace(){
        if(!this.finilised)this.buildTypistLanes();
        SwingUtilities.invokeLater(() -> {
            if(!this.finilised)return;
            for(JTypistLane lane : this.lanes){
                lane.update(super.calculateWPM(lane.typist));
            }
        });
    }

    @Override
    public int getPassageLength(){
        return this.passage.length();
    }

    public void setPassage(String passage) throws RulesException{
        if(this.finilised)throw new RulesException("Cannot change passage while race is running", false);
        this.passage = passage;
    }

    /**
     * @param typist typist to add
     * @throws RulesException exception if there are no more seats or layout is already finilised.
     */
    @Override
    public void addTypist(SwingTypist typist) throws RulesException{
        if(this.finilised)throw new RulesException("seats cannot be changed after race is started", false);
        super.addTypist(typist);
    }

    /**
     * @param typist typist to remove
     * @throws RulesException exception if there is no such typist or layout is already finilised.
     */
    @Override
    public void removeTypist(SwingTypist typist) throws RulesException{
        if(this.finilised)throw new RulesException("seats cannot be changed after race is started", false);
        super.removeTypist(typist);
    }

    /**
     * @param message the message to be pushed in a bad toast
    */
    @Override
    protected void printBadMessage(String message){
        ToastManager.get().push(Toast.bad(message));
    }

    /**
     * @param message the message to be pushed in a good toast
    */
    @Override
    protected void printGoodMessage(String message){
        ToastManager.get().push(Toast.good(message));
    }

    /**
     * @param message the message to be pushed in a system toast
    */
    @Override
    protected void printSystemMessage(String message){
        ToastManager.get().push(Toast.system(message));
    }

    /**
     * @param message the message to be pushed in a toast
     */
    @Override
    protected void printMessage(String message){
        ToastManager.get().push(new Toast(message));
    }
}
