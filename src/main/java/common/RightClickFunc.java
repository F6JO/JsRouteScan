package common;

import burp.BurpExtender;
import core.Content.HostContent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RightClickFunc {

    public static void DeleteHost(BurpExtender burp, JMenuItem menuItem){
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (String s : burp.tab.reqDisplay.hosttab.getSelected()) {
                    burp.tab.reqDisplay.hosttab.remove(s);
                }
                burp.tab.reqDisplay.infotab.pathTab.leftTab.clear();
                burp.tab.reqDisplay.infotab.pathTab.leftTab.bottomTab.clear();
                burp.tab.reqDisplay.infotab.scanTab.requestTab.clear();
                burp.tab.reqDisplay.infotab.scanTab.detailsTab.clear();
            }
        });
    }

    public static void CleanHost(BurpExtender burp, JMenuItem menuItem){
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                burp.tab.reqDisplay.hosttab.clear();
                burp.tab.reqDisplay.infotab.pathTab.leftTab.clear();
                burp.tab.reqDisplay.infotab.pathTab.leftTab.bottomTab.clear();
                burp.tab.reqDisplay.infotab.scanTab.requestTab.clear();
                burp.tab.reqDisplay.infotab.scanTab.detailsTab.clear();

            }
        });
    }


    public static void DeleteRoute(BurpExtender burp, JMenuItem menuItem){
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> selectedRoute = burp.tab.reqDisplay.infotab.pathTab.leftTab.getSelected();
                HostContent findHost = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0));
                for (String s : selectedRoute) {
                    findHost.removeRoute(s);
                    burp.tab.reqDisplay.infotab.pathTab.leftTab.remove(s);
                }
                burp.tab.reqDisplay.infotab.pathTab.leftTab.bottomTab.clear();

            }
        });
    }

    public static void CleanRoute(BurpExtender burp, JMenuItem menuItem){
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HostContent findHost = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0));
                findHost.clearRoute();
                burp.tab.reqDisplay.infotab.pathTab.leftTab.clear();
                burp.tab.reqDisplay.infotab.pathTab.leftTab.bottomTab.clear();
            }
        });
    }


    public static void DeleteScan(BurpExtender burp, JMenuItem deleteMenuItem) {
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> selectedRoute = burp.tab.reqDisplay.infotab.scanTab.requestTab.getSelected();
                HostContent findHost = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0));
                for (String s : selectedRoute) {
                    findHost.removeScan(s);
                    burp.tab.reqDisplay.infotab.scanTab.requestTab.remove(s);
                }
                burp.tab.reqDisplay.infotab.scanTab.detailsTab.clear();

            }
        });
    }

    public static void CleanScan(BurpExtender burp, JMenuItem clearMenuItem) {
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HostContent findHost = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0));
                findHost.clearScan();
                burp.tab.reqDisplay.infotab.scanTab.requestTab.clear();
                burp.tab.reqDisplay.infotab.scanTab.detailsTab.clear();
            }
        });
    }

    public static void RefreshScan(BurpExtender burp, JMenuItem refreshMenuItem) {
        refreshMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                burp.tab.reqDisplay.infotab.scanTab.requestTab.updateAll();
            }
        });
    }
}
