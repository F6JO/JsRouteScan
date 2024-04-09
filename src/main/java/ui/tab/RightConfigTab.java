package ui.tab;

import burp.BurpExtender;
import ui.Interface.TabInterface;

import javax.swing.*;
import java.awt.*;

public class RightConfigTab implements TabInterface {
    private BurpExtender burp;
    private JPanel panel;
    public RightConfigTab(BurpExtender burp) {
        this.burp = burp;
        this.panel = new JPanel();

        this.init();
    }
    private void init(){

    }

    @Override
    public Component getTab() {
        return this.panel;
    }
}
