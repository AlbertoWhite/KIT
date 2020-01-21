package cryptography;

import java.util.ArrayList;

public class CryptoAlgorithms {

    public static Long g = 2l;
    public static Long p = 30803l;
    private ArrayList<Boolean> key;
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
    Integer gcd(Integer a, Integer b)
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
    ArrayList<Integer> gcdFull(Integer a, Integer b)
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
    public Integer getRandom(Integer start, Integer end)
    {
        Integer ret = ((int) (start + (Math.random() * end)));
        return  ret;
    }
    private ArrayList<Boolean> getKey(int n)
    {
        ArrayList<Boolean> keyT = new ArrayList<>();
        for (int i = 0; i < n; i ++)
        {
            int temp = getRandom(0, 2);
            if(temp == 0)
                keyT.add(false);
            else
                keyT.add(true);
        }
        return  keyT;
    }
    private String getStr(ArrayList<Boolean> message)
    {
        String output = "";
        for(int i = 0; i < message.size(); i ++)
        {
            if(message.get(i) == false)
                output += '0';
            else
                output += '1';
        }
        return output;
    }
    private ArrayList<Boolean> getBool(String message)
    {
        ArrayList<Boolean> output = new ArrayList<>();
        for(int i = 0; i < message.length(); i ++)
        {
            if(message.charAt(i) == '0')
                output.add(false);
            else
                output.add(true);
        }
        return output;
    }
    public String code(String message)
    {
        ArrayList<Boolean> boolList = getBool(message);
        ArrayList<Boolean> secretMessage = new ArrayList<>();
        key = getKey(boolList.size());
        for(int i = 0; i < boolList.size(); i ++)
        {
            secretMessage.add(boolList.get(i) ^ key.get(i));
        }
        String str = getStr(secretMessage) + "." + getStr(key);
        return str;
    }
    public String decode(String message)
    {
        String msg = message.substring(0, message.indexOf("."));
        String keyStr = message.substring(message.indexOf(".") + 1);
        ArrayList<Boolean> boolList = getBool(msg);
        key = getBool(keyStr);
        ArrayList<Boolean> secretMessage = new ArrayList<>();
        for(int i = 0; i < boolList.size(); i ++)
        {
            secretMessage.add(boolList.get(i) ^ key.get(i));
        }
        return getStr(secretMessage);
    }
}
