package Alice;

public class MainAlice {
    public static void main(String[] args)
    {

        try {
            Alice alice = new Alice("Alice", 131l, 227l, 3l); // P, Q, d
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
