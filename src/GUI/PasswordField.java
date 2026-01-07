package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PasswordField extends JPasswordField {
    private Color backgroundColor = new Color(255, 255, 255);
    private Color borderColor = new Color(209, 209, 209);
    private Color accentColor = new Color(163, 177, 138);
    private int radius = 5;
    public PasswordField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setFont(new Font("SansSerif", Font.BOLD, 16));
        setForeground(new Color(74, 74, 74));
        setCaretColor(accentColor);
        setOpaque(false);
        setForeground(Color.decode("#1A0F00"));
    }


    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Change border color if focused
        g2.setColor(hasFocus() ? accentColor : borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.dispose();
    }
}
