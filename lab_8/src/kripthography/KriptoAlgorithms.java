package kripthography;

import java.util.ArrayList;

public class KriptoAlgorithms {

    public static Long g = 2l;
    public static Long p = 18181l;
    private void to2SystemFrom10(ArrayList arrayList, Long x)
    {
        Long temp = x;
        while (temp >= 1)
        {
            arrayList.add(new Long(temp%2));
            temp /= 2;
        }
    }
    public Long degreeByMod(Long a, Long x, Long mod)
    {
        ArrayList<Long> numberIn2System = new ArrayList<>();
        to2SystemFrom10(numberIn2System, x);
        ArrayList<Long> znach = new ArrayList<>(numberIn2System.size());
        znach.add(a);
        for(int i = 1; i < numberIn2System.size(); i ++)
        {
            znach.add(i, (znach.get(i - 1) * znach.get(i - 1)) % mod);
        }
        Long multiplex = 1l;
        for(int i = 0; i < numberIn2System.size(); i ++)
        {
            if(numberIn2System.get(i) > 0l)
            {
                multiplex = (multiplex * (numberIn2System.get(i) * znach.get(i))) % mod;
            }
        }
        numberIn2System = null;
        znach = null;
        return (multiplex);
    }
    public Integer gcd(Integer a, Integer b)
    {
        int r;
        if(a < b)
        {
            r = a;
            a = b;
            b = r;
        }
        while(b != 0)
        {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }
    public ArrayList<Integer> gcdFull(Integer a, Integer b)
    {
        int r;
        if(a < b)
        {
            r = a;
            a = b;
            b = r;
        }
        ArrayList<Integer> U = new ArrayList<>(3);
        ArrayList<Integer> V = new ArrayList<>(3);
        ArrayList<Integer> T = new ArrayList<>(3);
        int q;
        U.add(0, a);
        U.add(1, 1);
        U.add(2, 0);
        V.add(0, b);
        V.add(1, 0);
        V.add(2, 1);
        while (V.get(0) != 0)
        {
            q = U.get(0) / V.get(0);
            T.add(0, (U.get(0) % V.get(0)));
            T.add(1, (U.get(1) - q * V.get(1)));
            T.add(2, (U.get(2) - q * V.get(2)));

            U.add(0, V.get(0));
            U.add(1, V.get(1));
            U.add(2, V.get(2));

            V.add(0, T.get(0));
            V.add(1, T.get(1));
            V.add(2, T.get(2));
        }
        V = null;
        T = null;
        return U;
    }
    public Integer inversion(Integer a, Integer mod)
    {
        for(int i = 0; i < mod; i ++)
        {
            if((a * i) % mod == 1)
            {
                return new Integer(i);
            }
        }
        return 0;
    }
    public Long getRandomLong(Long start, Long end)
    {
        Long ret = ((long) (start + (Math.random() * end)));
        return  ret;
    }
    public Integer getRandom(Long start, Long end)
    {
        Integer ret = ((int) (start + (Math.random() * end)));
        return  ret;
    }
    public String code(String message, Long publicKeyN, Long publicKeyD)
    {
        String secretMessage = "";
        for (int i = 0; i < message.length(); i ++)
        {
            Long tempChar = Long.valueOf(message.charAt(i));
            Long tempSecret = degreeByMod(tempChar, publicKeyD, publicKeyN);
            secretMessage += "." + tempSecret.toString();
        }
        /*
        Long k = Long.valueOf(getRandom(1l, (p - 2l)));
        Long r = degreeByMod(g, k, p);
        Long e = degreeByMod(message * degreeByMod(publicKey, k, p), 1l, p);
        secretMessage = r.toString() + "." + e.toString();
        //System.out.println("secret msg:" + secretMessage);
        */
        return secretMessage;
    }

    public String decode(String message, Long secretKey, Long publicN)
    {
        String tmpMessage = message.substring(1, message.length());;
        String openMessage = "";
        while (tmpMessage.indexOf(".") != -1)
        {
            Long tempChar = Long.parseLong(tmpMessage.substring(0, tmpMessage.indexOf(".")));
            //Long tempMsg = Long.parseLong(tmpMessage.substring(tmpMessage.lastIndexOf(".") + 1, tmpMessage.length()));
            tmpMessage = tmpMessage.substring(tmpMessage.indexOf(".") + 1, tmpMessage.length());
            Long openTmp = degreeByMod(tempChar, secretKey, publicN);
            Integer codeS = Integer.parseInt(openTmp.toString());
            int tmp = codeS.intValue();
            openMessage += (char)tmp;
        }
       // System.out.println("in decode");
        Long tempChar = Long.parseLong(tmpMessage);

      //  System.out.println("parseLong:" + tempChar + " secretKey:" + secretKey + " N:" + publicN);
        Long openTmp = degreeByMod(tempChar, secretKey, publicN);

      //  System.out.println("open tmp:" + openTmp);
        Integer codeS = Integer.parseInt(openTmp.toString());
        int tmp = codeS.intValue();
        openMessage += (char)tmp;
        /*
        Long msg = degreeByMod(e * degreeByMod(r, p - 1 - secretKey, p), 1l, p);
        return  msg.toString();
     */
        return openMessage;
    }

    public boolean isDigit(String s)
    {
        try{
            Long.parseLong(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
public ArrayList<Long> mixCards(Long u1, Long u2, Long u3)
{
    ArrayList<Long> mixedCards = new ArrayList<>();
    int index1 = getRandom(0l, 3l);
    int index2 = getRandom(0l, 3l);
    int index3 = getRandom(0l, 3l);
    while (index1 == index2)
    {
        index2 = getRandom(0l, 3l);
    }
    while ((index3 == index2)||(index3 == index1))
    {
        index3 = getRandom(0l, 3l);
    }
    mixedCards.add(0l);
    mixedCards.add(0l);
    mixedCards.add(0l);
    //System.out.println("indexes: " + index1 + ", " + index2 + ", " + index3);
    mixedCards.set(index1, u1);
    mixedCards.set(index2, u2);
    mixedCards.set(index3, u3);
    return mixedCards;
}
    public ArrayList<Long> mixCards(Long u1, Long u2)
    {
        ArrayList<Long> mixedCards = new ArrayList<>();
        int index1 = getRandom(0l, 2l);
        int index2 = getRandom(0l, 2l);
        while (index1 == index2)
        {
            index2 = getRandom(0l, 2l);
        }
        mixedCards.add(0l);
        mixedCards.add(0l);

        mixedCards.set(index1, u1);
        mixedCards.set(index2, u2);
        return mixedCards;
    }
    /*public String codeBytes(String message, Long publicKey)
    {
        byte[] bytes = message.getBytes();
        String secretMessage = "D";
        for(int i = 0; i < bytes.length; i ++)
        {
            String temp = "" + bytes[i];
            String tmpString = code(Long.parseLong(temp), publicKey);
            secretMessage += tmpString + "|";
        }
        return secretMessage;
    }*/

    /*
    public String decodeBytes(String message, Long secretKey)
    {
        String openMessage = "";
        String msg = message.substring(1, message.length()); // delete "D"
        String temp = "";
        while(msg.length() > 1)
        {
            temp = msg.substring(0, msg.indexOf("|"));
            Integer codeS = Integer.parseInt(decode(temp, secretKey));
            int tmp = codeS.intValue();
            openMessage += (char)tmp;

            String tmp1 = msg.substring(temp.length() + 1, msg.length());
            msg = tmp1;
        }

        return openMessage;
    }*/
}