
package Alice;
import clientServer.ClientWindow;
import kripthography.KriptoAlgorithms;

public class Alice extends ClientWindow{
    private kripthography.KriptoAlgorithms algorithms = new kripthography.KriptoAlgorithms();
    Alice() {
        super();
        secretC = Long.valueOf(algorithms.getRandom(2l, KriptoAlgorithms.p - 2));
        publicD = algorithms.degreeByMod(KriptoAlgorithms.g, secretC, KriptoAlgorithms.p); //public key
        addPublicKey();
    }

}
