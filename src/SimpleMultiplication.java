import java.math.BigInteger;

public class SimpleMultiplication {
    public static long simpleCount = 0;

    public static BigInteger multiply(BigInteger x, BigInteger y) {
        simpleCount = 0;
        String sx = x.toString();
        String sy = y.toString();
        int m = sx.length();
        int n = sy.length();
        int[] result = new int[m + n];
        simpleCount += 6; // String conversion, lengths, array init

        for (int i = m - 1; i >= 0; i--) {
            simpleCount++; // Outer loop condition
            int dx = sx.charAt(i) - '0';
            int carry = 0;
            simpleCount += 2;

            for (int j = n - 1; j >= 0; j--) {
                simpleCount++; // Inner loop condition
                int dy = sy.charAt(j) - '0';
                int sum = result[i + j + 1] + (dx * dy) + carry;
                result[i + j + 1] = sum % 10;
                carry = sum / 10;
                simpleCount += 5; // Multiplications, additions, modulo, division
            }
            result[i] += carry;
            simpleCount++; // Final carry assignment
        }

        // Convert array back to BigInteger for return
        StringBuilder sb = new StringBuilder();
        for (int digit : result) sb.append(digit);
        return new BigInteger(sb.toString());
    }
}