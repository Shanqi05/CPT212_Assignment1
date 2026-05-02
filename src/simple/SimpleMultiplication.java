package simple;

import java.math.BigInteger;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Scanner;
import common.OperationCounter;
import common.DataGenerator;

/**
 * SimpleMultiplication: Grade school multiplication algorithm
 * Time Complexity: O(n²) where n is the number of digits
 */
public class SimpleMultiplication {
    public static OperationCounter counter = new OperationCounter();

    /**
     * Multiply two BigIntegers using simple multiplication algorithm
     * @param x first number (Multiplicand)
     * @param y second number (Multiplier)
     * @return product of x and y
     */
    public static BigInteger multiply(BigInteger x, BigInteger y) {
        counter.reset();
        
        String sx = x.toString();
        String sy = y.toString();
        counter.recordAssignment(2);
        
        int m = sx.length();
        int n = sy.length();
        counter.recordAssignment(2);
        
        int[] result = new int[m + n];
        counter.recordAssignment(1); 

        boolean printSteps = (m <= 10 && n <= 10);
        // Padding width for alignment: multiplicand length + multiplier length
        int W = m + n; 
        
        if (printSteps) {
            System.out.printf("%" + W + "s\n", sx);
            System.out.printf("x%" + (W - 1) + "s\n", sy);
            System.out.println("-".repeat(W + 40));
        }
        
        for (int j = n - 1; j >= 0; j--) {
            counter.recordComparison(1); 
            counter.recordAssignment(1); 
            
            int dy = sy.charAt(j) - '0';
            counter.recordArrayAccess(1); 
            counter.recordAssignment(2); 
            counter.recordSubtraction(1);
            
            int carry = 0;
            counter.recordAssignment(1);

            int[] currentPartials = new int[m];
            int[] currentCarriers = new int[m];
            
            for (int i = m - 1; i >= 0; i--) {
                counter.recordComparison(1); 
                counter.recordAssignment(1); 
                
                int dx = sx.charAt(i) - '0';
                counter.recordArrayAccess(1);
                counter.recordAssignment(1);
                counter.recordSubtraction(1);
                
                int prod = (dx * dy) + carry;
                counter.recordMultiplication(1);
                counter.recordAddition(1);
                counter.recordAssignment(1);
                
                currentPartials[i] = prod % 10;
                carry = prod / 10;
                currentCarriers[i] = carry;

                // Step 2 logic: accumulate in result array
                result[i + j + 1] += currentPartials[i];
                
                counter.recordArrayAccess(1);
                counter.recordModulo(1);
                counter.recordDivision(1);
                counter.recordAssignment(2);
            }
            
            result[j] += carry;
            counter.recordArrayAccess(2); 
            counter.recordAddition(1);
            counter.recordAssignment(1);

            if (printSteps) {
                StringBuilder pStr = new StringBuilder();
                StringBuilder cStr = new StringBuilder();
                for (int p : currentPartials) pStr.append(p);
                for (int c : currentCarriers) cStr.append(c);
                
                int shift = (n - 1) - j;
                
                // Bulletproof string padding (avoids the %0s format error)
                String pRow = String.format("%" + (W - shift) + "s", pStr.toString()) + " ".repeat(shift);
                String cRow = String.format("%" + (W - shift - 1) + "s", cStr.toString()) + " ".repeat(shift + 1);
                
                // Print exactly to match the rubric's visual flow
                System.out.printf("%s partial products for (=%s x %d)\n", pRow, sx, dy);
                System.out.printf("%s carriers for (%s x %d)\n", cRow, sx, dy);
            }
        }
        
        // Final carry handling for result array
        int finalCarry = 0;
        counter.recordAssignment(1);
        for (int k = result.length - 1; k >= 0; k--) {
            counter.recordComparison(1);
            counter.recordAssignment(1); // For the k--

            int val = result[k] + finalCarry;
            counter.recordArrayAccess(1);
            counter.recordAddition(1);
            counter.recordAssignment(1);

            result[k] = val % 10;
            counter.recordModulo(1);
            counter.recordArrayAccess(1);
            counter.recordAssignment(1);

            finalCarry = val / 10;
            counter.recordDivision(1);
            counter.recordAssignment(1);
        }
        
        StringBuilder sb = new StringBuilder();
        counter.recordAssignment(1);

        boolean leadingZero = true;
        counter.recordAssignment(1);

        for (int digit : result) {
            counter.recordArrayAccess(1); 
            counter.recordAssignment(1); // For the implicit array iteration

            counter.recordComparison(2); // For the boolean check (leadingZero && digit == 0)
            if (leadingZero && digit == 0) continue;

            leadingZero = false;
            counter.recordAssignment(1);

            sb.append(digit);
            counter.recordAddition(1); // Treating string append as addition
            counter.recordAssignment(1);
        }
        
        String res = sb.length() == 0 ? "0" : sb.toString();
        counter.recordComparison(1);
        counter.recordAssignment(1);
        
        if (printSteps) {
            System.out.println("+ " + "-".repeat(W + 38));
            System.out.printf("%" + W + "s\n\n", res);
        }
        
        return new BigInteger(res);
    }
    
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("         MANUAL MULTIPLICATION           ");
        System.out.println("=========================================");
        System.out.print("Enter the Top Number (Multiplicand) : ");
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
        System.out.println("Starting Automated Algorithm Analysis...");

