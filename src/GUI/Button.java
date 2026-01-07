package GUI;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {

    private final Color goldLight = new Color(245, 230, 160);
    private final Color goldDark  = new Color(190, 160, 80);
    private final Color goldHover = new Color(255, 250, 200);
    private final Color textDark  = new Color(60, 40, 20);

    public Button(String text) {
        super(text);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        setFont(new Font("Serif", Font.BOLD, 18));
        setForeground(textDark);
        setMargin(new Insets(5, 15, 5, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 20;

        ButtonModel model = getModel();
        Color startColor, endColor;

        if (model.isPressed()) {
            startColor = goldDark;
            endColor = goldLight;
        } else if (model.isRollover()) {
            startColor = goldHover;
            endColor = goldDark;
        } else {
            startColor = goldLight;
            endColor = goldDark;
        }

        GradientPaint goldGradient = new GradientPaint(0, 0, startColor, 0, h, endColor);
        g2.setPaint(goldGradient);
        g2.fillRoundRect(0, 0, w, h, arc, arc);

        g2.setColor(new Color(130, 100, 50));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

        if (!model.isPressed()) {
            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawRoundRect(2, 2, w - 5, h / 2, arc, arc);
        }


        super.paintComponent(g);
    }
}