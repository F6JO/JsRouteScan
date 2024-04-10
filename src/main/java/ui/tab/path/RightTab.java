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

//    private JTextPane custHead;
    private JTextArea custHeadField;
    private JButton setHeadButton;
//    private JButton addHeadButton;

    public RightTab(BurpExtender burp) {
        this.burp = burp;
        this.panel = new JPanel();
        this.panel.setLayout(new GridLayout(3, 1));
//        this.custHead = new JTextPane();
//        this.custHead.setText("Headers: ");
//        this.custHead.setEditable(false);
        this.setHeadButton = new JButton("SetHead");
//        this.addHeadButton = new JButton("AddHead");
        this.custHeadField = new JTextArea(1,1);
        this.custHeadField.setLineWrap(false);
//        this.custHeadField.setPreferredSize(new Dimension(200, 100));

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
        JPanel onePanel = new JPanel();
        onePanel.setLayout(new GridLayout(2, 1));
//        onePanel.add(this.packaging(this.custHead,new JScrollPane(this.custHeadField)));
        onePanel.add(new JScrollPane(this.custHeadField));

        JPanel oneonePanel = new JPanel();
        oneonePanel.setLayout(new GridLayout(5, 1));
        oneonePanel.add(this.packaging(new Panel(),this.setHeadButton));
        onePanel.add(oneonePanel);
//        this.addEmptyRow(onePanel);

        JPanel twoPanel = new JPanel();
        twoPanel.setLayout(new GridLayout(10, 1));
        twoPanel.add(this.packaging(this.scanRootPathPane,this.scanRootPathField));
        twoPanel.add(this.packaging(new JPanel(),new JPanel(),this.scanButton));
        this.addEmptyRow(twoPanel);
        twoPanel.add(this.recScanButton);

        this.panel.add(onePanel);
        this.panel.add(twoPanel);

        this.scanButtonFunc();
        this.recScanButtonFunc();
        this.setHeadButtonFunc();
    }

    private void setHeadButtonFunc() {
        this.setHeadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (burp.tab.reqDisplay.hosttab.getSelected().size() == 0){
                    burp.tab.prompt("Please select a host!");
                    return;
                }
                String headerText = custHeadField.getText();
                List<String> headers = RequestOper.parseHead(headerText);
                if (headers == null){
                    burp.tab.prompt("Wrong header!");
                }
                HostContent hostContent = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0));
                boolean withBool = true;
                for (String head : headers){
                    if (head.startsWith("Host:")){
                        withBool = false;
                        break;
                    }
                }
                if (withBool){
                    headers.add(0,"Host: " + hostContent.getHost());
                }

                hostContent.setHeaders(headers);
                burp.tab.prompt("Set " + hostContent.getHost() + " headers successfully!");

            }
        });
    }


    public void addEmptyRow(JPanel panel1){
        panel1.add(new JPanel());
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

    public void setCustHeadFieldText(List<String> headers){
        String headerText = "";
        for (String head : headers){
            headerText += head + "\n";
        }
        this.custHeadField.setText(headerText);
    }

    public Component getTab() {
        return this.panel;
    }



}
