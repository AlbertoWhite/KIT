package Alice;

public class MainAlice {
    public static void main(String[] args)
    {
        Long p = Long.parseLong(args[0]);
        Long q = Long.parseLong(args[1]);
        Alice alice;
        if(args.length < 2)
            alice  = new Alice(131l, 227l); // P, Q
        else alice = new Alice(p, q);
    }
}