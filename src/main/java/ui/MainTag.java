package ui;

import burp.BurpExtender;
import burp.ITab;

import javax.swing.*;
import java.awt.*;

//class MyRunnable implements Runnable {
//    private MainTag myClass;
//
//    public MyRunnable(MainTag myClass) {
//        this.myClass = myClass;
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            myClass.updateUI();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ignored) {
//
//            }
//        }
//    }
//}
public class MainTag implements ITab {

    private BurpExtender burp;
    private final String TAGNAME = "JsRouteScan";

    private JSplitPane jSplitPane;
    private JTabbedPane tabs;

    public ReqDisplayTag reqDisplay;
    public ConfigTag config;

    public MainTag(BurpExtender burpExtender) {
        this.burp = burpExtender;
        this.jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.init();
    }

    private void init(){
        this.tabs = new JTabbedPane();

        // Interface initialization
        this.reqDisplay = new ReqDisplayTag(this.burp);
        this.config = new ConfigTag(this.burp);


        this.tabs.addTab("ReqDisplay", reqDisplay.getTag());
        this.tabs.addTab("Config", config.getTag());

        // Setting the main interface
        this.jSplitPane.setTopComponent(this.tabs);
    }


//    public void updateUI(){
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                burp.call.printOutput("updateUI");
//                tabs.revalidate();
//                tabs.repaint();
//            }
//        });
//    }
    public void prompt(String message){
        JOptionPane.showMessageDialog(this.getUiComponent(), message);
    }

    @Override
    public String getTabCaption() {
        return this.TAGNAME;
    }

    @Override
    public Component getUiComponent() {
        return this.jSplitPane;
    }
}
