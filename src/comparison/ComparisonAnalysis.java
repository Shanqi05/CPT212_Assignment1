package comparison;

import java.math.BigInteger;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import common.DataGenerator;
import simple.SimpleMultiplication;
import karatsuba.Karatsuba;

/**
 * ComparisonAnalysis: Comprehensive analysis of Simple Multiplication vs Karatsuba
 * 
 * This class performs the following:
 * 1. Runs both algorithms on n-digit numbers (n = 1 to 100)
 * 2. Records primitive operation counts
 * 3. Generates CSV output for spreadsheet analysis
 * 4. Displays comparative analysis
 * 5. Compares theoretical vs actual complexity
 */
public class ComparisonAnalysis {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  Algorithm Comparison: Simple Multiplication vs Karatsuba      ║");
        System.out.println("║  CPT212 Part 2 - Algorithm Analysis and Comparison             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Define test sizes
        int[] nValues = {1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        long[] simpleOps = new long[nValues.length];
        long[] karatsubaOps = new long[nValues.length];

        // Run both algorithms for each n and record operation counts
        System.out.println("Running algorithms for comparison...");
        System.out.println();
        
        for (int i = 0; i < nValues.length; i++) {
            int n = nValues[i];
            
            // Generate test data
            BigInteger[] data = DataGenerator.generate(n);
            
            // Run Simple Multiplication
            SimpleMultiplication.multiply(data[0], data[1]);
            simpleOps[i] = SimpleMultiplication.counter.getTotalOperations();
            
            // Run Karatsuba
            Karatsuba.counter.reset();
            Karatsuba.multiply(data[0], data[1]);
            karatsubaOps[i] = Karatsuba.counter.getTotalOperations();
        }

        // Display comparison table
        displayComparisonTable(nValues, simpleOps, karatsubaOps);

        // Save results to CSV
        saveToCSV(nValues, simpleOps, karatsubaOps);

        // Perform complexity analysis
        performComplexityAnalysis(nValues, simpleOps, karatsubaOps);

        // Display detailed analysis
        displayDetailedAnalysis(nValues, simpleOps, karatsubaOps);

        System.out.println("\n✓ Analysis complete! Results saved to 'comparison_results.csv'");
    }

    /**
     * Display comparison table
     */
    private static void displayComparisonTable(int[] n, long[] simple, long[] karatsuba) {
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ PRIMITIVE OPERATIONS COUNT COMPARISON                            │");
        System.out.println("├──────────┬───────────────────┬──────────────────┬────────────────┤");
        System.out.println("│  n       │   Simple Mult.    │   Karatsuba      │   Ratio (K/S)  │");
        System.out.println("├──────────┼───────────────────┼──────────────────┼────────────────┤");

        for (int i = 0; i < n.length; i++) {
            double ratio = karatsuba[i] > 0 ? (double)karatsuba[i] / simple[i] : 0;
            System.out.printf("│ %8d │ %17d │ %16d │ %14.4f │\n", 
                n[i], simple[i], karatsuba[i], ratio);
        }

        System.out.println("└──────────┴───────────────────┴──────────────────┴────────────────┘");
        System.out.println();
    }

    /**
     * Save results to CSV file
     */
    private static void saveToCSV(int[] n, long[] simple, long[] karatsuba) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("comparison_results.csv"))) {
            writer.println("n,Simple_Multiplication,Karatsuba,Ratio_Karatsuba_to_Simple");
            
            for (int i = 0; i < n.length; i++) {
                double ratio = karatsuba[i] > 0 ? (double)karatsuba[i] / simple[i] : 0;
                writer.printf("%d,%d,%d,%.4f\n", n[i], simple[i], karatsuba[i], ratio);
            }
            
            System.out.println("✓ CSV file 'comparison_results.csv' generated");
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    /**
     * Perform and display complexity analysis
     */
    private static void performComplexityAnalysis(int[] n, long[] simple, long[] karatsuba) {
        System.out.println("\n┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ THEORETICAL VS EXPERIMENTAL COMPLEXITY ANALYSIS                  │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.println();

        System.out.println("SIMPLE MULTIPLICATION:");
        System.out.println("  Theoretical: O(n²)");
        System.out.println("  Analysis of growth rate:");
        
        for (int i = 1; i < n.length - 1; i++) {
            double ratio = n[i] / (double)n[i-1];
            double opsRatio = simple[i] / (double)simple[i-1];
            double theoreticalRatio = ratio * ratio;
            
            System.out.printf("    n: %d→%d (ratio: %.2f), ops: %.2f (theoretical n²: %.2f)\n",
                n[i-1], n[i], ratio, opsRatio, theoreticalRatio);
        }

        System.out.println("\nKARATSUBA ALGORITHM:");
        System.out.println("  Theoretical: O(n^1.585)");
        System.out.println("  Analysis of growth rate:");
        
        for (int i = 1; i < n.length - 1; i++) {
            double ratio = n[i] / (double)n[i-1];
            double opsRatio = karatsuba[i] / (double)karatsuba[i-1];
            double theoreticalRatio = Math.pow(ratio, 1.585);
            
            System.out.printf("    n: %d→%d (ratio: %.2f), ops: %.2f (theoretical n^1.585: %.2f)\n",
                n[i-1], n[i], ratio, opsRatio, theoreticalRatio);
        }
    }

    /**
     * Display detailed analysis with crossover point
     */
    private static void displayDetailedAnalysis(int[] n, long[] simple, long[] karatsuba) {
        System.out.println("\n┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│ DETAILED COMPARISON AND INSIGHTS                                │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        System.out.println();

        // Find crossover point
        int crossoverIdx = -1;
        for (int i = 0; i < n.length; i++) {
            if (karatsuba[i] < simple[i]) {
                crossoverIdx = i;
                break;
            }
        }

        if (crossoverIdx >= 0) {
            System.out.printf("Crossover Point: n ≈ %d digits\n", n[crossoverIdx]);
            System.out.printf("  At n=%d: Simple=%d, Karatsuba=%d\n", 
                n[crossoverIdx], simple[crossoverIdx], karatsuba[crossoverIdx]);
            if (crossoverIdx > 0) {
                System.out.printf("  At n=%d: Simple=%d, Karatsuba=%d\n", 
                    n[crossoverIdx-1], simple[crossoverIdx-1], karatsuba[crossoverIdx-1]);
            }
        } else {
            System.out.println("No crossover detected in tested range.");
            System.out.println("Simple Multiplication remains more efficient for all tested values.");
        }

        System.out.println();
        System.out.println("Summary:");
        
        long totalSimple = 0;
        long totalKaratsuba = 0;
        for (long val : simple) totalSimple += val;
        for (long val : karatsuba) totalKaratsuba += val;
        
        System.out.printf("  Total operations (all tests):\n");
        System.out.printf("    Simple Multiplication: %d\n", totalSimple);
        System.out.printf("    Karatsuba:             %d\n", totalKaratsuba);
        System.out.printf("    Difference:            %d (%.2f%% more for Karatsuba)\n", 
            totalKaratsuba - totalSimple,
            ((double)(totalKaratsuba - totalSimple) / totalSimple) * 100);

        System.out.println();
        System.out.println("Observations:");
        System.out.println("  1. Simple multiplication shows O(n²) growth pattern");
        System.out.println("  2. Karatsuba shows better asymptotic behavior for very large n");
        System.out.println("  3. Higher overhead in Karatsuba due to recursive calls and");
        System.out.println("     additional operations (divides, remainders, multiplications)");
        System.out.println("  4. Karatsuba becomes advantageous at larger digit counts");
        System.out.println();
    }
}
