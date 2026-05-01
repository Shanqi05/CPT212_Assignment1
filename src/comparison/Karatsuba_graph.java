package comparison;

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
        // Test sequence for smooth curve
        int[] nValues = {1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        long[] karatsubaOps = new long[nValues.length];

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   Karatsuba Algorithm Analysis (1 to 100 digits)║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println();
        System.out.printf("%-12s | %-20s\n", "n (Digits)", "Operations");
        System.out.println("-------------|--------------------");

        for (int i = 0; i < nValues.length; i++) {
            int n = nValues[i];
            BigInteger[] data = DataGenerator.generate(n);

            Karatsuba.counter.reset();
            Karatsuba.multiply(data[0], data[1]);

            karatsubaOps[i] = Karatsuba.counter.getTotalOperations();
            System.out.printf("%-12d | %20d\n", n, karatsubaOps[i]);
        }

        System.out.println("-------------|--------------------");
        System.out.println();

        // Save to CSV
        saveToCSV(nValues, karatsubaOps);

        // Generate the PNG plot
        drawGraph(nValues, karatsubaOps, "karatsuba_plot.png");
        System.out.println("✓ karatsuba_plot.png generated successfully.");
        System.out.println("✓ karatsuba_data.csv generated successfully.");
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
        maxVal = (maxVal / 50000 + 1) * 50000;

        // Main Title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Karatsuba Algorithm: Primitive Operations vs Number of Digits", 120, 50);

        // Draw X and Y Axes
        g.setStroke(new BasicStroke(2.5f));
        g.setColor(Color.BLACK);
        g.drawLine(padding, h - padding, w - 30, h - padding); // X-axis
        g.drawLine(padding, h - padding, padding, 80);        // Y-axis

        // Y-axis label (rotated) - "Primitive Operations"
        AffineTransform aff = new AffineTransform();
        aff.rotate(-Math.PI / 2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(aff);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Primitive Operations", -h/2 + 30, 20);
        g2d.setTransform(new AffineTransform());

        // Draw Y-axis markings and grid lines with proper scale
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            int y = (h - padding) - (i * (h - 2 * padding) / divisions);
            
            // Light grid lines
            g.setColor(new Color(240, 240, 240));
            g.drawLine(padding + 5, y, w - 30, y);
            
            // Y-axis scale values
            g.setColor(Color.BLACK);
            long scaleValue = (maxVal * i / divisions);
            String label;
            if (scaleValue >= 1000000) {
                label = String.format("%.1fM", scaleValue / 1000000.0);
            } else if (scaleValue >= 1000) {
                label = String.format("%.0fK", scaleValue / 1000.0);
            } else {
                label = String.valueOf(scaleValue);
            }
            g.drawString(label, padding - 60, y + 5);
        }

        // Draw X-axis labels - just numbers without parentheses
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i < n.length; i++) {
            int x = padding + (i * (w - padding - 100) / (n.length - 1));
            g.drawString(String.valueOf(n[i]), x - 10, h - padding + 25);
        }

        // X-axis label (bottom)
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Number of Digits", w / 2 - 70, h - 10);

        // Plot the Karatsuba curve (Blue line)
        g.setColor(new Color(30, 144, 255)); // Blue
        g.setStroke(new BasicStroke(3.5f));
        for (int i = 0; i < n.length - 1; i++) {
            int x1 = padding + (i * (w - padding - 100) / (n.length - 1));
            int x2 = padding + ((i + 1) * (w - padding - 100) / (n.length - 1));

            int y1 = (h - padding) - (int)(operations[i] * (h - 2 * padding) / maxVal);
            int y2 = (h - padding) - (int)(operations[i+1] * (h - 2 * padding) / maxVal);

            g.drawLine(x1, y1, x2, y2);
        }

        // Draw data points
        for (int i = 0; i < n.length; i++) {
            int x = padding + (i * (w - padding - 100) / (n.length - 1));
            int y = (h - padding) - (int)(operations[i] * (h - 2 * padding) / maxVal);
            g.setColor(new Color(30, 144, 255));
            g.fillOval(x - 5, y - 5, 10, 10);
        }

        // Legend with colored line
        g.setFont(new Font("Arial", Font.BOLD, 13));
        int legendX = w - 280;
        int legendY = 80;
        
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(30, 144, 255));
        g.drawLine(legendX, legendY, legendX + 25, legendY);
        g.setColor(Color.BLACK);
        g.drawString("Karatsuba Operation", legendX + 35, legendY + 5);

        g.dispose();
        ImageIO.write(img, "png", new File(file));
    }
}