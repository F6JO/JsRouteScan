package ui.tab;

import burp.BurpExtender;
import core.Content.HostContent;
import core.Interface.ContentClass;
import common.RightClickFunc;
import org.jdesktop.swingx.JXTable;
import ui.Interface.JXTableInterfae;
import ui.Interface.TabInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HostTab implements TabInterface, JXTableInterfae {

    private JXTable jXTable;
    private DefaultTableModel model;
    private ArrayList<HostContent> hostList;
    private BurpExtender burp;


    public HostTab(BurpExtender burp) {
        this.burp = burp;
        this.hostList = new ArrayList<>();
        this.jXTable = new JXTable();
        this.jXTable.setSortable(false);

        this.init();

    }

    private void init() {
        // 创建一个表格模型
        this.model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 使表格的单元格不可编辑
                return false;
            }
        };
        // 添加列名
        this.model.addColumn("HOST");
        // 将模型设置为表格的模型
        this.jXTable.setModel(this.model);
        Select_Change_Listener();
        Select_RightClick_Listener();
    }

    // 添加一条URL
    @Override
    public ContentClass add(Object host) {
        // 添加URL到列表
        HostContent hostContent = new HostContent((String) host);
        this.hostList.add(hostContent);
        this.updateAll();
        return hostContent;
    }

    // 删除一条URL
    @Override
    public void remove(String host) {
        for (HostContent u : this.hostList) {
            if (u.getHost().equals(host)) {
                this.hostList.remove(u);
                break;
            }
        }
        this.updateAll();
    }

    @Override
    public HostContent find(String host) {
        for (HostContent u : this.hostList) {
            if (u.getHost().equals(host)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        this.hostList.clear();
        this.updateAll();
    }

    @Override
    public void updateAll() {
        // 更新表格
        try {
            this.model.setRowCount(0); // 清空表格
        } catch (Exception ignored) {
        }
        for (HostContent u : this.hostList) {
            model.addRow(new Object[]{u.getHost()}); // 添加URL到表格
        }
    }


    @Override
    public void Select_Change_Listener() {

        // 添加监听器
        this.jXTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
//                    updateAllUI();
                    HostContent hostContent = find(getSelected().get(0));
                    burp.tab.reqDisplay.infotab.pathTab.leftTab.routeList = hostContent.routeContents;
                    burp.tab.reqDisplay.infotab.scanTab.requestTab.requestStorages = find(getSelected().get(0)).requestStorages;
                    burp.tab.reqDisplay.infotab.pathTab.leftTab.updateAll();
                    burp.tab.reqDisplay.infotab.scanTab.requestTab.updateAll();
                    burp.tab.reqDisplay.infotab.pathTab.rightTab.setCustHeadFieldText(hostContent.getHeaders());
                }
            }
        });
    }

//    public void updateAllUI() {
//        this.updateLeftUI();
//        this.updateRequestUI();
//    }
//    public void updateLeftUI() {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                if (getSelected().size() == 0) {
//                    return;
//                }
//                String selected = getSelected().get(0);
//                if (selected != null) {
//                    burp.tab.reqDisplay.infotab.pathTab.leftTab.clear();
//                    HostContent hostContent = find(selected);
//                    for (RouteContent route : hostContent.routeContents) {
//                        burp.tab.reqDisplay.infotab.pathTab.leftTab.add(route);
//                    }
//                    burp.tab.reqDisplay.infotab.pathTab.leftTab.bottomTab.clear();
//                }
//            }
//        });
//    }
//
//    public void updateRequestUI() {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                if (getSelected().size() == 0) {
//                    return;
//                }
//                String selected = getSelected().get(0);
//                if (selected != null) {
//                    burp.tab.reqDisplay.infotab.scanTab.requestTab.clear();
//                    HostContent hostContent = find(selected);
//                    for (RequestStorage requestStorage : hostContent.requestStorages) {
//                        burp.tab.reqDisplay.infotab.scanTab.requestTab.add(requestStorage);
//                    }
//                }
//            }
//        });
//    }

    @Override
    public void Select_RightClick_Listener() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem DeleteMenuItem = new JMenuItem("Delete");
        JMenuItem ClearMenuItem = new JMenuItem("Clear");
        RightClickFunc.DeleteHost(this.burp, DeleteMenuItem);
        RightClickFunc.CleanHost(this.burp, ClearMenuItem);
        popupMenu.add(DeleteMenuItem);
        popupMenu.add(ClearMenuItem);

        this.jXTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = jXTable.rowAtPoint(e.getPoint());
                    int column = jXTable.columnAtPoint(e.getPoint());

                    if (!jXTable.isRowSelected(row))
                        jXTable.changeSelection(row, column, false, false);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    public ArrayList<String> getSelected() {
        int[] selectedRows = this.jXTable.getSelectedRows();
        ArrayList<String> selectedData = new ArrayList<>();

        for (int row : selectedRows) {
            selectedData.add((String) model.getValueAt(row, 0));
        }

        return selectedData;
    }

    @Override
    public Component getTab() {
        JScrollPane scrollPane = new JScrollPane(this.jXTable);
        this.jXTable.setFillsViewportHeight(true);
        return scrollPane;
    }

}
