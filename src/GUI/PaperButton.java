package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class PaperButton extends JButton {
    private Color baseColor = new Color(139, 0, 0);
    private Font textFont = new Font("Serif", Font.BOLD, 18);

    public PaperButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Use actual component dimensions
        int w = getWidth();
        int h = getHeight();

        // Calculate the circle to fit within the actual bounds
        int size = Math.min(w, h) - 15;
        int x = (w - size) / 2;
        int y = (h - size) / 2;

        // Interaction shift
        if (getModel().isPressed()) {
            g2.translate(1, 1);
        }

        // 1. Draw organic shadow
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval(x + 4, y + 4, size, size);

        // 2. Draw outer seal with radial gradient
        RadialGradientPaint gradient = new RadialGradientPaint(
                new Point2D.Float(x + size * 0.4f, y + size * 0.4f),
                size * 0.6f,
                new float[]{0f, 0.8f, 1f},
                new Color[]{
                        baseColor.brighter(),
                        baseColor,
                        baseColor.darker()
                }
        );
        g2.setPaint(gradient);

        // Use an Area to create a slightly irregular "wax" shape
        Ellipse2D circle1 = new Ellipse2D.Double(x, y, size, size);
        Ellipse2D circle2 = new Ellipse2D.Double(x + 2, y - 1, size - 2, size + 2);
        Area waxArea = new Area(circle1);
        waxArea.add(new Area(circle2));
        g2.fill(waxArea);

        // 3. Draw embossed inner ring
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(0, 0, 0, 50));
        g2.drawOval(x + size / 5, y + size / 5, size * 3 / 5, size * 3 / 5);

        // 4. Draw text with stamped effect
        g2.setFont(textFont);
        FontMetrics fm = g2.getFontMetrics();
        int textX = (w - fm.stringWidth(getText())) / 2;
        int textY = (h + fm.getAscent()) / 2 - 2;

        // "Stamped" Shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(getText(), textX + 1, textY + 1);

        // Main gold/parchment text color
        g2.setColor(new Color(212, 175, 55));
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(120,120);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(120,120);
    }
}