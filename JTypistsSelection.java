import java.awt.*;
import java.util.ArrayList;
import java.util.function.Predicate;
import javax.swing.*;

public class JTypistsSelection extends JPanel {
    public static ArrayList<SwingTypist> defaultRotation(){
        ArrayList<SwingTypist> typists = new ArrayList<>();
        typists.add(new SwingTypist("TURBOFINGERS", 0.85, Color.RED));
        typists.add(new SwingTypist("QWERTY_QUEEN",  0.60, Color.ORANGE));
        typists.add(new SwingTypist("HUNT_N_PECK",   0.30, Color.YELLOW));
        typists.add(new SwingTypist("Ivan",   0.50, Color.GREEN));
        typists.add(new SwingTypist("Alex",   0.51, Color.BLUE));
        typists.add(new SwingTypist("Monawar",   0.61, new Color(250, 0, 250)));
        typists.add(new SwingTypist("Oliver",   0.70, Color.PINK));
        return typists;
    }

    private final ArrayList<JTypistSelection> selections = new ArrayList<>();
    private Predicate<SwingTypist> onAdd;
    private Predicate<SwingTypist> onRemove;
    public JTypistsSelection(ArrayList<SwingTypist> typists){
        super();
        if(typists == null) typists = defaultRotation();
        this.setLayout(new GridLayout(0, 1, 0, 10));
        for(SwingTypist typist : typists){
            JTypistSelection selection = new JTypistSelection(typist);
            selection.addMouseListener(Utils.onLeftClick(() -> toggle(selection)));
            this.selections.add(selection);
            this.add(selection);
        }
    }

    private JTypistSelection getSelection(SwingTypist typist){
        for(JTypistSelection selection : this.selections){
            if(selection.getTypist().equals(typist)){
                return selection;
            }
        }return null;
    }

    public void toggle(SwingTypist typist){
        this.toggle(getSelection(typist));
    }
    private void toggle(JTypistSelection selection){
        if(selection.isSelected()){
            this.unselect(selection);
        }else this.select(selection);
    }
    private void select(JTypistSelection selection){
        if(!this.onAdd.test(selection.getTypist())) return;
        selection.select();
    }

    private void unselect(JTypistSelection selection){
        if(!this.onRemove.test(selection.getTypist())) return;
        selection.unselect();
    }

    public void unselectAll(){
        for(JTypistSelection selection : this.selections){
            if(selection.isSelected()) this.unselect(selection);
        }
    }

    public void onAdd(Predicate<SwingTypist> f){
        this.onAdd = f;
    }
    public void onRemove(Predicate<SwingTypist> f){
        this.onRemove = f;
    }
}

class JTypistSelection extends JPanel {
    private final SwingTypist typist;
    private boolean selected;
    private JLabel indicator;

    public JTypistSelection(SwingTypist typist){
        super();
        this.setLayout(new FlowLayout());
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        this.typist = typist;
        this.selected = false;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(this.buildIndicator());
        JTypistInfo info = new JTypistInfo(typist);
        this.add(info);
        info.update();
    }

    private JPanel buildIndicator(){
        JPanel panel = new JPanel(new BorderLayout());
        this.indicator = new JLabel();
        panel.add(this.indicator, SwingConstants.CENTER);
        return panel;
    }

    public SwingTypist getTypist(){
        return this.typist;
    }
    public boolean isSelected(){
        return this.selected;
    }
    public void select(){
        indicator.setText("●");
        this.selected = true;
    }
    public void unselect(){
        indicator.setText("");
        this.selected = false;
    }
}
