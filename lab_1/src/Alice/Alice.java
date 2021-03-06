package Alice;
import clientServer.ClientWindow;
import cryptography.CryptoAlgorithms;

public class Alice extends ClientWindow{
    private Long a;
    private Integer sizePublicKeys = 0;
    private CryptoAlgorithms algorithms = new CryptoAlgorithms();

    Alice(String name) throws InterruptedException {
        super(name);
        System.out.println("i'm "+ name);
        a = Long.valueOf(algorithms.getRandom(0, 10000));
        Long A = algorithms.degreeByMod(CryptoAlgorithms.g, a, CryptoAlgorithms.p);
        super.sendMsg(A.toString());
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
                secretKeys.clear();
                sizePublicKeys = tempKeys.size();

                for (String s : tempKeys.keySet()) {

                    secretKeys.put(s, algorithms.degreeByMod(tempKeys.get(s), a, CryptoAlgorithms.p));
                    if(s.equalsIgnoreCase(name))
                        continue;
                    System.out.println("(secret)" + s + " : " + secretKeys.get(s));
                }


            }
            Thread.sleep(500);
        }
    }

}
