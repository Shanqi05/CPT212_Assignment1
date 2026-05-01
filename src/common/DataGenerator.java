package common;
import java.math.BigInteger;
import java.util.Random;

/**
 * DataGenerator: Generates random n-digit numbers for algorithm testing
 */
public class DataGenerator {
    private static Random random = new Random();

    /**
     * Generate two random n-digit numbers
     * Each digit is in the range [1, 9] to ensure exactly n digits
     * @param n number of digits
     * @return array containing two n-digit BigIntegers
     */
    public static BigInteger[] generate(int n) {
        if (n == 0) {
            return new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};
        }

        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        // Generate n random digits for each number (1-9 to ensure n digits)
        for (int i = 0; i < n; i++) {
            s1.append(random.nextInt(9) + 1);
            s2.append(random.nextInt(9) + 1);
        }

        return new BigInteger[]{
                new BigInteger(s1.toString()),
                new BigInteger(s2.toString())
        };
    }

    /**
     * Generate two n-digit numbers with a specific seed (for reproducibility)
     * @param n number of digits
     * @param seed random seed
     * @return array containing two n-digit BigIntegers
     */
    public static BigInteger[] generateWithSeed(int n, long seed) {
        Random r = new Random(seed);
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        for (int i = 0; i < n; i++) {
            s1.append(r.nextInt(9) + 1);
            s2.append(r.nextInt(9) + 1);
        }

        return new BigInteger[]{
                new BigInteger(s1.toString()),
                new BigInteger(s2.toString())
        };
    }
}