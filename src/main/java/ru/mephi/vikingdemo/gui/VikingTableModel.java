package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.Viking;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VikingTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Name", "Age", "Height (cm)", "Hair color", "Beard style", "Equipment"};
    private final List<Viking> data = new ArrayList<>();

    public void addViking(Viking viking) {
        int row = data.size();
        data.add(viking);
        fireTableRowsInserted(row, row);
    }

    public Viking getVikingAt(int row) {
        return data.get(row);
    }

    public void removeVikingAt(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }

    public void removeVikingById(int id) {
        int row = findRowById(id);

        if (row >= 0) {
            removeVikingAt(row);
        }
    }

    public void updateViking(Viking viking) {
        if (viking.id() == null) {
            return;
        }

        int row = findRowById(viking.id());

        if (row >= 0) {
            data.set(row, viking);
            fireTableRowsUpdated(row, row);
        }
    }

    private int findRowById(int id) {
        for (int i = 0; i < data.size(); i++) {
            Viking viking = data.get(i);

            if (viking.id() != null && viking.id() == id) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Viking viking = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> viking.id();
            case 1 -> viking.name();
            case 2 -> viking.age();
            case 3 -> viking.heightCm();
            case 4 -> viking.hairColor();
            case 5 -> viking.beardStyle();
            case 6 -> formatEquipment(viking.equipment());
            default -> "";
        };
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return "";
        }

        return equipment.stream()
                .map(item -> item.name() + " [" + item.quality() + "]")
                .collect(Collectors.joining(", "));
    }
}
