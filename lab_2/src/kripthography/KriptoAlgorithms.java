package kripthography;

import java.util.ArrayList;

public class KriptoAlgorithms {

   // public static Long g = 2l;
    public static Long p = 30803l;
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
    private void copyMass(ArrayList<Integer> M1, ArrayList<Integer> M2)
    {
        M1.add(0, M2.get(0));
        M1.add(1, M2.get(1));
        M1.add(2, M2.get(2));
    }
    private void addElements(ArrayList<Integer> Mass, Integer element1, int element2, int element3)
    {

        Mass.add(0, element1);
        Mass.add(1, element2);
        Mass.add(2, element3);
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
        addElements(U, a, 1, 0);
        addElements(V, b, 0, 1);

        while (V.get(0) != 0)
        {
            q = U.get(0) / V.get(0);
            T.add(0, (U.get(0) % V.get(0)));
            T.add(1, (U.get(1) - q * V.get(1)));
            T.add(2, (U.get(2) - q * V.get(2)));

            copyMass(U, V);
            copyMass(V, T);
        }
        V = null;
        T = null;
        return U;
    }
    public Long inversion(Long a, Long mod)
    {
        for(int i = 0; i < mod; i ++)
        {
            if((a * i) % mod == 1)
            {
                return new Long(i);
            }
        }
        return 0l;
    }
    public Integer getRandom(Integer start, Long end)
    {
        Integer ret = ((int) (start + (Math.random() * end)));
        return  ret;
    }
    public String code(String message, Long secretKey)
    {
        byte[] bytes = message.getBytes();
        String secretMessage = "";
        for(int i = 0; i < bytes.length; i ++)
        {
            Long temp = bytes[i] + secretKey;
            secretMessage += temp.toString() + ".";
        }
        return secretMessage;
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
}
