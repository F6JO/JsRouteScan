package ui.tab.path;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import burp.IHttpService;
import core.Content.HostContent;
import core.Content.RouteContent;
import utils.RequestOper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class RightTab {

    private BurpExtender burp;
    public JTextPane scanRootPathPane;
    public JTextField scanRootPathField;
    public JButton scanButton;
    public JButton recScanButton;
    private JPanel panel;

    public RightTab(BurpExtender burp) {
        this.burp = burp;
        this.panel = new JPanel();
        this.scanRootPathPane = new JTextPane();
        this.scanRootPathPane.setText("Scan Root Path: ");
        this.scanRootPathPane.setEditable(false);
        this.scanRootPathField = new JTextField();
        this.scanRootPathField.setText("/");
        this.scanButton = new JButton("Scan");
        this.recScanButton = new JButton("Recursion-Scan");

        this.init();
    }

    public void init(){
        this.panel.add(this.packaging(this.scanRootPathPane,this.scanRootPathField));
        this.panel.add(this.packaging(new JPanel(),new JPanel(),this.scanButton));
        this.addEmptyRow();
        this.panel.add(this.recScanButton);
        this.panel.setLayout(new GridLayout(30, 1));
        this.scanButtonFunc();
        this.recScanButtonFunc();
    }


    public void addEmptyRow(){
        this.panel.add(new JPanel());
    }
    public JPanel packaging(Component... components){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, components.length));
        for (Component component : components) {
            panel.add(component);
        }
        return panel;
    }


    public void scanButtonFunc(){
        this.scanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String scanRootPathText = scanRootPathField.getText();
                if (scanRootPathText.equals("")){
                    burp.tab.prompt("Wrong scan root path!");
                    return;
                }
                if (burp.tab.reqDisplay.hosttab.getSelected().size() == 0){
                    burp.tab.prompt("Please select a host!");
                    return;
                }
                HostContent hostContent = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0));
                List<String> headers = hostContent.getHeaders();
                IHttpService httpService = hostContent.getHttpService();
                for (RouteContent routeContent:hostContent.routeContents){
                    burp.launchRequest.fuckGO(httpService,routeContent.getRoute(),headers,hostContent,scanRootPathText);
                }


            }
        });

    }


    public void recScanButtonFunc(){
        this.recScanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (burp.tab.reqDisplay.hosttab.getSelected().size() == 0){
                    burp.tab.prompt("Please select a host!");
                    return;
                }
                HostContent hostContent = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0));
                List<IHttpRequestResponse> siteMap = Arrays.asList(burp.call.getSiteMap(hostContent.getUrl()));
                if (hostContent.is80or443()){
                    siteMap = RequestOper.matchPrefix(siteMap, burp.helpers);
                }
                burp.launchRequest.recursionScan(hostContent,siteMap);


            }
        });
    }


    public Component getTab() {
        return this.panel;
    }



}
