package simple;

import java.math.BigInteger;
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
     * @param x first number
     * @param y second number
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
        counter.recordAssignment(1); // Array initialization
        
        // Outer loop: iterates m times
        for (int i = m - 1; i >= 0; i--) {
            counter.recordComparison(1); // i >= 0
            counter.recordAssignment(1); // Implicit: i--
            
            int dx = sx.charAt(i) - '0';
            counter.recordArrayAccess(1); // charAt access
            counter.recordAssignment(2); // dx and subtraction result
            counter.recordSubtraction(1);
            
            int carry = 0;
            counter.recordAssignment(1);
            
            // Inner loop: iterates n times for each i
            for (int j = n - 1; j >= 0; j--) {
                counter.recordComparison(1); // j >= 0
                counter.recordAssignment(1); // Implicit: j--
                
                int dy = sy.charAt(j) - '0';
                counter.recordArrayAccess(1);
                counter.recordAssignment(1);
                counter.recordSubtraction(1);
                
                // Calculate: result[i+j+1] + (dx*dy) + carry
                int sum = result[i + j + 1] + (dx * dy) + carry;
                counter.recordArrayAccess(1); // result array access
                counter.recordMultiplication(1); // dx * dy
                counter.recordAddition(2); // array_value + product, then + carry
                counter.recordAssignment(1); // sum assignment
                
                // Store: result[i+j+1] = sum % 10
                result[i + j + 1] = sum % 10;
                counter.recordModulo(1);
                counter.recordArrayAccess(1); // write to array
                counter.recordAssignment(1);
                
                // Calculate: carry = sum / 10
                carry = sum / 10;
                counter.recordDivision(1);
                counter.recordAssignment(1);
            }
            
            // Final carry addition
            result[i] += carry;
            counter.recordArrayAccess(2); // read and write
            counter.recordAddition(1);
            counter.recordAssignment(1);
        }
        
        // Convert array back to BigInteger for return
        StringBuilder sb = new StringBuilder();
        counter.recordAssignment(1);
        
        for (int digit : result) {
            sb.append(digit);
            counter.recordAddition(1); // for loop iteration
            counter.recordArrayAccess(1); // array access
            counter.recordAssignment(1); // append operation
        }
        
        return new BigInteger(sb.toString());
    }
    
    public static void main(String[] args) {
        System.out.println("Simple Multiplication Algorithm Analysis");
        System.out.println("=========================================");
        System.out.printf("%-15s | %-20s\n", "n (Digits)", "Total Operations");
        System.out.println("-----------------------------------------");
        
        for (int n = 1; n <= 100; n = (n == 1) ? 10 : n + 10) {
            BigInteger[] data = DataGenerator.generate(n);
            multiply(data[0], data[1]);
            System.out.printf("%-15d | %-20d\n", n, counter.getTotalOperations());
        }
        System.out.println("=========================================");
    }
}