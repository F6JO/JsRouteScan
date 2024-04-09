package ui.tab.Info;

import burp.BurpExtender;
import common.RightClickFunc;
import core.Storage.RequestStorage;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import ui.Interface.JXTableInterfae;
import ui.Interface.TabInterface;
import ui.MainTag;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class RequestTab implements JXTableInterfae, TabInterface {


    public JXTable jXTable;
    private BurpExtender burp;
    public ArrayList<RequestStorage> requestStorages;
    private final String[] columnNames = new String[]{"#", "Uri", "Method", "Route", "Status", "Size", "Time"};

    private DefaultTableModel model;

    public RequestTab(BurpExtender burp) {
        this.burp = burp;
        this.jXTable = new JXTable();
//        this.requestStorages = new ArrayList<RequestStorage>();
//        this.requestStorages = burp.tab.reqDisplay.hosttab.find(burp.tab.reqDisplay.hosttab.getSelected().get(0)).requestStorages;
        this.init();

    }

    public void init() {
        this.model = new DefaultTableModel();
        for (String columnName : this.columnNames) {
            this.model.addColumn(columnName);
        }

        this.jXTable.setSortable(true);
        this.jXTable.setModel(this.model);

        TableColumnExt column1 = this.jXTable.getColumnExt(0);
        TableColumnExt column2 = this.jXTable.getColumnExt(4);
        TableColumnExt column3 = this.jXTable.getColumnExt(5);
        column1.setComparator(this.getComparator());
        column2.setComparator(this.getComparator());
        column3.setComparator(this.getComparator());

        this.Select_Change_Listener();
        this.Select_RightClick_Listener();
    }

    @Override
    public Object add(Object a) {
        this.requestStorages.add((RequestStorage) a);
        this.updateAll();
        return null;
    }

    @Override
    public void remove(String a) {
        for (RequestStorage requestStorage : this.requestStorages) {
            if (Objects.equals(requestStorage.id, a)) {
                this.requestStorages.remove(requestStorage);
                break;
            }
        }
        this.updateAll();

    }

    @Override
    public Object find(String a) {
        for (RequestStorage requestStorage : this.requestStorages) {
            if (Objects.equals(requestStorage.id, a)) {
                return requestStorage;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        this.requestStorages.clear();
        this.updateAll();
    }

    @Override
    public void updateAll() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                synchronized (model) {

                    try {
                        model.setRowCount(0);
                    } catch (Exception ignored) {
                    }
                    synchronized (requestStorages) {
                        for (RequestStorage requestStorage : requestStorages) {
                            model.addRow(new Object[]{requestStorage.id, requestStorage.Uri, requestStorage.Method, requestStorage.Route, requestStorage.Status, requestStorage.Size, requestStorage.Time});
                        }
                    }

                }
            }
        });


    }

    @Override
    public void Select_Change_Listener() {
        this.jXTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selected = getSelected().get(0);
                    RequestStorage requestStorage = (RequestStorage) find(selected);
                    if (requestStorage != null) {
                        burp.tab.reqDisplay.infotab.scanTab.detailsTab.setDetailsStorage(requestStorage.detailsStorage);
                    }


                }
            }
        });


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
    public void Select_RightClick_Listener() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem RefreshMenuItem = new JMenuItem("Refresh");
        JMenuItem DeleteMenuItem = new JMenuItem("Delete");
        JMenuItem ClearMenuItem = new JMenuItem("Clear");
        RightClickFunc.RefreshScan(this.burp, RefreshMenuItem);
        RightClickFunc.DeleteScan(this.burp, DeleteMenuItem);
        RightClickFunc.CleanScan(this.burp, ClearMenuItem);
        popupMenu.add(RefreshMenuItem);
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
    public Component getTab() {
        JScrollPane scrollPane = new JScrollPane(this.jXTable);
        this.jXTable.setFillsViewportHeight(true);
        return scrollPane;
    }

    private Comparator<String> getComparator() {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(Integer.parseInt(s1), Integer.parseInt(s2));
            }
        };
        return comparator;
    }


}
