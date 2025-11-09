package views.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTable extends JTable {

    public CustomTable() {
        setShowGrid(true);
        setGridColor(Color.LIGHT_GRAY);
        setRowHeight(25);
        setSelectionBackground(new Color(220, 240, 255));
        setSelectionForeground(Color.BLACK);
        setFont(new Font("Arial", Font.PLAIN, 12));

        // Centrar contenido de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, centerRenderer);

        // Header personalizado
        getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        getTableHeader().setBackground(new Color(70, 130, 180));
        getTableHeader().setForeground(Color.WHITE);
        getTableHeader().setPreferredSize(new Dimension(0, 30));
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Hacer la tabla no editable
    }
}