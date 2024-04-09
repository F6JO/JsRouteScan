package ui.tab.Info;

import burp.BurpExtender;

import javax.swing.*;
import java.awt.*;

public class ScanTab {

    private JSplitPane splitPane;
    private BurpExtender burp;

    public RequestTab requestTab;
    public DetailsTab detailsTab;

    public ScanTab(BurpExtender burp){
        this.burp = burp;
        this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.requestTab = new RequestTab(burp);
        this.detailsTab = new DetailsTab(burp);
        this.init();
    }

    public void init(){
        this.splitPane.add(this.requestTab.getTab());
        this.splitPane.add(this.detailsTab.getTab());
    }

    public Component getTab(){
        return this.splitPane;
    }


}
