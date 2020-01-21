
package clientServer;
import clientServer.ClientWindow;
import kripthography.KriptoAlgorithms;

import java.util.ArrayList;

public class Bank/* extends ClientWindow*/{

    private Long Q = 227l;
    private Long P = 131l;
    public Long d;
    private Long fi;
    private ArrayList<Long> moneyNumbers = new ArrayList<>();
    public Long publicN;//
    private Long secretC;
    private Long bankAccount;
    private Long shopAccount = 0l;

    private kripthography.KriptoAlgorithms algorithms = new kripthography.KriptoAlgorithms();
    Bank(/*Long P, Long Q*/) {
        super();
       // this.P = P;
        //this.Q = Q;
        publicN = P*Q;
        fi = new Long((P - 1) * (Q - 1));
        for(int i = 2; i < fi; i ++)
        {
            if(algorithms.gcd(i, Integer.valueOf(fi.toString())) == 1)
            {
                d = new Long(i);
                break;
            }
        }
        secretC = 2l;
        while (algorithms.degreeByMod(secretC * d, 1l, fi) != 1)
        {
            secretC ++;
        }

    }
    public void setBankAccount(Long summ)
    {
        bankAccount = summ;
    }
    public Long getBankAccount()
    {
        return bankAccount;
    }
    public Long getShopAccount()
    {
        return shopAccount;
    }
    public boolean checkUsed(Long n, Long summ)
    {
        if(moneyNumbers.contains(n)) {
            bankAccount += summ;
            return true;
        }
        else
        {
            moneyNumbers.add(n);
            shopAccount += summ;
            return false;
        }
    }
    public String getMoney(Long n, Long summ)
    {
        //if(!checkUsed(n)) {
            String secretMessage = algorithms.code(n.toString(), publicN, secretC);
            bankAccount -= summ;
            //moneyNumbers.add(n);
            return secretMessage;
        //}
        //else return null;
    }

}
