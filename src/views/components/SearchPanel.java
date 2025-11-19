package views.components;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> searchType;
    private JButton searchButton;

    public SearchPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        searchField = new JTextField(20);
        searchType = new JComboBox<>(new String[]{
                "Todos", "Nombre", "Email", "ID", "Carrera", "Departamento"
        });
        searchButton = new JButton("🔍 Buscar");

        add(new JLabel("Buscar:"));
        add(searchField);
        add(new JLabel(" por "));
        add(searchType);
        add(searchButton);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JComboBox<String> getSearchType() {
        return searchType;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public String getSearchText() {
        return searchField.getText().trim();
    }

    public String getSearchTypeText() {
        return searchType.getSelectedItem().toString();
    }
}