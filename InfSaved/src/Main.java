import java.util.Scanner;

public class Main {
    private static Scanner inputStr = new Scanner(System.in);
    public static void main(String[] args) {

        String inputString = inputStr.nextLine();
        while (!inputString.equalsIgnoreCase("stop")) {

            byte[] key = "key".getBytes();
            System.out.println(inputString.getBytes() + "col = " + inputString.getBytes().length);
            RC4 coder = new RC4(key);
            RC4 decoder = new RC4(key);

            byte[] secretString = coder.encrypt(inputString.getBytes());
            System.out.println("secret string:" + secretString + "col = " + secretString.length);

            byte[] openString = decoder.decrypt(secretString);
            String openStr = new String(openString);
            System.out.println("open string:" + openStr);
            inputString = "";
            inputString = inputStr.nextLine();
        }
    }
}
