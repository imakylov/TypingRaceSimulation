import java.awt.*;
import javax.swing.*;

/**
 * An extension of TypingRace that only holds SwingTypist typists. A maximum of 6 typists race using the same logic as in TypingRace,
 * but with a swing interface.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class TypistEditingView extends JPanel {
    private JPanel controlPanel;
    private JTextField nameInput;
    private JTextField accuracyInput;
    private JColorChooser colorInput;
    private JTypistSelection selection = null;
    private SwingTypist selected = null;

    /**
     * constructor of class TypistEditingView. Automatically builds the editing layout
     */
    public TypistEditingView(JButton backBtn){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(this.buildControlPanel(backBtn));
        JPanel main = Utils.getBoxPanel(BoxLayout.X_AXIS);
        main.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        this.add(main);
        main.add(this.buildTypistSelection());
        main.add(this.buildTypistForm());
    }

    /**
     * @return JPanel holding control panel with buttons like save, delete or go back
     */
    private JPanel buildControlPanel(JButton backBtn){
        this.controlPanel = new JPanel(new FlowLayout());
        
        JButton addBtn = new JButton("Add Typist");
        addBtn.addActionListener(e -> this.addTypist());
        this.controlPanel.add(addBtn);
        
        JButton saveBtn = new JButton("Save Typist");
        saveBtn.addActionListener(FI.toastExceptionsAction(e -> this.saveSelected()));
        this.controlPanel.add(saveBtn);

        JButton deleteBtn = new JButton("Delete Typist");
        deleteBtn.addActionListener(FI.toastExceptionsAction(e -> {
            if(this.selected == null) throw new RulesException("Typist not selected", false);
            this.selection.removeOption(this.selected);
        }));
        this.controlPanel.add(deleteBtn);
        
        this.controlPanel.add(backBtn);
        return this.controlPanel;
    }

    private JScrollPane buildTypistSelection(){
        this.selection = JTypistSelection.fromAll(false);
        this.selection.onAdd(typist -> this.select(typist));
        this.selection.onRemoveAll(() -> this.unselect());
        JScrollPane scrollPane = new JScrollPane(this.selection);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        return scrollPane;
    }

    private JPanel buildTypistForm(){
        JPanel form = Utils.getBoxPanel(BoxLayout.Y_AXIS);
        JPanel textFieldsPanel = Utils.getBoxPanel(BoxLayout.X_AXIS);
        this.nameInput = new JTextField();
        textFieldsPanel.add(this.nameInput);
        this.accuracyInput = new JTextField();
        textFieldsPanel.add(this.accuracyInput);
        form.add(textFieldsPanel);
        this.colorInput = new JColorChooser();
        form.add(this.colorInput);
        return form;
    }

    private void select(SwingTypist typist){
        this.selected = typist;
        this.nameInput.setText(typist.getName());
        this.accuracyInput.setText(String.format("%.3f", 100*typist.getAccuracy()));
        this.colorInput.setColor(typist.getColor());
    }
    private void unselect(){
        this.selected = null;
        this.nameInput.setText("");
        this.accuracyInput.setText("");
        this.colorInput.setColor(null);
    }

    private void saveSelected() throws RulesException{
        if(this.selected == null) throw new RulesException("Typist not selected", false);
        String name = this.nameInput.getText();
        double accuracy = FI.ruleNumberFormat(() -> Double.valueOf(this.accuracyInput.getText())) / 100;
        Color color = this.colorInput.getColor();
        if(color == null) throw new RulesException("Color not selected", true);
        this.selected.setName(name);
        this.selected.setAccuracy(accuracy);
        this.selected.setColor(color);
        JTypistSelection.updateAll();
    }

    private void addTypist(){
        SwingTypist typist = SwingTypist.makeRandom();
        this.selection.addOption(typist);
        this.selection.toggle(typist);
    }
}