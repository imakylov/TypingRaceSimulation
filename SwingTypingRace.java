import java.awt.*;
import javax.swing.*;

/**
 * An extension of TypingRace that only holds SwingTypist typists. A maximum of 6 typists race using the same logic as in TypingRace,
 * but with a swing interface.
 *
 * @author Adil Akylov
 * @version 1.1
 */
public class SwingTypingRace extends TypingRace<SwingTypist> {
    private final String passage;
    private final JPanel viewRoot;
    private JPanel controlPanel;
    private JTypistLane[] lanes;
    private boolean finilised;

    @Override
    public int getMaxTypists(){return 6;}

    /**
     * constructor of class SwingTypingRace. prepares the fields and automatically builds the race layout.
     *
     * @param passage the passage that the typists will compete for
     */
    public SwingTypingRace(String passage, JComponent parent){
        if(passage == null)passage = "";
        super(passage.length());
        this.passage = passage;
        this.viewRoot = Utils.getBoxPanel(BoxLayout.Y_AXIS);
        parent.add(this.viewRoot, "RACE");
        this.finilised = false;
    }

    public void addButton(JButton btn){
        this.controlPanel.add(btn);
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

    /**
     * builds the race layout including all tracks and typist infos
     */
    public JPanel buildLayout(){
        this.prepareForRace();
        this.viewRoot.add(this.buildControlPanel());
        this.viewRoot.add(this.buildTypistLanes());
        this.printRace();
        return this.viewRoot;
    }

    @Override
    public void startRace(){
        if(!this.finilised)this.buildLayout();
        new Thread(() -> {
            try {
                super.startRace();
            } catch (RulesException ex) {
                if(ex.fatal) this.printBadMessage(ex.message);
                else this.printSystemMessage(ex.message);
            }
            this.finilised = false;
        }).start();
    }

    private JPanel buildControlPanel(){
        this.controlPanel = new JPanel(new FlowLayout());
        JButton startBtn = new JButton("Start race");
        startBtn.addActionListener(ev -> this.startRace());
        this.controlPanel.add(startBtn);
        return this.controlPanel;
    }
    private JPanel buildTypistLanes(){
        JPanel tracksPanel = Utils.getBoxPanel(BoxLayout.Y_AXIS);
        this.lanes = new JTypistLane[this.getSeatsTaken()];
        for(int i=0;i<this.getSeatsTaken();i++){
            this.lanes[i] = new JTypistLane(this.typists.get(i));
            tracksPanel.add(this.lanes[i].buildLane(this.passage));
        }this.finilised = true;
        return tracksPanel;
    }

    /**
     * renders the current state of the race on tracks and user infos.
     * Uses invokeLater to move all ui work to ui thread.
     */
    @Override
    public void printRace(){
        if(!this.finilised)this.buildLayout();
        SwingUtilities.invokeLater(() -> {
            for(JTypistLane lane : this.lanes){
                lane.update();
            }
        });
    }

    @Override
    public void addTypist(SwingTypist typist) throws RulesException{
        if(this.finilised)throw new RulesException("seats cannot be changed after race is started", false);
        super.addTypist(typist);
    }

    @Override
    public void removeTypist(SwingTypist typist) throws RulesException{
        if(this.finilised)throw new RulesException("seats cannot be changed after race is started", false);
        super.removeTypist(typist);
    }
}
