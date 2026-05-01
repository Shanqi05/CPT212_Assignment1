package karatsuba;

import java.math.BigInteger;
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

    public static void main(String[] args) {
        System.out.println("Karatsuba Algorithm Analysis");
        System.out.println("============================");
        System.out.printf("%-15s | %-20s\n", "n (Digits)", "Total Operations");
        System.out.println("-----------------------------");

        for (int n = 1; n <= 100; n = (n == 1) ? 10 : n + 10) {
            BigInteger[] data = DataGenerator.generate(n);
            counter.reset();
            multiply(data[0], data[1]);
            System.out.printf("%-15d | %-20d\n", n, counter.getTotalOperations());
        }
        System.out.println("============================");
    }
}