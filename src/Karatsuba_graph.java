import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.math.BigInteger;

public class Karatsuba_graph {

    public static void main(String[] args) throws Exception {
        // Updated sequence to provide more data points for a smoother curve
        int[] nValues = {1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        long[] karatsuba = new long[nValues.length];

        // CSV Header for Excel use
        System.out.println("n,actualoutput");

        for (int i = 0; i < nValues.length; i++) {
            int n = nValues[i];
            var data = DataGenerator.generate(n);

            Karatsuba.karatsubaCount = 0;
            Karatsuba.multiply(data[0], data[1]);

            karatsuba[i] = Karatsuba.karatsubaCount;
            // Print results for Excel CSV redirection
            System.out.println(n + "," + karatsuba[i]);
        }

        // Generate the PNG plot as requested
        drawGraph(nValues, karatsuba, "karatsuba_plot.png");
        System.out.println("\nkaratsuba_plot.png generated successfully.");
    }

    public static void drawGraph(int[] n, long[] k, String file) throws Exception {
        int w = 900, h = 700;
        int padding = 100;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        // High quality rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // White background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        // Find max value to scale the Y-axis
        long maxVal = 0;
        for (long v : k) if (v > maxVal) maxVal = v;
        maxVal = (maxVal / 10000 + 1) * 10000; // Round up for clean axis labels[cite: 1]

        // Main Title from Screenshot 2026-05-01 131409_2.png
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Graph of Total Number of Primitive Operations against Number of n(digits)", 120, 50);

        // Draw X and Y Axes
        g.setStroke(new BasicStroke(2));
        g.drawLine(padding, h - padding, w - 50, h - padding); // X-axis
        g.drawLine(padding, h - padding, padding, 80);        // Y-axis

        // Axis Labels
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Number of n(digits)", w / 2 - 50, h - 40);

        // Draw Y-axis markings and grid lines
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            int y = (h - padding) - (i * (h - 2 * padding) / divisions);
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(padding, y, w - 50, y); // Grid line
            g.setColor(Color.BLACK);
            g.drawString("" + (maxVal * i / divisions), 30, y + 5);
        }

        // Draw X-axis markings
        for (int i = 0; i < n.length; i++) {
            int x = padding + (i * (w - padding - 100) / (n.length - 1));
            g.drawString("" + n[i], x - 5, h - padding + 25);
        }

        // Plot the Karatsuba curve (Orange color to match screenshot)
        g.setColor(new Color(237, 125, 49)); // Orange color from Screenshot 2026-05-01 131409_2.png
        g.setStroke(new BasicStroke(3));
        for (int i = 0; i < n.length - 1; i++) {
            int x1 = padding + (i * (w - padding - 100) / (n.length - 1));
            int x2 = padding + ((i + 1) * (w - padding - 100) / (n.length - 1));

            int y1 = (h - padding) - (int)(k[i] * (h - 2 * padding) / maxVal);
            int y2 = (h - padding) - (int)(k[i+1] * (h - 2 * padding) / maxVal);

            g.drawLine(x1, y1, x2, y2);
        }

        // Legend at the bottom
        g.setColor(new Color(237, 125, 49));
        g.fillRect(w / 2 - 60, h - 20, 30, 10);
        g.setColor(Color.BLACK);
        g.drawString("Primitive Operations", w / 2 - 20, h - 10);

        g.dispose();
        ImageIO.write(img, "png", new File(file));
    }
}