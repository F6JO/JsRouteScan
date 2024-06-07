package burp;

import common.BurpListening;
import ui.MainTag;
import utils.LaunchRequest;

import javax.swing.*;

public class BurpExtender implements IBurpExtender {

    public IBurpExtenderCallbacks call;

    private BurpExtender burp;
    public IExtensionHelpers helpers;
    public static String EXPAND_NAME = "JsRouteScan";
    public static String AUTHOR = "F6JO";
    public static String LINK = "https://github.com/F6JO/JsRouteScan";
    public static String VERSION = "1.2.1";
    public static String CONFIGPATH = System.getProperty("user.home") + "/.config/JsRouteScan/config.yaml";
    public LaunchRequest launchRequest;

    public Config config;
    public MainTag tab;

    private void printBanner(){
        this.call.printOutput("@Info: Load "+EXPAND_NAME+" success");
        this.call.printOutput("@Version: " + EXPAND_NAME + " v" + VERSION);
        this.call.printOutput("@From: Code by " + AUTHOR);
        this.call.printOutput("@Github: " + LINK);
        this.call.printOutput("");
    }
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks iBurpExtenderCallbacks) {
        this.burp = this;
        this.config = new Config(this);
        this.call = iBurpExtenderCallbacks;
        this.helpers = iBurpExtenderCallbacks.getHelpers();
        this.call.setExtensionName(EXPAND_NAME + " " + VERSION);
        this.launchRequest = new LaunchRequest(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                tab = new MainTag(burp);
                call.addSuiteTab(tab);
            }
        });
//        this.tab = new MainTag(this);
//        this.call.addSuiteTab(this.tab);
        this.call.registerHttpListener(new BurpListening(this));
        this.printBanner();
    }



}
