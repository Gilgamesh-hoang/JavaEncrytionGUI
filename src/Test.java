import model.AbstractEncryptionAlgorithm;
import model.symmetric.AES;
import model.symmetric.Blowfish;
import model.symmetric.DES;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {

        List<AbstractEncryptionAlgorithm> algorithm = new ArrayList<>();
        algorithm.add(new AES());
        algorithm.add(new Blowfish());
        algorithm.add(new DES());


        String plaintext = "Hello, World!hôm nay ăn cái gì vậy";
        algorithm.forEach(algo -> {
            System.out.println("Algorithm: " + algo.name());
            String[] keyLengths = algo.getKeyLengths();
            String[] paddings = algo.getPaddings();
            String[] modes = algo.getModes();

            for (String keyLength : keyLengths) {
                int keyLengthInBits = Integer.parseInt(keyLength);
                String key = algo.generateKey(keyLengthInBits);
                for (String padding : paddings) {
                    for (String mode : modes) {
//                        System.out.printf("key: %s,Key Length: %s,Padding: %s, Mode: %s \n", key, keyLength, padding, mode);
                        try {
                            String encrypted = algo.encrypt(plaintext, key, keyLengthInBits, mode, padding);
//                            System.out.println("Encrypted: " + encrypted);
                            String decrypted = algo.decrypt(encrypted, key, keyLengthInBits, mode, padding);
//                            System.out.println("Decrypted: " + decrypted);
//                            System.out.println("=====================================");
                            if (!decrypted.equals(plaintext)) {
                                System.out.println("not equal");
                                System.out.printf("key: %s,Key Length: %s,Padding: %s, Mode: %s \n", key, keyLength, padding, mode);
                                return;
                            }
                        } catch (Exception e) {
                            System.out.println("Error with Key Length: " + keyLength + ", Padding: " + padding + ", Mode: " + mode);
                            e.printStackTrace();
                            System.out.println("=====================================");
                            return;
                        }
                    }
                }
            }
        });


    }
}
