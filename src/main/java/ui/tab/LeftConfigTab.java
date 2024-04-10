package ui.tab;

import burp.BurpExtender;
import ui.Interface.TabInterface;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class LeftConfigTab implements TabInterface {

    private BurpExtender burp;
    private JPanel panel;
    private JButton passiveScanButton;
    private JButton CarryheadButton;
    private JSpinner intSpinner;
    private JComboBox<String> requestMethod;
    private JTextField passiveScanPathField;
    public LeftConfigTab(BurpExtender burp) {
        this.burp = burp;
        this.panel = new JPanel();
        this.passiveScanButton = new JButton("PassiveStart");
        this.CarryheadButton = new JButton("CarryHead");
        this.intSpinner = new JSpinner(new SpinnerNumberModel(burp.config.RequestThread, 1, null, 1));

        this.requestMethod = new JComboBox<>(new String[]{"GET", "POST"});
        this.requestMethod.setSelectedItem(burp.config.RequestMethod);

        this.passiveScanPathField = new JTextField();
        this.passiveScanPathField.setText(burp.config.PassiveScanPath);
        this.init();
    }

    public void init() {
        this.panel.add(this.packaging(this.passiveScanButton, this.CarryheadButton));
        this.addEmptyRow();
        this.panel.add(this.packaging(new Label("Thread Pools Number:"),this.intSpinner));
        this.addEmptyRow();
        this.panel.add(this.packaging(new Label("Request Method"),this.requestMethod));
        this.addEmptyRow();
        this.panel.add(this.packaging(new Label("Passive Scan Path"),this.passiveScanPathField));

        this.setStartButtonAction();
        this.setCarryHeadButtonAction();
        this.setIntSpinnerAction();
        this.setRequestMethodAction();
        this.setPassiveScanPathAction();

        this.panel.setLayout(new GridLayout(25, 1));
    }

    public JPanel packaging(Component... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, components.length));
        panel.add(new JPanel());
        for (Component component : components) {
            panel.add(component);
        }
        for (int i = 0; i < 1; i++) {
            panel.add(new JPanel());
        }
        return panel;
    }


    public void setCarryHeadButtonAction() {
        this.CarryheadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (burp.config.Carryhead) {
                    burp.config.Carryhead = false;
                    CarryheadButton.setText("CarryHead");
                    CarryheadButton.setBackground(null);
                } else {
                    burp.config.Carryhead = true;
                    CarryheadButton.setText("Stop");
                    CarryheadButton.setBackground(Color.green);
                }

            }
        });
    }

    public void setStartButtonAction() {
        this.passiveScanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (burp.config.PassiveScan) {
                    burp.config.PassiveScan = false;
                    passiveScanButton.setText("PassiveStart");
                    passiveScanButton.setBackground(null);
                } else {
                    burp.config.PassiveScan = true;
                    passiveScanButton.setText("Stop");
                    passiveScanButton.setBackground(Color.green);
                }

            }
        });
    }

    public void setIntSpinnerAction() {
        this.intSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Object value = intSpinner.getValue();
                if (value == null || !(value instanceof Number)) {
                    intSpinner.setValue(10);
                }else {
                    burp.config.RequestThread = (int) value;
                    new Thread(() -> burp.launchRequest.updateThreadPoolSize()).start();
//                    burp.launchRequest.updateThreadPoolSize();
                }
            }
        });
    }

    public void setRequestMethodAction() {
        this.requestMethod.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    burp.config.RequestMethod = (String)event.getItem();
                }
            }
        });
    }

    public void setPassiveScanPathAction() {
        this.passiveScanPathField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            public void changed() {
                burp.config.PassiveScanPath = passiveScanPathField.getText();
            }
        });
    }

    public void addEmptyRow() {
        this.panel.add(new JPanel());
    }

    @Override
    public Component getTab() {
        return this.panel;
    }
}
