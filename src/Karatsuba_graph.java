import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.math.BigInteger;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import common.DataGenerator;
import karatsuba.Karatsuba;

/**
 * Karatsuba_graph: Generates graph and CSV data for Karatsuba algorithm analysis
 */
public class Karatsuba_graph {

    public static void main(String[] args) throws Exception {
        // Test range: 2 to 10000 digits with detailed progression
        int[] nValues = {2, 50, 100, 200, 300, 400, 500, 600, 700, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
        long[] karatsubaOps = new long[nValues.length];

        System.out.println("Karatsuba Algorithm Analysis");
        System.out.println("============================");
        System.out.println("n,Operations");

        for (int i = 0; i < nValues.length; i++) {
            int n = nValues[i];
            BigInteger[] data = DataGenerator.generate(n);

            Karatsuba.counter.reset();
            Karatsuba.multiply(data[0], data[1]);

            karatsubaOps[i] = Karatsuba.counter.getTotalOperations();
            System.out.println(n + "," + karatsubaOps[i]);
        }

        // Save to CSV
        saveToCSV(nValues, karatsubaOps);

        // Generate the PNG plot
        drawGraph(nValues, karatsubaOps, "karatsuba_plot.png");
        System.out.println("\n✓ karatsuba_plot.png generated successfully.");
    }

    /**
     * Save Karatsuba data to CSV file
     */
    private static void saveToCSV(int[] n, long[] operations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("karatsuba_data.csv"))) {
            writer.println("n,Operations");
            for (int i = 0; i < n.length; i++) {
                writer.println(n[i] + "," + operations[i]);
            }
            System.out.println("✓ CSV file 'karatsuba_data.csv' generated");
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    /**
     * Draw Karatsuba algorithm graph
     */
    public static void drawGraph(int[] n, long[] operations, String file) throws Exception {
        int w = 900;
        int h = 700;
        int padding = 100;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        // High quality rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // White background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        // Find max value to scale the Y-axis
        long maxVal = 0;
        for (long v : operations) if (v > maxVal) maxVal = v;
        maxVal = (maxVal / 100000 + 1) * 100000;

        // Main Title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Karatsuba Algorithm: Primitive Operations (2 to 10000 digits)", 130, 50);

        // Draw X and Y Axes
        g.setStroke(new BasicStroke(2));
        g.drawLine(padding, h - padding, w - 50, h - padding); // X-axis
        g.drawLine(padding, h - padding, padding, 80);        // Y-axis

        // Axis Labels
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Number of Digits (n)", w / 2 - 70, h - 40);

        // Y-axis label (rotated)
        AffineTransform aff = new AffineTransform();
        aff.rotate(-Math.PI / 2);
        g.setTransform(aff);
        g.drawString("Primitive Operations", -h/2 - 30, 30);
        g.setTransform(new AffineTransform());

        // Draw Y-axis markings and grid lines
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            int y = (h - padding) - (i * (h - 2 * padding) / divisions);
            g.setColor(new Color(220, 220, 220));
            g.drawLine(padding, y, w - 50, y); // Grid line
            g.setColor(Color.BLACK);
            String label = String.format("%d", (maxVal * i / divisions) / 1000) + "K";
            g.drawString(label, 5, y + 6);  // Moved to left with better spacing
        }

        // Draw X-axis markings (0, 1000, 2000... 10000)
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        for (int scale = 0; scale <= 10000; scale += 1000) {
            double position = scale / 10000.0;
            int x = padding + (int)(position * (w - padding - 100));
            
            // Draw light grid line
            g.setColor(new Color(220, 220, 220));
            g.drawLine(x, padding + 20, x, h - padding);
            
            // Draw X-axis label
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(scale), x - 10, h - padding + 25);
        }

        // Plot the Karatsuba curve
        g.setColor(new Color(237, 125, 49)); // Orange color
        g.setStroke(new BasicStroke(3));
        for (int i = 0; i < n.length - 1; i++) {
            double pos1 = n[i] / 10000.0;
            double pos2 = n[i + 1] / 10000.0;
            int x1 = padding + (int)(pos1 * (w - padding - 100));
            int x2 = padding + (int)(pos2 * (w - padding - 100));

            int y1 = (h - padding) - (int)(operations[i] * (h - 2 * padding) / maxVal);
            int y2 = (h - padding) - (int)(operations[i+1] * (h - 2 * padding) / maxVal);

            g.drawLine(x1, y1, x2, y2);
        }

        // Draw data points
        for (int i = 0; i < n.length; i++) {
            double position = n[i] / 10000.0;
            int x = padding + (int)(position * (w - padding - 100));
            int y = (h - padding) - (int)(operations[i] * (h - 2 * padding) / maxVal);
            g.setColor(new Color(237, 125, 49));
            g.fillOval(x - 5, y - 5, 10, 10);
        }

        // Legend at the bottom
        g.setColor(new Color(237, 125, 49));
        g.fillRect(w / 2 - 60, h - 25, 15, 15);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Operations O(n^1.585)", w / 2 - 40, h - 12);

        // Add complexity note
        g.setFont(new Font("Arial", Font.ITALIC, 10));
        g.drawString("Theoretical: O(n^1.585) ≈ O(n^log₂3)", padding, h - 55);

        g.dispose();
        ImageIO.write(img, "png", new File(file));
    }
}