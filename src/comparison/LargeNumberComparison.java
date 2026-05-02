package comparison;

import java.math.BigInteger;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import common.DataGenerator;
import simple.SimpleMultiplication;
import karatsuba.Karatsuba;

/**
 * LargeNumberComparison: Compares algorithms for large numbers (0 to 10000 digits)
 */
public class LargeNumberComparison {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 700;
    private static final int PADDING = 120;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Large Number Comparison Analysis...\n");
        
        SimpleMultiplication.disableVerboseOutput = false;
        
        System.out.println("=========================================");
        System.out.println("         MANUAL MULTIPLICATION           ");
        System.out.println("=========================================");
        System.out.print("Enter the Top Number (Multiplicand) : ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String input1 = scanner.nextLine().trim();
        System.out.print("Enter the Bottom Number (Multiplier): ");
        String input2 = scanner.nextLine().trim();
        
        if (!input1.isEmpty() && !input2.isEmpty()) {
            BigInteger num1 = new BigInteger(input1);
            BigInteger num2 = new BigInteger(input2);
            
            System.out.println("\n--- Manual Step-by-Step Output ---");
            SimpleMultiplication.multiply(num1, num2);
            Karatsuba.multiply(num1, num2);
        }
        
        System.out.println("=========================================\n");
        System.out.println("--- AUTO-GENERATED STEP-BY-STEP (n=1 to n=10) ---\n");
        
        // Auto-generate for 1 to 10 digits
        for (int digits = 1; digits <= 10; digits++) {
            BigInteger[] data = DataGenerator.generate(digits);
            System.out.println("Auto-generated numbers with " + digits + " digits:");
            System.out.println("--- Simple Multiplication ---");
            SimpleMultiplication.multiply(data[0], data[1]);
            System.out.println("--- Karatsuba ---");
            Karatsuba.multiply(data[0], data[1]);
            System.out.println();
        }
        
        System.out.println("=========================================\n");
        System.out.println("Starting Automated Algorithm Analysis...\n");
        
        // Disable verbose output for automated analysis
        SimpleMultiplication.disableVerboseOutput = true;
        
        // Generate n values from 1 to 10000 (every digit)
        int[] nValues = new int[10000];
        for (int i = 0; i < 10000; i++) {
            nValues[i] = i + 1;
        }
        long[] simpleOps = new long[nValues.length];
        long[] karatsubaOps = new long[nValues.length];

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                   LARGE NUMBER COMPARISON (n=1-10000)                           ║");
        System.out.println("╠════════════════════╦═══════════════════════════╦═══════════════════════════╣");
        System.out.println("║   n (Digits)       ║  Simple Multiplication    ║   Karatsuba Operations    ║");
        System.out.println("╠════════════════════╬═══════════════════════════╬═══════════════════════════╣");
        
        for (int i = 0; i < nValues.length; i++) {
            BigInteger[] data = DataGenerator.generate(nValues[i]);

            // Run Simple Multiplication
            SimpleMultiplication.multiply(data[0], data[1]);
            simpleOps[i] = SimpleMultiplication.counter.getTotalOperations();

            // Run Karatsuba
            Karatsuba.counter.reset();
            Karatsuba.multiply(data[0], data[1]);
            karatsubaOps[i] = Karatsuba.counter.getTotalOperations();
            
            System.out.printf("║ %18d ║ %25d ║ %25d ║\n",
                nValues[i], simpleOps[i], karatsubaOps[i]);
        }

        System.out.println("╚════════════════════╩═══════════════════════════╩═══════════════════════════╝\n");

        // Generate outputs (use all data points for graphing)
        saveToCSV(nValues, simpleOps, karatsubaOps);
        drawComparisonGraph(nValues, simpleOps, karatsubaOps, "large_comparison_graph.png");

        System.out.println("✓ CSV file 'large_comparison_results.csv' generated");
        System.out.println("✓ Graph saved as 'large_comparison_graph.png'");
        scanner.close();
    }

