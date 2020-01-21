
package Alice;
import clientServer.ClientWindow;

import java.util.ArrayList;

public class Alice extends ClientWindow{


    private kripthography.KriptoAlgorithms algorithms = new kripthography.KriptoAlgorithms();
    Alice(String name) throws InterruptedException {
        super(name);
        System.out.println("i'm "+ name);
        secretC = algorithms.getRandomLong(2l, algorithms.p * 2);
        //ArrayList<Integer> gcdR = algorithms.gcdFull(Integer.parseInt(secretC.toString()), Integer.parseInt(algorithms.p.toString()) - 1);
        while (/*gcdR.get(0) != 1*/ algorithms.gcd(Integer.parseInt(secretC.toString()), Integer.parseInt(algorithms.p.toString()) - 1) > 1)
        {
            secretC = algorithms.getRandomLong(2l, algorithms.p * 2);
            //gcdR = algorithms.gcdFull(Integer.parseInt(secretC.toString()), Integer.parseInt(algorithms.p.toString()) - 1);
        }
        //ArrayList<Integer> gcdRes = algorithms.gcdFull(Integer.parseInt(algorithms.p.toString()) - 1, Integer.parseInt(secretC.toString()));
        //secretD = Long.parseLong(gcdRes.get(2).toString());
        //if(secretD < 0)
         //   secretD += algorithms.p;
        Long tmp = 2l;
        while(algorithms.degreeByMod(secretC * tmp, 1l, algorithms.p - 1) != 1)
        {
            tmp ++;
        }
        secretD = tmp;
        //System.out.println("c * d mod (p - 1) = " + algorithms.degreeByMod(secretC * secretD, 1l, algorithms.p - 1));

    }

}
