package ui;

import burp.BurpExtender;
import ui.Interface.TagInterface;
import ui.tab.LeftConfigTab;
import ui.tab.RightConfigTab;

import javax.swing.*;
import java.awt.*;

public class ConfigTag implements TagInterface {
    private BurpExtender burp;

    private JSplitPane splitPane;

    private LeftConfigTab leftConfig;
    private RightConfigTab rightConfigTab;



    public ConfigTag(BurpExtender burp) {
        this.burp = burp;
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.leftConfig = new LeftConfigTab(burp);
        this.rightConfigTab = new RightConfigTab(burp);
        this.init();
    }
    private void init() {
        this.splitPane.add(this.leftConfig.getTab());
        this.splitPane.add(this.rightConfigTab.getTab());
        this.splitPane.setDividerLocation(0.5);
    }



    @Override
    public Component getTag() {
        return this.splitPane;
    }
}
