package Alice;

public class MainBob {
    public static void main(String[] args)
    {

        try {
            Alice bob = new Alice("Bob", 113l, 281l, 3l); //P, Q, d
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
