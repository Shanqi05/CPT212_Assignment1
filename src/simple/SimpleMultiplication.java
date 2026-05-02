package simple;

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
 * SimpleMultiplication: Grade school multiplication algorithm
 * Time Complexity: O(n²) where n is the number of digits
 */
public class SimpleMultiplication {
    public static OperationCounter counter = new OperationCounter();
    public static boolean disableVerboseOutput = false;

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

        boolean printSteps = !disableVerboseOutput && (m <= 10 && n <= 10);
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
            
            //int carry = 0;
            //counter.recordAssignment(1);

            int[] currentPartials = new int[m];
            int[] currentCarriers = new int[m];
            
            for (int i = m - 1; i >= 0; i--) {
                counter.recordComparison(1); 
                counter.recordAssignment(1); 
                
                int dx = sx.charAt(i) - '0';
                counter.recordArrayAccess(1);
                counter.recordAssignment(1);
                counter.recordSubtraction(1);
                
                int prod = (dx * dy);
                counter.recordMultiplication(1);
                //counter.recordAddition(1);
                counter.recordAssignment(1);
                
                currentPartials[i] = prod % 10;
                //carry = prod / 10;
                //currentCarriers[i] = carry;
                currentCarriers[i] = prod / 10;
                
                counter.recordArrayAccess(2);
                counter.recordModulo(1);
                counter.recordDivision(1);
                counter.recordAssignment(2);

                result[i + j + 1] += currentPartials[i];
                result[i + j] += currentCarriers[i];

                counter.recordArrayAccess(4);
                counter.recordAddition(2);
                counter.recordAssignment(2);
            }
            
            //result[j] += carry;
            //counter.recordArrayAccess(2); 
            //counter.recordAddition(1);
            //counter.recordAssignment(1);

