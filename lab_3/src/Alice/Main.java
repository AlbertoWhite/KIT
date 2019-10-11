package Alice;

public class Main {
    public static void main(String[] args)
    {

        try {
            Alice alice = new Alice(args[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
