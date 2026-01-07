package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ComboBox<E> extends JComboBox<E> {
    public ComboBox() {
        super();
        setOpaque(false);
        setBackground(Color.decode("#f1e1c0"));
        setForeground(new Color(74, 74, 74));
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setBorder(BorderFactory.createLineBorder(new Color(92, 64, 51), 3));

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setBorder(new EmptyBorder(8, 10, 8, 10));
                if (isSelected) {
                    setBackground(Color.decode("#f1e1c0"));
                    setForeground(new Color(159, 76, 17));
                } else {
                    setBackground(Color.decode("#f1e1c0"));
                    setForeground(new Color(100, 100, 100));

                }
                return this;
            }
        });

        // 3. Remove the default "Arrow" look
        setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setContentAreaFilled(false);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setOpaque(false);
                return button;
            }
        });
    }
}