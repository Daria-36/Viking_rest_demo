package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final JTable vikingTable = new JTable(tableModel);

    public VikingDesktopFrame(VikingService vikingService) {
        this.vikingService = vikingService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JButton deleteButton = new JButton("Delete selected viking");
        deleteButton.addActionListener(event -> onDeleteSelectedViking());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void onDeleteSelectedViking() {
        int selectedRow = vikingTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Сначала выберите викинга в таблице");
            return;
        }

        int modelRow = vikingTable.convertRowIndexToModel(selectedRow);
        Viking viking = tableModel.getVikingAt(modelRow);

        if (viking.id() != null) {
            vikingService.deleteById(viking.id());
        }

        tableModel.removeVikingAt(modelRow);
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }

    public void updateViking(Viking viking) {
        tableModel.updateViking(viking);
    }

    public void deleteVikingById(int id) {
        tableModel.removeVikingById(id);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()) {
            for (Viking viking : all) {
                tableModel.addViking(viking);
            }
        }
    }
}

