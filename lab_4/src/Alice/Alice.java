
package Alice;
import clientServer.ClientWindow;
import kripthography.KriptoAlgorithms;

public class Alice extends ClientWindow{

    private Long Q;
    private Long P;
    private Long d;
   // private Integer publicN;
    private Long fi;

    private Integer sizePublicKeys = 0;
    private kripthography.KriptoAlgorithms algorithms = new kripthography.KriptoAlgorithms();
    Alice(String name, Long P, Long Q, Long d) throws InterruptedException {
        super(name);
        this.P = P;
        this.Q = Q;
        this.d = d;
        System.out.println("i'm "+ name);
        publicN = P*Q;
        fi = new Long((P - 1) * (Q - 1));
        secretC = 2l;
        while (algorithms.degreeByMod(secretC * d, 1l, fi) != 1)
        {
            secretC ++;
        }

//        secretC = Long.valueOf(algorithms.getRandom(2l, KriptoAlgorithms.p - 2));
  //      Long d = algorithms.degreeByMod(KriptoAlgorithms.g, secretC, KriptoAlgorithms.p);
        super.sendMsg(publicN.toString() + "." + d.toString());
        while (true) {

            if ((tempKeys.size() == 0)||(tempKeys.size() != sizePublicKeys))
            {
                super.updatePublicKeys();
                Thread.sleep(100);
                Integer count = 100;
                while (!flagKeysIsReady) // wait for updating
                {
                    count += 100;
                    Thread.sleep(100);
                    if(count >= 300)
                    {
                        break;
                    }
                }
              sizePublicKeys = tempKeys.size();
            }
            Thread.sleep(500);
        }
    }

}
