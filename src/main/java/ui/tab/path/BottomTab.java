package ui.tab.path;

import ui.Interface.TabInterface;

import javax.swing.*;
import java.awt.*;

public class BottomTab implements TabInterface {
    public JTextPane jTextPane;


    public BottomTab() {
        this.jTextPane = new JTextPane();
        this.init();

    }

    public void init() {
        this.jTextPane.setEditable(false);
    }
    public void clear() {
        this.jTextPane.setText("");
    }

    public void setText(String text) {
        this.jTextPane.setText(text);
    }

    @Override
    public Component getTab() {
        return this.jTextPane;
    }
}
