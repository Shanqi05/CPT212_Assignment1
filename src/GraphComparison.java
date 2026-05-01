import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class GraphComparison {
    public static void main(String[] args) throws Exception {
        // Updated nValues to match the style of n=1 to 100
        int[] nValues = {1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        long[] simple = new long[nValues.length];
        long[] karatsuba = new long[nValues.length];

        for (int i = 0; i < nValues.length; i++) {
            var data = DataGenerator.generate(nValues[i]);
            SimpleMultiplication.multiply(data[0], data[1]);
            simple[i] = SimpleMultiplication.simpleCount;
            Karatsuba.karatsubaCount = 0;
            Karatsuba.multiply(data[0], data[1]);
            karatsuba[i] = Karatsuba.karatsubaCount;
        }
        drawGraph(nValues, simple, karatsuba, "comparison.png");
        System.out.println("comparison.png generated with labels.");
    }

    public static void drawGraph(int[] n, long[] s, long[] k, String file) throws Exception {
        int w = 900, h = 700;
        int padding = 100;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        // Calculate Max for Y Axis scaling
        long maxVal = 0;
        for (long v : s) if (v > maxVal) maxVal = v;
        for (long v : k) if (v > maxVal) maxVal = v;
        maxVal = (maxVal / 10000 + 1) * 10000; // Round up for cleaner axis

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Graph of Total Number of Primitive Operations against Number of n(digits)", 150, 50);

        // Draw Axes
        g.drawLine(padding, h - padding, w - 50, h - padding); // X-axis
        g.drawLine(padding, h - padding, padding, 80);        // Y-axis

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        // Y-axis numbers and grid lines
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            int y = (h - padding) - (i * (h - 2 * padding) / divisions);
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(padding, y, w - 50, y);
            g.setColor(Color.BLACK);
            g.drawString("" + (maxVal * i / divisions), 30, y + 5);
        }

        // X-axis numbers[cite: 1]
        for (int i = 0; i < n.length; i++) {
            int x = padding + (i * (w - padding - 100) / (n.length - 1));
            g.drawString("" + n[i], x - 5, h - padding + 25);
        }

        // Labels[cite: 1]
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Number of n(digits)", w / 2 - 50, h - 40);

        // Draw Lines[cite: 1]
        for (int i = 0; i < n.length - 1; i++) {
            int x1 = padding + (i * (w - padding - 100) / (n.length - 1));
            int x2 = padding + ((i + 1) * (w - padding - 100) / (n.length - 1));

            int y1s = (h - padding) - (int)(s[i] * (h - 2 * padding) / maxVal);
            int y2s = (h - padding) - (int)(s[i+1] * (h - 2 * padding) / maxVal);
            int y1k = (h - padding) - (int)(k[i] * (h - 2 * padding) / maxVal);
            int y2k = (h - padding) - (int)(k[i+1] * (h - 2 * padding) / maxVal);

            g.setStroke(new BasicStroke(2));
            g.setColor(Color.RED);
            g.drawLine(x1, y1s, x2, y2s);
            g.setColor(Color.BLUE);
            g.drawLine(x1, y1k, x2, y2k);
        }

        // Legend[cite: 1]
        g.setColor(Color.RED); g.fillRect(padding, h - 20, 20, 10);
        g.setColor(Color.BLACK); g.drawString("Simple Multiplication", padding + 25, h - 10);
        g.setColor(Color.BLUE); g.fillRect(padding + 200, h - 20, 20, 10);
        g.setColor(Color.BLACK); g.drawString("Karatsuba", padding + 225, h - 10);

        g.dispose();
        ImageIO.write(img, "png", new File(file));
    }
}