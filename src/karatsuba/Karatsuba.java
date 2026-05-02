package karatsuba;

import java.math.BigInteger;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import common.OperationCounter;
import common.DataGenerator;

/**
 * Karatsuba: Divide-and-conquer multiplication algorithm
 * Time Complexity: O(n^log₂3) ≈ O(n^1.585) where n is the number of digits
 * 
 * Based on: Karatsuba, A.; Ofman, Y. (1962). 
 * "Multiplication of multidigit numbers on automata"
 */
public class Karatsuba {
    public static OperationCounter counter = new OperationCounter();

    /**
     * Multiply two BigIntegers using Karatsuba algorithm
     * @param x first number
     * @param y second number
     * @return product of x and y
     */
    public static BigInteger multiply(BigInteger x, BigInteger y) {
        // Base case: if numbers are single digit
        counter.recordComparison(2); // Two comparisons
        if (x.compareTo(BigInteger.TEN) < 0 && y.compareTo(BigInteger.TEN) < 0) {
            counter.recordMultiplication(1);
            return x.multiply(y);
        }

        // Convert to strings and get lengths
        String sx = x.toString();
        String sy = y.toString();
        counter.recordAssignment(2);
        
        int n = Math.max(sx.length(), sy.length());
        counter.recordComparison(1); // max comparison
        counter.recordAssignment(1);
        
        int half = (n / 2) + (n % 2);
        counter.recordDivision(1);
        counter.recordModulo(1);
        counter.recordAddition(1);
        counter.recordAssignment(1);

        // Calculate power of 10 for splitting
        BigInteger power = BigInteger.TEN.pow(half);
        counter.recordAssignment(1);
        counter.recordMultiplication(1); // pow operation noted
        
        // Split x into a and b (x = a * 10^half + b)
        BigInteger a = x.divide(power);
        BigInteger b = x.remainder(power);
        counter.recordDivision(1);
        counter.recordDivision(1); // remainder is also a division
        counter.recordAssignment(2);
        
        // Split y into c and d (y = c * 10^half + d)
        BigInteger c = y.divide(power);
        BigInteger d = y.remainder(power);
        counter.recordDivision(2);
        counter.recordAssignment(2);

        // Three recursive multiplications (key insight of Karatsuba)
        // z0 = a * c
        // z1 = (a + b) * (c + d)
        // z2 = b * d
        BigInteger z0 = multiply(a, c);
        counter.recordAddition(1); // from recursive call tracking
        
        BigInteger z1 = multiply(a.add(b), c.add(d));
        counter.recordAddition(2); // a+b and c+d
        counter.recordAddition(1); // from recursive call
        
        BigInteger z2 = multiply(b, d);
        counter.recordAddition(1); // from recursive call
        
        // Calculate k = z1 - z0 - z2
        BigInteger k = z1.subtract(z0).subtract(z2);
        counter.recordSubtraction(2);
        counter.recordAssignment(1);
        
        // Result = z0 * 10^(2*half) + k * 10^half + z2
        BigInteger pow2 = BigInteger.TEN.pow(2 * half);
        counter.recordMultiplication(1);
        counter.recordAssignment(1);
        
        BigInteger res = z0.multiply(pow2)
                .add(k.multiply(power))
                .add(z2);
        counter.recordMultiplication(2);
        counter.recordAddition(2);
        counter.recordAssignment(1);

        return res;
    }

    public static void main(String[] args) throws Exception {
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
            multiply(num1, num2);
        }
        
        System.out.println("=========================================\n");
        System.out.println("--- AUTO-GENERATED STEP-BY-STEP (n=1 to n=10) ---\n");
        
        // Auto-generate for 1 to 10 digits
        for (int digits = 1; digits <= 10; digits++) {
            BigInteger[] data = DataGenerator.generate(digits);
            System.out.println("Auto-generated numbers with " + digits + " digits:");
            System.out.println("--- Step-by-Step Output ---");
            multiply(data[0], data[1]);
            System.out.println();
        }
        
        System.out.println("=========================================\n");
        System.out.println("Starting Automated Algorithm Analysis...\n");

        // Generate n values from 1 to 10000 (every digit)
        int[] nValues = new int[10000];
        for (int i = 0; i < 10000; i++) {
            nValues[i] = i + 1;
        }
        
        long[] karatsubaOps = new long[nValues.length];
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       Karatsuba Algorithm Analysis (n=1-10000)              ║");
        System.out.println("╠════════════════════╦═══════════════════════════════════════╣");
        System.out.println("║   n (Digits)       ║      Total Operations               ║");
        System.out.println("╠════════════════════╬═══════════════════════════════════════╣");
        
