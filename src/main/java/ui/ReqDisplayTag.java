package ui;

import burp.BurpExtender;
import ui.Interface.TagInterface;
import ui.tab.HostTab;
import ui.tab.InfoTab;

import javax.swing.*;
import java.awt.*;

public class ReqDisplayTag implements TagInterface {

    public JSplitPane splitPane;
    private BurpExtender burp;

    public HostTab hosttab;
    public InfoTab infotab;
    public ReqDisplayTag(BurpExtender burp) {
        this.burp = burp;
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.init();

    }
    private void init(){
        this.splitPane.setResizeWeight(0.2);
        this.hosttab = new HostTab(this.burp);
        this.infotab = new InfoTab(this.burp);

        this.splitPane.add(hosttab.getTab());
        this.splitPane.add(infotab.getTab());

    }
    @Override
    public Component getTag() {

        return this.splitPane;
    }
}
