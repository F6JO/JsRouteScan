package ui.tab.path;

import burp.BurpExtender;
import common.RightClickFunc;
import core.Content.RouteContent;
import org.jdesktop.swingx.JXTable;
import core.Interface.ContentClass;
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

public class LeftTab implements TabInterface, JXTableInterfae {

    private JSplitPane splitPane;
    public JXTable jXTable;

    private BurpExtender burp;

    public ArrayList<RouteContent> routeList;
    private DefaultTableModel model;
    public BottomTab bottomTab;

    public LeftTab(BurpExtender burp) {
        this.burp = burp;
        this.jXTable = new JXTable();
//        this.jXTable.setSortable(false);
        this.routeList = new ArrayList<>();
//        this.routeList = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0)).routeContents;
        this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.bottomTab = new BottomTab();
        this.init();
    }
    private void init(){
        this.model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 使表格的单元格不可编辑
                return false;
            }
        };
        // 添加列名
        this.model.addColumn("PATH");
        // 将模型设置为表格的模型
        this.jXTable.setModel(this.model);
        Select_Change_Listener();
        Select_RightClick_Listener();
    }
    @Override
    public ArrayList<String> getSelected() {
        int[] selectedRows = this.jXTable.getSelectedRows();
        ArrayList<String> selectedData = new ArrayList<>();

        for (int row : selectedRows) {
            int modelRow = jXTable.convertRowIndexToModel(row);
            selectedData.add((String) model.getValueAt(modelRow, 0));
//            selectedData.add((String) model.getValueAt(row, 0));
        }

        return selectedData;
    }
    @Override
    public void Select_Change_Listener(){

        this.jXTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selected = getSelected().get(0);
                    if (selected != null) {
                        for (RouteContent r : routeList) {

                            if (r.getRoute().equals(selected)) {
                                bottomTab.setText(
                                        "Js: \n  " + r.url +
                                        "\n\nOriginal route: \n  " + r.originRoute +
                                        "\n\nAfter route: \n  " + r.route
                                );
                                break;
                            }

                        }

                    }
                }
            }
        });
    }


    @Override
    public void Select_RightClick_Listener() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem DeleteMenuItem = new JMenuItem("Delete");
        JMenuItem ClearMenuItem = new JMenuItem("Clear");
        RightClickFunc.DeleteRoute(this.burp,DeleteMenuItem);
        RightClickFunc.CleanRoute(this.burp,ClearMenuItem);
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
    public ContentClass add(Object a) {
        this.routeList.add((RouteContent)a);
        this.updateAll();
        return null;
    }

    @Override
    public void remove(String a) {
        for (RouteContent r : this.routeList) {
            if (r.getRoute().equals(a)) {
                this.routeList.remove(r);
                break;
            }
        }
        this.updateAll();
    }

    @Override
    public ContentClass find(String a) {
        for (RouteContent r : this.routeList) {
            if (r.getRoute().equals(a)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        this.routeList.clear();
        this.updateAll();
    }

    @Override
    public void updateAll(){
        // 更新表格
        synchronized (this.model){
            try {
                this.model.setRowCount(0); // 清空表格
            }catch (Exception ignored){
            }

            for (RouteContent r : this.routeList) {
                model.addRow(new Object[]{r.getRoute()});
            }
        }

    }

    @Override
    public Component getTab() {
        JScrollPane scrollPane = new JScrollPane(this.jXTable);
        this.jXTable.setFillsViewportHeight(true);
        this.splitPane.setResizeWeight(0.6);
        this.splitPane.add(scrollPane);
        this.splitPane.add(bottomTab.getTab());
        return this.splitPane;
    }
}
