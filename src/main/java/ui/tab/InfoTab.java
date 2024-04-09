package ui.tab;

import burp.BurpExtender;
import ui.Interface.TabInterface;
import ui.tab.Info.ScanTab;

import javax.swing.*;
import java.awt.*;

public class InfoTab implements TabInterface {

    public JSplitPane jSplitPane;
    private BurpExtender burp;
    public PathTab pathTab;
    public ScanTab scanTab;

    public InfoTab(BurpExtender burp){
        this.burp = burp;
        this.jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        this.init();
    }

    private void init(){
        JTabbedPane tabs = new JTabbedPane();

        this.pathTab = new PathTab(this.burp);
        this.scanTab = new ScanTab(this.burp);
        tabs.addTab("path", this.pathTab.getTab());
        tabs.addTab("scan", this.scanTab.getTab());

        this.jSplitPane.setTopComponent(tabs);
    }


    @Override
    public Component getTab() {
        return this.jSplitPane;
    }
}