            if (printSteps) {
                StringBuilder pStr = new StringBuilder();
                StringBuilder cStr = new StringBuilder();
                for (int p : currentPartials) pStr.append(p);
                for (int c : currentCarriers) cStr.append(c);
                
                int shift = (n - 1) - j;
                
                String pRow = String.format("%" + (W - shift) + "s", pStr.toString()) + " ".repeat(shift);
                String cRow = String.format("%" + (W - shift - 1) + "s", cStr.toString()) + " ".repeat(shift + 1);
                
                System.out.printf("%s partial products for (=%s x %d)\n", pRow, sx, dy);
                System.out.printf("%s carriers for (%s x %d)\n", cRow, sx, dy);
            }
        }
        
        int finalCarry = 0;
        counter.recordAssignment(1);
        for (int k = result.length - 1; k >= 0; k--) {
            counter.recordComparison(1);
            counter.recordAssignment(1);

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
            counter.recordAssignment(1);

            counter.recordComparison(2);
            if (leadingZero && digit == 0) continue;

            leadingZero = false;
            counter.recordAssignment(1);

            sb.append(digit);
            counter.recordAddition(1);
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
        java.util.Scanner scanner = new java.util.Scanner(System.in);

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
            disableVerboseOutput = false;
            multiply(num1, num2);
            disableVerboseOutput = true;
        }
        
        System.out.println("=========================================\n");
        System.out.println("--- AUTO-GENERATED STEP-BY-STEP (n=1 to n=10) ---\n");
        
        // Auto-generate for 1 to 10 digits
        for (int digits = 1; digits <= 10; digits++) {
            BigInteger[] data = DataGenerator.generate(digits);
            System.out.println("Auto-generated numbers with " + digits + " digits:");
            System.out.println("--- Step-by-Step Output ---");
            disableVerboseOutput = false;
            multiply(data[0], data[1]);
            disableVerboseOutput = true;
            System.out.println();
        }
        
        System.out.println("=========================================\n");

        System.out.print("Do you want to run the automated algorithm analysis (n=1 to 10000)? (yes/no): ");
        String runAnalysis = scanner.nextLine().trim().toLowerCase();

        if (runAnalysis.equals("yes") || runAnalysis.equals("y")) {
            System.out.println("Starting Automated Algorithm Analysis...\n");

            disableVerboseOutput = true;

            int[] nValues = new int[10000];
            for (int i = 0; i < 10000; i++) {
                nValues[i] = i + 1;
            }
        
            long[] simpleOps = new long[nValues.length];

            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║     Simple Multiplication Algorithm Analysis (n=1-10000)    ║");
            System.out.println("╠════════════════════╦═══════════════════════════════════════╣");
            System.out.println("║   n (Digits)       ║      Total Operations               ║");
            System.out.println("╠════════════════════╬═══════════════════════════════════════╣");
        
            for (int i = 0; i < nValues.length; i++) {
                BigInteger[] data = DataGenerator.generate(nValues[i]);
                multiply(data[0], data[1]);
                simpleOps[i] = counter.getTotalOperations();
                System.out.printf("║ %18d ║ %35d ║\n", nValues[i], simpleOps[i]);
            }
        
            System.out.println("╚════════════════════╩═══════════════════════════════════════╝\n");
        
            saveToCSV(nValues, simpleOps);
            drawSimpleMultiplicationGraph(nValues, simpleOps, "simple_multiplication_graph.png");
            System.out.println("✓ CSV file 'simple_multiplication_results.csv' generated");
            System.out.println("✓ Graph saved as 'simple_multiplication_graph.png'");
        } else{
            System.out.println("\nSkipping Automated Algorithm Analysis. Program finished.");
        }
            scanner.close();
    }

    
    private static String getOutputDir() {
        String currentDir = System.getProperty("user.dir");
        if (currentDir.endsWith("bin")) {
            return currentDir.replace("\\bin", "").replace("/bin", "") + File.separator + "src";
        }
        return currentDir;
    }
    
    private static void saveToCSV(int[] n, long[] operations) {
        try {
            String outputDir = getOutputDir();
            File outFile = new File(outputDir, "simple_multiplication_results.csv");
            PrintWriter writer = new PrintWriter(new FileWriter(outFile));
            writer.println("n,Simple_Multiplication");

            for (int i = 0; i < n.length; i++) {
                writer.printf("%d,%d\n", n[i], operations[i]);
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }
    
    public static void drawSimpleMultiplicationGraph(int[] n, long[] ops, String filename) throws Exception {
        final int WIDTH = 1200;
        final int HEIGHT = 700;
        final int PADDING = 120;
        String outputDir = getOutputDir();
        
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        long maxVal = 0;
        for (long v : ops) if (v > maxVal) maxVal = v;
        maxVal = (maxVal / 1000000 + 1) * 1000000;

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        String title = "Simple Multiplication Algorithm Analysis";
        FontMetrics fm = g.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (WIDTH - titleWidth) / 2;
        g.drawString(title, titleX, 45);

        g.setStroke(new BasicStroke(2.5f));
        g.setColor(Color.BLACK);
        g.drawLine(PADDING, HEIGHT - PADDING, WIDTH - 30, HEIGHT - PADDING);
        g.drawLine(PADDING, HEIGHT - PADDING, PADDING, 70);

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

        AffineTransform aff = new AffineTransform();
        aff.rotate(-Math.PI / 2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(aff);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Primitive Operations", -HEIGHT / 2 + 30, 15);
        g2d.setTransform(new AffineTransform());

        g.setFont(new Font("Arial", Font.PLAIN, 13));
        for (int scale = 0; scale <= 10000; scale += 1000) {
            double position = scale / 10000.0;
            int x = PADDING + (int)(position * (WIDTH - PADDING - 100));
            
            g.setColor(new Color(240, 240, 240));
            g.drawLine(x, PADDING + 20, x, HEIGHT - PADDING);
            
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(scale), x - 15, HEIGHT - PADDING + 25);
        }

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Number of Digits", WIDTH / 2 - 80, HEIGHT - 10);

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

        g.setFont(new Font("Arial", Font.BOLD, 13));
        int legendX = WIDTH - 280;
        int legendY = 80;
        int lineLength = 25;

        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(220, 20, 60));
        g.drawLine(legendX, legendY, legendX + lineLength, legendY);
        g.setColor(Color.BLACK);
        g.drawString("Simple Multiplication", legendX + 35, legendY + 5);

        g.dispose();
        ImageIO.write(img, "png", new File(outputDir, filename));
    }
}
