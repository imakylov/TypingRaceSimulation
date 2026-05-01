import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Entry point for 
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class SwingMenu extends JPanel{
    /**
     * Rudimentary entry point for swing typing races. quick starts a sample race.
     */
    static JFrame frame;
    static JPanel cardsPanel;
    static CardLayout cardLayout;
    static SwingTypingRace race;

    private JPanel controlPanel;
    private JTextArea passageInput;

    public SwingMenu(SwingTypingRace race){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(this.buildControlPanel());
        JPanel main = Utils.getBoxPanel(BoxLayout.X_AXIS);
        main.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        this.add(main);
        main.add(this.buildTypistSelection());
        main.add(this.buildPassagePanel());
    }
    
    private JScrollPane buildTypistSelection(){
        JTypistSelection typistSelection = JTypistSelection.fromAll(true);
        typistSelection.onTryAdd(FI.toastExceptions(typist -> race.addTypist(typist)));
        typistSelection.onTryRemove(FI.toastExceptions(typist -> race.removeTypist(typist)));
        JScrollPane scrollPane = new JScrollPane(typistSelection);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        return scrollPane;
    }

    private JPanel buildPassagePanel(){
        JPanel passagePanel = Utils.getBoxPanel(BoxLayout.Y_AXIS);
        this.passageInput = new JTextArea();
        this.passageInput.setFont(new Font("Monospace", 1, 15));
        this.passageInput.setLineWrap(true);
        this.passageInput.setWrapStyleWord(true);
        JScrollPane inputPane = new JScrollPane(passageInput);
        Utils.setStableSize(inputPane, 500, 70);
        inputPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 20));

        passagePanel.add(inputPane);
        JPassageSelection passageSelection = new JPassageSelection(null, false);
        passageSelection.onAdd(p -> this.passageInput.setText(p));
        passageSelection.onRemove(p -> {
            if(this.passageInput.getText().equals(p))this.passageInput.setText("");
        });
        JScrollPane scrollPane = new JScrollPane(passageSelection);
        scrollPane.setBorder(null);
        passagePanel.add(scrollPane);
        return passagePanel;
    }

    private JPanel buildControlPanel(){
        this.controlPanel = new JPanel(new FlowLayout());
        this.controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.controlPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(ev -> {
            try {
                race.setPassage(this.passageInput.getText());
            } catch (RulesException ex) {
                ToastManager.get().push(ex);
            }
            race.prepareForOpen();
        });
        startButton.addActionListener(e -> cardLayout.show(cardsPanel, "RACE"));
        this.controlPanel.add(startButton);
        JButton editTypistsButton = new JButton("Edit Typists");
        editTypistsButton.addActionListener(e -> cardLayout.show(cardsPanel, "EDIT"));
        this.controlPanel.add(editTypistsButton);

        return this.controlPanel;
    }

    public static void main(String[] args) throws RulesException{
        SwingUtilities.invokeLater(FI.toastExceptionsIgnore(() -> {
            setupFrame();
            ToastManager.init(frame);
            
            cardLayout = new CardLayout();
            cardsPanel = new JPanel(cardLayout);
    
            JPanel raceView = setupRace();
            
            cardsPanel.add(new SwingMenu(race), "MENU");
            cardsPanel.add(raceView, "RACE");
            cardsPanel.add(new TypistEditingView(menuButton()), "EDIT");
    
            frame.add(cardsPanel);
        }));
    }
    static void setupFrame(){
        frame = new JFrame("Typing Race simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    static JPanel setupRace() throws RulesException{
        JPanel racePanel = new JPanel();
        JButton backBtn = menuButton();
        race = new SwingTypingRace("", racePanel, backBtn);
        backBtn.addActionListener(e -> race.prepareForClose());
        return racePanel;
    }
    public static JButton menuButton(){
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(cardsPanel, "MENU"));
        return backBtn;
    }
}