        for (int i = 0; i < nValues.length; i++) {
            BigInteger[] data = DataGenerator.generate(nValues[i]);
            counter.reset();
            multiply(data[0], data[1]);
            karatsubaOps[i] = counter.getTotalOperations();
            System.out.printf("║ %18d ║ %35d ║\n", nValues[i], karatsubaOps[i]);
        }
        
        System.out.println("╚════════════════════╩═══════════════════════════════════════╝\n");
        
        // Generate outputs (use all data points for graphing)
        saveToCSV(nValues, karatsubaOps);
        drawKaratsubaGraph(nValues, karatsubaOps, "karatsuba_graph.png");
        System.out.println("✓ CSV file 'karatsuba_results.csv' generated");
        System.out.println("✓ Graph saved as 'karatsuba_graph.png'");
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
    private static void saveToCSV(int[] n, long[] operations) {
        try {
            String outputDir = getOutputDir();
            File outFile = new File(outputDir, "karatsuba_results.csv");
            PrintWriter writer = new PrintWriter(new FileWriter(outFile));
            writer.println("n,Karatsuba");

            for (int i = 0; i < n.length; i++) {
                writer.printf("%d,%d\n", n[i], operations[i]);
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }
    
    public static void drawKaratsubaGraph(int[] n, long[] ops, String filename) throws Exception {
        final int WIDTH = 1200;
        final int HEIGHT = 700;
        final int PADDING = 120;
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
        for (long v : ops) if (v > maxVal) maxVal = v;
        maxVal = (maxVal / 1000000 + 1) * 1000000;

        // Title - centered
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        String title = "Karatsuba Algorithm Analysis";
        FontMetrics fm = g.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (WIDTH - titleWidth) / 2;
        g.drawString(title, titleX, 45);

        // Draw axes
        g.setStroke(new BasicStroke(2.5f));
        g.setColor(Color.BLACK);
        g.drawLine(PADDING, HEIGHT - PADDING, WIDTH - 30, HEIGHT - PADDING);
        g.drawLine(PADDING, HEIGHT - PADDING, PADDING, 70);

        // Draw grid and Y-axis labels
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            int y = (HEIGHT - PADDING) - (i * (HEIGHT - 2 * PADDING) / divisions);
            g.setColor(new Color(240, 240, 240));
            g.drawLine(PADDING + 5, y, WIDTH - 30, y);
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

        // Y-axis label
        AffineTransform aff = new AffineTransform();
        aff.rotate(-Math.PI / 2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(aff);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Primitive Operations", -HEIGHT / 2 + 30, 15);
        g2d.setTransform(new AffineTransform());

        // X-axis labels (0, 1000, 2000... 10000)
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        int maxDigits = 10000;
        for (int scale = 0; scale <= maxDigits; scale += 1000) {
            // Find position of this scale value (linear interpolation)
            // min n = 2, max n = 10000
            int minN = 2;
            int maxN = 10000;
            double position = ((double)(scale - minN) / (maxN - minN));
            int x = PADDING + (int)(position * (WIDTH - PADDING - 100));
            
            // Draw light grid line
            g.setColor(new Color(240, 240, 240));
            g.drawLine(x, PADDING + 20, x, HEIGHT - PADDING);
            
            // Draw X-axis label
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(scale), x - 15, HEIGHT - PADDING + 25);
        }

        // X-axis label
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Number of Digits", WIDTH / 2 - 80, HEIGHT - 10);

        // Draw line (no dots - line only)
        g.setStroke(new BasicStroke(3.5f));
        g.setColor(new Color(30, 144, 255));
        for (int i = 0; i < n.length - 1; i++) {
            double pos1 = n[i] / 10000.0;
            double pos2 = n[i + 1] / 10000.0;
            int x1 = PADDING + (int)(pos1 * (WIDTH - PADDING - 100));
            int x2 = PADDING + (int)(pos2 * (WIDTH - PADDING - 100));
            int y1 = (HEIGHT - PADDING) - (int)(ops[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(ops[i + 1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
        }

        // Legend with colored line
        g.setFont(new Font("Arial", Font.BOLD, 13));
        int legendX = WIDTH - 280;
        int legendY = 80;
        int lineLength = 25;

        // Blue line for Karatsuba
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(30, 144, 255));
        g.drawLine(legendX, legendY, legendX + lineLength, legendY);
        g.setColor(Color.BLACK);
        g.drawString("Karatsuba Operation", legendX + 35, legendY + 5);

        g.dispose();
        ImageIO.write(img, "png", new File(outputDir, filename));
    }
}