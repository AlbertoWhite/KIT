
package Alice;
import clientServer.ClientWindow;
import kripthography.KriptoAlgorithms;

public class Alice extends ClientWindow{

    private Integer sizePublicKeys = 0;
    private kripthography.KriptoAlgorithms algorithms = new kripthography.KriptoAlgorithms();
    Alice(String name) throws InterruptedException {
        super(name);
        System.out.println("i'm "+ name);
        secretC = Long.valueOf(algorithms.getRandom(2, KriptoAlgorithms.p - 2));
        secretD = algorithms.inversion(secretC, KriptoAlgorithms.p - 1);
        while(algorithms.degreeByMod(secretC * secretD, 1l, KriptoAlgorithms.p - 1) == 0) {
      //      System.out.println("check:" + algorithms.degreeByMod(secretC * secretD, 1l, KriptoAlgorithms.p - 1));
            secretC = Long.valueOf(algorithms.getRandom(2, KriptoAlgorithms.p - 2));
            secretD = algorithms.inversion(secretC, KriptoAlgorithms.p - 1);
        }
    }

}
