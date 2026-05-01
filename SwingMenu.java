import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        JTypistSelection typistSelection = new JTypistSelection(null);
        typistSelection.onAdd(FI.toastExceptions(typist -> race.addTypist(typist)));
        typistSelection.onRemove(FI.toastExceptions(typist -> race.removeTypist(typist)));
        JScrollPane scrollPane = new JScrollPane(typistSelection);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        return scrollPane;
    }

    private JPanel buildPassagePanel(){
        JPanel passagePanel = Utils.getBoxPanel(BoxLayout.Y_AXIS);
        JPassageSelection passageSelection = new JPassageSelection(null);
        // passageSelection.
        // JTextField passageInput = new JTextField("", 60);
        // passageSelection.onAdd();
        passagePanel.add(passageSelection);
        return passagePanel;
    }

    private JPanel buildControlPanel(){
        this.controlPanel = new JPanel(new FlowLayout());
        this.controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.controlPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(e -> cardLayout.show(cardsPanel, "RACE"));
        startButton.addActionListener(e -> race.prepareForOpen());
        this.controlPanel.add(startButton);

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
    
            frame.add(cardsPanel);
        }));
    }
    static void setupFrame(){
        frame = new JFrame("Typing Race simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    static JPanel setupRace() throws RulesException{
        JPanel racePanel = new JPanel();
        String passage = "The last man of Earth sat alone in a room. There was a knock on the door.";
        // String passage = "In the rigor which is space, this spacesuit was designed by engineers to maintain your life in space, and can be called the smallest spaceship.";
        race = new SwingTypingRace(passage, racePanel);
        JButton backBtn = menuButton();
        backBtn.addActionListener(e -> race.prepareForClose());
        race.addButton(backBtn);
        return racePanel;
    }
    public static JButton menuButton(){
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> cardLayout.show(cardsPanel, "MENU"));
        return backBtn;
    }
}
