package ui.tab;

import burp.BurpExtender;
import ui.Interface.TabInterface;
import ui.tab.path.LeftTab;
import ui.tab.path.RightTab;

import javax.swing.*;
import java.awt.*;

public class PathTab implements TabInterface {

    private JSplitPane splitPane;
    private BurpExtender burp;
    public LeftTab leftTab;
    public RightTab rightTab;
    public PathTab(BurpExtender burp) {
        this.burp = burp;
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.init();
    }


    private void init(){
        this.leftTab = new LeftTab(burp);
        this.rightTab = new RightTab(burp);
        this.splitPane.setResizeWeight(0.9);
        this.splitPane.add(this.leftTab.getTab());
        this.splitPane.add(this.rightTab.getTab());
    }

    @Override
    public Component getTab() {
        return this.splitPane;
    }
}
