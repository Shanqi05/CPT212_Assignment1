import java.math.BigInteger;
import java.util.Random;

public class DataGenerator {

    public static BigInteger[] generate(int n) {

        Random r = new Random();

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