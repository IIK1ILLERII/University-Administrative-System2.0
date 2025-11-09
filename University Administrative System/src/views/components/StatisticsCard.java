package views.components;

import javax.swing.*;
import java.awt.*;

public class StatisticsCard extends JPanel {
    private String title;
    private String value;
    private String description;
    private Color color;

    public StatisticsCard(String title, String value, String description, Color color) {
        this.title = title;
        this.value = value;
        this.description = description;
        this.color = color;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setPreferredSize(new Dimension(200, 120));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(color);

        JLabel descLabel = new JLabel(description, JLabel.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(Color.GRAY);

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
        add(descLabel, BorderLayout.SOUTH);
    }

    public void updateValue(String newValue) {
        // Método para actualizar el valor dinámicamente
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }
}