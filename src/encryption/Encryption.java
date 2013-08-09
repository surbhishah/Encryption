package encryption;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Encryption {
    private static Cipher ecipher;
    private static Cipher dcipher;
    // 8-byte initialization vector
     private static byte[] iv = {
       (byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2,
        (byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3,
        (byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2,
        (byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3
    };

    public static void main(String[] args) {
        try {
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            encrypt(new FileInputStream("C:\\Users\\Surbhi\\Documents\\Major Project\\Myfile.docx"), new FileOutputStream("encrypted.dat"));
            decrypt(new FileInputStream("encrypted.dat"), new FileOutputStream("C:\\Users\\Surbhi\\Documents\\Major Project\\MyfileDec.docx"));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found:" + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Invalid Alogorithm Parameter:" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
        }
    }

    private static void encrypt(InputStream is, OutputStream os) {

        try {
            byte[] buf = new byte[1024];
// bytes at this stream are first encoded
            os = new CipherOutputStream(os, ecipher);
// read in the clear text and write to out to encrypt
            int numRead = 0;
            while ((numRead = is.read(buf)) >= 0) {
                os.write(buf, 0, numRead);
            }
// close all streams
            os.close();
        } catch (IOException e) {
            System.out.println("I/O Error:" + e.getMessage());
        }
    }

    private static void decrypt(InputStream is, OutputStream os) {
        try {
            byte[] buf = new byte[1024];
            try (CipherInputStream cis = new CipherInputStream(is, dcipher)) {
                int numRead = 0;
                while ((numRead = cis.read(buf)) >= 0) {
                    os.write(buf, 0, numRead);
                }
            }
            is.close();
            os.close();
        } catch (IOException e) {
            System.out.println("I/O Error:" + e.getMessage());
        }
    }
}