    /**
     * Get output directory (src folder)
     */
    private static String getOutputDir() {
        String currentDir = System.getProperty("user.dir");
        if (currentDir.endsWith("bin")) {
            return currentDir.replace("\\bin", "").replace("/bin", "") + File.separator + "src";
        }
        return currentDir;
    }
    
    /**
     * Save results to CSV file
     */
    private static void saveToCSV(int[] n, long[] simple, long[] karatsuba) {
        try {
            String outputDir = getOutputDir();
            File outFile = new File(outputDir, "large_comparison_results.csv");
            PrintWriter writer = new PrintWriter(new FileWriter(outFile));
            writer.println("n,Simple_Multiplication,Karatsuba");

            for (int i = 0; i < n.length; i++) {
                writer.printf("%d,%d,%d\n", n[i], simple[i], karatsuba[i]);
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    /**
     * Draw comparison graph for large numbers
     */
    public static void drawComparisonGraph(int[] n, long[] simple, long[] karatsuba, String filename) throws Exception {
        String outputDir = getOutputDir();
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

        // Title - centered
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        String title = "Large Number Comparison: Primitive Operations Analysis";
        FontMetrics fm = g.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (WIDTH - titleWidth) / 2;
        g.drawString(title, titleX, 45);

        // Draw axes
        g.setStroke(new BasicStroke(2.5f));
        g.setColor(Color.BLACK);
        g.drawLine(PADDING, HEIGHT - PADDING, WIDTH - 30, HEIGHT - PADDING); // X-axis
        g.drawLine(PADDING, HEIGHT - PADDING, PADDING, 70); // Y-axis

        // Draw grid and Y-axis labels with proper scale
        g.setFont(new Font("Arial", Font.PLAIN, 13));
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

        // Y-axis label (rotated)
        AffineTransform aff = new AffineTransform();
        aff.rotate(-Math.PI / 2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(aff);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Primitive Operations", -HEIGHT / 2 + 30, 15);
        g2d.setTransform(new AffineTransform());

        // X-axis labels (0, 1000, 2000... 10000) - fixed scale from 0 to 10000
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        for (int scale = 0; scale <= 10000; scale += 1000) {
            // Map scale value (0-10000) to pixel position
            double position = scale / 10000.0;
            int x = PADDING + (int)(position * (WIDTH - PADDING - 100));
            
            // Draw light grid line
            g.setColor(new Color(240, 240, 240));
            g.drawLine(x, PADDING + 20, x, HEIGHT - PADDING);
            
            // Draw X-axis label
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(scale), x - 15, HEIGHT - PADDING + 25);
        }

        // X-axis label (bottom)
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Number of Digits", WIDTH / 2 - 80, HEIGHT - 10);

        // Draw Simple Multiplication line (RED)
        g.setStroke(new BasicStroke(3.5f));
        g.setColor(new Color(220, 20, 60)); // Red
        for (int i = 0; i < n.length - 1; i++) {
            double pos1 = n[i] / 10000.0;
            double pos2 = n[i + 1] / 10000.0;
            int x1 = PADDING + (int)(pos1 * (WIDTH - PADDING - 100));
            int x2 = PADDING + (int)(pos2 * (WIDTH - PADDING - 100));
            int y1 = (HEIGHT - PADDING) - (int)(simple[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(simple[i + 1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw Karatsuba line (BLUE)
        g.setColor(new Color(30, 144, 255)); // Blue
        g.setStroke(new BasicStroke(3.5f));
        for (int i = 0; i < n.length - 1; i++) {
            double pos1 = n[i] / 10000.0;
            double pos2 = n[i + 1] / 10000.0;
            int x1 = PADDING + (int)(pos1 * (WIDTH - PADDING - 100));
            int x2 = PADDING + (int)(pos2 * (WIDTH - PADDING - 100));
            int y1 = (HEIGHT - PADDING) - (int)(karatsuba[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(karatsuba[i + 1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
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
        ImageIO.write(img, "png", new File(outputDir, filename));
    }
}

