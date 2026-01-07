package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RadioButton extends JRadioButton {
    private final Color brownBorder = new Color(92, 64, 51);
    private Color selectedBg = new Color(245, 241, 226);

    public RadioButton(String text){
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Serif", Font.ITALIC, 16));
        setForeground(new Color(40, 35, 30));
    }

    public void setSelectedBg(Color color){
        selectedBg = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (isSelected()) {
            g2.setColor(selectedBg);
            g2.fillRoundRect(2, 2, w - 5, h - 5, 5, 5);
            g2.setColor(brownBorder);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawRoundRect(2, 2, w - 5, h - 5, 5, 5);
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(92, 64, 51, 40));
            g2.drawRoundRect(2, 2, w - 5, h - 5, 5, 5);
        }

        FontMetrics fm = g2.getFontMetrics();
        int textX = (w - fm.stringWidth(getText())) / 2;
        int textY = (h + fm.getAscent()) / 2 - 2;

        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
