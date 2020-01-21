package Alice;

public class MainBob {
    public static void main(String[] args)
    {

        try {
            Alice bob = new Alice("Bob"); //P, Q, d
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
