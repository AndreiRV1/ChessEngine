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
        setForeground(new Color(150,112,89));
        setCaretColor(accentColor);
        setOpaque(false);
    }
}
