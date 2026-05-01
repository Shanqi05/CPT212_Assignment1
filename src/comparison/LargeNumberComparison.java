package comparison;

import java.math.BigInteger;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import common.DataGenerator;
import simple.SimpleMultiplication;
import karatsuba.Karatsuba;

/**
 * LargeNumberComparison: Compares algorithms for extremely large numbers
 * Tests range: 0 to 1000 digits (increments of 100)
 */
public class LargeNumberComparison {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final int PADDING = 120;

    public static void main(String[] args) throws Exception {
        // Test range: 0, 100, 200, 300, ... 1000 digits
        int[] nValues = {10, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        long[] simpleOps = new long[nValues.length];
        long[] karatsubaOps = new long[nValues.length];

        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║   LARGE NUMBER COMPARISON (10 to 1000 digits)      ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.printf("%-12s | %-18s | %-18s | %-10s\n", 
            "n (Digits)", "Simple Mult.", "Karatsuba", "Ratio");
        System.out.println("-------------|-------------------|-------------------|----------");

        for (int i = 0; i < nValues.length; i++) {
            BigInteger[] data = DataGenerator.generate(nValues[i]);

            // Run Simple Multiplication
            SimpleMultiplication.multiply(data[0], data[1]);
            simpleOps[i] = SimpleMultiplication.counter.getTotalOperations();

            // Run Karatsuba
            Karatsuba.counter.reset();
            Karatsuba.multiply(data[0], data[1]);
            karatsubaOps[i] = Karatsuba.counter.getTotalOperations();

            double ratio = simpleOps[i] > 0 ? (double) karatsubaOps[i] / simpleOps[i] : 0;
            System.out.printf("%-12d | %17d | %17d | %10.4f\n",
                nValues[i], simpleOps[i], karatsubaOps[i], ratio);
        }

        System.out.println("-------------|-------------------|-------------------|----------");
        System.out.println();

        // Generate outputs
        saveToCSV(nValues, simpleOps, karatsubaOps);
        drawComparisonGraph(nValues, simpleOps, karatsubaOps, "large_comparison_graph.png");

        System.out.println("✓ CSV file 'large_comparison_results.csv' generated");
        System.out.println("✓ Graph saved as 'large_comparison_graph.png'");
    }

    /**
     * Save results to CSV file
     */
    private static void saveToCSV(int[] n, long[] simple, long[] karatsuba) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("large_comparison_results.csv"))) {
            writer.println("n,Simple_Multiplication,Karatsuba,Ratio_Karatsuba_to_Simple");

            for (int i = 0; i < n.length; i++) {
                double ratio = simple[i] > 0 ? (double) karatsuba[i] / simple[i] : 0;
                writer.printf("%d,%d,%d,%.4f\n", n[i], simple[i], karatsuba[i], ratio);
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    /**
     * Draw comparison graph for large numbers
     */
    public static void drawComparisonGraph(int[] n, long[] simple, long[] karatsuba, String filename) throws Exception {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // White background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Calculate scale
        long maxVal = 0;
        for (long v : simple) if (v > maxVal) maxVal = v;
        for (long v : karatsuba) if (v > maxVal) maxVal = v;
        maxVal = (maxVal / 1000000 + 1) * 1000000;

        // Title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Large Number Comparison: Primitive Operations Analysis", 130, 40);

        // Draw axes
        g.setStroke(new BasicStroke(2.5f));
        g.setColor(Color.BLACK);
        g.drawLine(PADDING, HEIGHT - PADDING, WIDTH - 30, HEIGHT - PADDING); // X-axis
        g.drawLine(PADDING, HEIGHT - PADDING, PADDING, 70); // Y-axis

        // Draw grid and Y-axis labels with proper scale
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            int y = (HEIGHT - PADDING) - (i * (HEIGHT - 2 * PADDING) / divisions);
            
            // Light grid lines
            g.setColor(new Color(240, 240, 240));
            g.drawLine(PADDING + 5, y, WIDTH - 30, y);
            
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
            g.drawString(label, PADDING - 60, y + 5);
        }

        // Y-axis label (rotated) - "Primitive Operations"
        AffineTransform aff = new AffineTransform();
        aff.rotate(-Math.PI / 2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(aff);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Primitive Operations", -HEIGHT / 2 + 30, 20);
        g2d.setTransform(new AffineTransform());

        // X-axis labels - just the numbers without parentheses
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int i = 0; i < n.length; i++) {
            int x = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));
            g.drawString(String.valueOf(n[i]), x - 15, HEIGHT - PADDING + 25);
        }

        // X-axis label (bottom)
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Number of Digits", WIDTH / 2 - 70, HEIGHT - 10);

        // Draw Simple Multiplication line (RED)
        g.setStroke(new BasicStroke(3.5f));
        g.setColor(new Color(220, 20, 60)); // Red
        for (int i = 0; i < n.length - 1; i++) {
            int x1 = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));
            int x2 = PADDING + ((i + 1) * (WIDTH - PADDING - 100) / (n.length - 1));
            int y1 = (HEIGHT - PADDING) - (int)(simple[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(simple[i + 1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw Karatsuba line (BLUE)
        g.setColor(new Color(30, 144, 255)); // Blue
        for (int i = 0; i < n.length - 1; i++) {
            int x1 = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));
            int x2 = PADDING + ((i + 1) * (WIDTH - PADDING - 100) / (n.length - 1));
            int y1 = (HEIGHT - PADDING) - (int)(karatsuba[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(karatsuba[i + 1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw data points
        for (int i = 0; i < n.length; i++) {
            int x = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));

            // Simple point (red)
            int ys = (HEIGHT - PADDING) - (int)(simple[i] * (HEIGHT - 2 * PADDING) / maxVal);
            g.setColor(new Color(220, 20, 60));
            g.fillOval(x - 5, ys - 5, 10, 10);

            // Karatsuba point (blue)
            int yk = (HEIGHT - PADDING) - (int)(karatsuba[i] * (HEIGHT - 2 * PADDING) / maxVal);
            g.setColor(new Color(30, 144, 255));
            g.fillOval(x - 5, yk - 5, 10, 10);
        }

        // Legend with colored lines
        g.setFont(new Font("Arial", Font.BOLD, 13));
        int legendX = WIDTH - 280;
        int legendY = 80;
        int lineLength = 25;
        int spacing = 35;

        // Red line for Simple Multiplication
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(220, 20, 60));
        g.drawLine(legendX, legendY, legendX + lineLength, legendY);
        g.setColor(Color.BLACK);
        g.drawString("Simple Multiplication", legendX + 35, legendY + 5);

        // Blue line for Karatsuba
        g.setColor(new Color(30, 144, 255));
        g.drawLine(legendX, legendY + spacing, legendX + lineLength, legendY + spacing);
        g.setColor(Color.BLACK);
        g.drawString("Karatsuba Operation", legendX + 35, legendY + spacing + 5);

        g.dispose();
        ImageIO.write(img, "png", new File(filename));
    }
}