        int[] nValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 50, 100, 200, 300, 400, 500, 600, 700, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
        long[] simpleOps = new long[nValues.length];

        System.out.println("--- STEP-BY-STEP VERIFICATION (n = 1 to 10) ---");
        
        // 1. Calculate everything and print the step-by-step BEFORE the table
        for (int i = 0; i < nValues.length; i++) {
            BigInteger[] data = DataGenerator.generate(nValues[i]);

            if (nValues[i] <= 10) {
                System.out.println("Testing n=" + nValues[i] + ": " + data[0] + " x " + data[1]);
            }

            multiply(data[0], data[1]);
            simpleOps[i] = counter.getTotalOperations();
        }
        
        // 2. Now print the clean, uninterrupted table
        System.out.println("Simple Multiplication Algorithm Analysis");
        System.out.println("=========================================");
        System.out.printf("%-15s | %-20s\n", "n (Digits)", "Total Operations");
        System.out.println("-----------------------------------------");
        
        for (int i = 0; i < nValues.length; i++) {
            System.out.printf("%-15d | %-20d\n", nValues[i], simpleOps[i]);
        }
        
        System.out.println("=========================================\n");
        
        // Generate outputs
        drawSimpleMultiplicationGraph(nValues, simpleOps, "simple_multiplication_graph.png");
        System.out.println("✓ Graph saved as 'simple_multiplication_graph.png'");

        scanner.close();
    }
    
    public static void drawSimpleMultiplicationGraph(int[] n, long[] ops, String filename) throws Exception {
        final int WIDTH = 1200;
        final int HEIGHT = 700;
        final int PADDING = 120;
        
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
        String title = "Simple Multiplication Algorithm Analysis";
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

        // X-axis label
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Number of Digits", WIDTH / 2 - 80, HEIGHT - 10);

        // Draw line
        g.setStroke(new BasicStroke(3.5f));
        g.setColor(new Color(220, 20, 60));
        for (int i = 0; i < n.length - 1; i++) {
            double pos1 = n[i] / 10000.0;
            double pos2 = n[i + 1] / 10000.0;
            int x1 = PADDING + (int)(pos1 * (WIDTH - PADDING - 100));
            int x2 = PADDING + (int)(pos2 * (WIDTH - PADDING - 100));
            int y1 = (HEIGHT - PADDING) - (int)(ops[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(ops[i + 1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw data points
        for (int i = 0; i < n.length; i++) {
            double position = n[i] / 10000.0;
            int x = PADDING + (int)(position * (WIDTH - PADDING - 100));
            int y = (HEIGHT - PADDING) - (int)(ops[i] * (HEIGHT - 2 * PADDING) / maxVal);
            g.setColor(new Color(220, 20, 60));
            g.fillOval(x - 5, y - 5, 10, 10);
        }

        // Legend with colored line
        g.setFont(new Font("Arial", Font.BOLD, 13));
        int legendX = WIDTH - 280;
        int legendY = 80;
        int lineLength = 25;

        // Red line for Simple Multiplication
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(220, 20, 60));
        g.drawLine(legendX, legendY, legendX + lineLength, legendY);
        g.setColor(Color.BLACK);
        g.drawString("Simple Multiplication", legendX + 35, legendY + 5);

        g.dispose();
        ImageIO.write(img, "png", new File(filename));
    }
}