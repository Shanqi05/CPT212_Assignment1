import java.math.BigInteger;

public class Karatsuba {

    public static long karatsubaCount = 0;

    // The Karatsuba logic extracted from your lecture reference
    public static BigInteger multiply(BigInteger x, BigInteger y) {
        karatsubaCount++; // Method call
        karatsubaCount++; // Comparison

        if (x.compareTo(BigInteger.TEN) < 0 && y.compareTo(BigInteger.TEN) < 0) {
            karatsubaCount++; // Base case multiplication
            return x.multiply(y);
        }

        String sx = x.toString();
        String sy = y.toString();
        int n = Math.max(sx.length(), sy.length());
        int half = (n / 2) + (n % 2);
        karatsubaCount += 5; // Assignments and math for splitting[cite: 1]

        BigInteger power = BigInteger.TEN.pow(half);
        BigInteger a = x.divide(power);
        BigInteger b = x.remainder(power);
        BigInteger c = y.divide(power);
        BigInteger d = y.remainder(power);
        karatsubaCount += 6; // Pow + 4 div/rem + assignments[cite: 1]

        BigInteger z0 = multiply(a, c);
        BigInteger z1 = multiply(a.add(b), c.add(d));
        BigInteger z2 = multiply(b, d);
        karatsubaCount += 5; // Recursive call additions and assignments[cite: 1]

        // Karatsuba formula calculation[cite: 1]
        BigInteger res = z0.multiply(BigInteger.TEN.pow(half * 2))
                .add(z1.subtract(z0).subtract(z2).multiply(power))
                .add(z2);
        karatsubaCount += 8;

        return res;
    }

    // Adding the missing main method[cite: 1]
    public static void main(String[] args) {
        System.out.println("_____________________________________________");
        System.out.printf("| %-10s | %-25s |\n", "n (Digits)", "Primitive Operations");
        System.out.println("|____________|_______________________________|");

        // Loop for n = 1, 11, 21... 91[cite: 1]
        for (int n = 1; n <= 100; n = (n == 1) ? 11 : n + 10) {
            var data = DataGenerator.generate(n);

            karatsubaCount = 0; // Reset counter for each size n[cite: 1]
            multiply(data[0], data[1]);

            System.out.printf("| %-10d | %-25d |\n", n, karatsubaCount);
        }
        System.out.println("|____________|_______________________________|");
    }
}