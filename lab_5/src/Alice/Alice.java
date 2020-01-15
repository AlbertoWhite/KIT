
package Alice;
import clientServer.ClientWindow;
import kripthography.KriptoAlgorithms;

public class Alice extends ClientWindow{

    private Long Q;
    private Long P;
    private Integer d;
    private Long fi;

    private kripthography.KriptoAlgorithms algorithms = new kripthography.KriptoAlgorithms();
    Alice(Long P, Long Q) {
        super();
        this.P = P;
        this.Q = Q;
        publicN = P*Q;
        fi = new Long((P - 1) * (Q - 1));
        for(int i = 2; i < fi; i ++)
        {
            if(algorithms.gcd(i, Integer.valueOf(fi.toString())) == 1)
            {
                d = i;
                break;
            }
        }
        secretC = 2l;
        while (algorithms.degreeByMod(secretC * d, 1l, fi) != 1)
        {
            secretC ++;
        }

    }

}
