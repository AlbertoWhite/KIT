
package Alice;
import clientServer.ClientWindow;
import kripthography.KriptoAlgorithms;

public class Alice extends ClientWindow{

    private Integer sizePublicKeys = 0;
    private kripthography.KriptoAlgorithms algorithms = new kripthography.KriptoAlgorithms();
    Alice(String name) throws InterruptedException {
        super(name);
        System.out.println("i'm "+ name);
        secretC = Long.valueOf(algorithms.getRandom(2l, KriptoAlgorithms.p - 2));
        Long d = algorithms.degreeByMod(KriptoAlgorithms.g, secretC, KriptoAlgorithms.p);
        super.sendMsg(d.toString());
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
               // secretKeys.clear();
                sizePublicKeys = tempKeys.size();

             /*   for (String s : tempKeys.keySet()) {

                    secretKeys.put(s, algorithms.degreeByMod(tempKeys.get(s), a, KriptoAlgorithms.p));
                    if(s.equalsIgnoreCase(name))
                        continue;
                    System.out.println("(secret)" + s + " : " + secretKeys.get(s));
                }
*/

            }
            Thread.sleep(500);
        }
    }

}
