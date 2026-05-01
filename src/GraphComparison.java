import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.math.BigInteger;

/**
 * GraphComparison: Generates visual comparison of Simple Multiplication vs Karatsuba
 * Creates a line graph showing operation count vs n-digit numbers
 */
public class GraphComparison {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final int PADDING = 120;
    
    public static void main(String[] args) throws Exception {
        // Test data points
        int[] nValues = {1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        long[] simpleOps = new long[nValues.length];
        long[] karatsubaOps = new long[nValues.length];

        System.out.println("Generating graph data...");
        
        // Collect data
        for (int i = 0; i < nValues.length; i++) {
            BigInteger[] data = DataGenerator.generate(nValues[i]);
            
            // Run Simple Multiplication
            SimpleMultiplication.multiply(data[0], data[1]);
            simpleOps[i] = SimpleMultiplication.counter.getTotalOperations();
            
            // Run Karatsuba
            Karatsuba.counter.reset();
            Karatsuba.multiply(data[0], data[1]);
            karatsubaOps[i] = Karatsuba.counter.getTotalOperations();
            
            System.out.printf("n=%3d: Simple=%10d, Karatsuba=%10d\n", 
                nValues[i], simpleOps[i], karatsubaOps[i]);
        }

        // Generate graphs
        drawComparisonGraph(nValues, simpleOps, karatsubaOps, "comparison_graph.png");
        System.out.println("✓ Graph saved as 'comparison_graph.png'");
    }

    /**
     * Draw comparison graph with both algorithms
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
        maxVal = (maxVal / 100000 + 1) * 100000;

        // Title
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Algorithm Comparison: Primitive Operations Analysis", 150, 40);

        // Draw axes
        g.setStroke(new BasicStroke(2));
        g.drawLine(PADDING, HEIGHT - PADDING, WIDTH - 50, HEIGHT - PADDING); // X-axis
        g.drawLine(PADDING, HEIGHT - PADDING, PADDING, 70);  // Y-axis

        // Draw grid and Y-axis labels
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        int divisions = 10;
        for (int i = 0; i <= divisions; i++) {
            int y = (HEIGHT - PADDING) - (i * (HEIGHT - 2 * PADDING) / divisions);
            g.setColor(new Color(220, 220, 220));
            g.drawLine(PADDING, y, WIDTH - 50, y);
            g.setColor(Color.BLACK);
            String label = String.format("%d", (maxVal * i / divisions) / 1000) + "K";
            g.drawString(label, 5, y + 6);  // Moved further left for better spacing
        }

        // X-axis labels
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        for (int i = 0; i < n.length; i++) {
            int x = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));
            g.drawString(String.valueOf(n[i]), x - 10, HEIGHT - PADDING + 30);
        }

        // Axis labels
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("Number of Digits (n)", WIDTH / 2 - 80, HEIGHT - 30);
        
        // Y-axis label (rotated)
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform aff = new AffineTransform();
        aff.rotate(-Math.PI / 2);
        g2d.setTransform(aff);
        g.drawString("Primitive Operations Count", -HEIGHT/2 - 50, 30);
        g2d.setTransform(new AffineTransform());

        // Draw Simple Multiplication line
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(220, 20, 60)); // Crimson
        for (int i = 0; i < n.length - 1; i++) {
            int x1 = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));
            int x2 = PADDING + ((i + 1) * (WIDTH - PADDING - 100) / (n.length - 1));
            int y1 = (HEIGHT - PADDING) - (int)(simple[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(simple[i+1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw Karatsuba line
        g.setColor(new Color(30, 144, 255)); // Dodger Blue
        for (int i = 0; i < n.length - 1; i++) {
            int x1 = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));
            int x2 = PADDING + ((i + 1) * (WIDTH - PADDING - 100) / (n.length - 1));
            int y1 = (HEIGHT - PADDING) - (int)(karatsuba[i] * (HEIGHT - 2 * PADDING) / maxVal);
            int y2 = (HEIGHT - PADDING) - (int)(karatsuba[i+1] * (HEIGHT - 2 * PADDING) / maxVal);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw data points
        for (int i = 0; i < n.length; i++) {
            int x = PADDING + (i * (WIDTH - PADDING - 100) / (n.length - 1));
            
            // Simple point
            int ys = (HEIGHT - PADDING) - (int)(simple[i] * (HEIGHT - 2 * PADDING) / maxVal);
            g.setColor(new Color(220, 20, 60));
            g.fillOval(x - 4, ys - 4, 8, 8);
            
            // Karatsuba point
            int yk = (HEIGHT - PADDING) - (int)(karatsuba[i] * (HEIGHT - 2 * PADDING) / maxVal);
            g.setColor(new Color(30, 144, 255));
            g.fillOval(x - 4, yk - 4, 8, 8);
        }

        // Legend
        g.setFont(new Font("Arial", Font.BOLD, 12));
        int legendY = 80;
        
        // Simple Multiplication legend
        g.setColor(new Color(220, 20, 60));
        g.fillRect(WIDTH - 250, legendY, 15, 15);
        g.setColor(Color.BLACK);
        g.drawString("Simple Multiplication O(n²)", WIDTH - 230, legendY + 12);
        
        // Karatsuba legend
        g.setColor(new Color(30, 144, 255));
        g.fillRect(WIDTH - 250, legendY + 30, 15, 15);
        g.setColor(Color.BLACK);
        g.drawString("Karatsuba O(n^1.585)", WIDTH - 230, legendY + 42);

        g.dispose();
        ImageIO.write(img, "png", new File(filename));
    }
}