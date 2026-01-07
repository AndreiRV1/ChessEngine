package GUI;

import javax.swing.*;
import java.awt.*;

public class LeatherPatchPanel extends JPanel {

    public LeatherPatchPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 20;

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(3, 3, w - 6, h - 6, arc, arc);

        GradientPaint leather = new GradientPaint(
                0, 0, new Color(79, 53, 26),
                0, h, new Color(56, 29, 8));
        g2.setPaint(leather);
        g2.fillRoundRect(0, 0, w - 3, h - 3, arc, arc);

        Stroke stitchStroke = new BasicStroke(
                2.0f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                1.0f,
                new float[]{6.0f, 6.0f},
                0.0f
        );
        g2.setStroke(stitchStroke);
        g2.setColor(new Color(160, 130, 100));
        g2.drawRoundRect(5, 5, w - 13, h - 13, arc, arc);

        g2.dispose();
    }
}