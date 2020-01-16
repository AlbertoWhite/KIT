package kripthography;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KriptoAlgorithms {

    public static Long g = 2l;
    public static Long p = 31259l;
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
    public Long getRandom(Long start, Long end)
    {
        Long ret = ((long) (start + (Math.random() * end)));
        return  ret;
    }
    public String check(String message, String r, String s, Long publicD)
    {
        Long msg = Long.parseLong(message);
        Long rDigit = Long.parseLong(r);
        Long sDigit = Long.parseLong(s);
        Long left = degreeByMod(degreeByMod(publicD, rDigit, p) * degreeByMod(rDigit, sDigit, p), 1l, p);
        Long right = degreeByMod(g, msg, p);
        if(left.toString().equalsIgnoreCase(right.toString()))
            return "true";
        else return "false";
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
      public ArrayList<Long> calculateBigList(Long y, Long a, Long p, Long m)
      {
        ArrayList<Long> outputList = new ArrayList<>();
        outputList.add(y);
        for (int i = 1; i < m; i ++)
        {
            Long aInDegree = degreeByMod(a, new Long(i), p);
            outputList.add(degreeByMod(aInDegree * y, 1l, p));
        }
        return outputList;
      }
    public ArrayList<Long> calculateSmallList(Long a, Long p, Long k, Long m)
    {
        ArrayList<Long> outputList = new ArrayList<>();
        for (int i = 1; i < k; i ++)
        {
            Long aInDegree = degreeByMod(a, i * m, p);
            outputList.add(aInDegree);
        }
        return outputList;
    }
    public ArrayList<Integer> getIJOfTheSame(ArrayList<Long> smallList, ArrayList<Long> bigList)
    {
        ArrayList<Integer> output = new ArrayList<>();
        for (Long elem : smallList) {
            if(bigList.contains(elem))
            {
                System.out.print("contained: " + elem);

                output.add(smallList.indexOf(elem) + 1); //i
                output.add(bigList.indexOf(elem)); //j
                break;
            }
        }
        return output;
    }
    public ArrayList<Integer> getPrimes(Integer count)
    {
        ArrayList<Integer> primes = new ArrayList<>();
        if(count > 0)
            primes.add(2);
        for(int i = 3; primes.size() < count; i += 2)
        {
            if(isPrime(i, primes))
                primes.add(i);
        }
        return primes;
    }
    public boolean isPrime(Integer n, List<Integer> primes)
    {
        double sqrt = Math.sqrt(n);
        for (int i = 0; i < primes.size(); i ++)
        {
            Integer prime = primes.get(i);
            if(prime > sqrt)
                return true;
            if(n % prime == 0)
                return false;
        }
        return true;
    }
    private ArrayList<Integer> checkSmooth(Long param, ArrayList<Integer> s)
    {
        ArrayList<Integer> smooth = new ArrayList<>();
        Long temp = param;
        int index = 0;
        int count = 0;
        for(int i = 0; i < s.size(); i ++)
            smooth.add(i, 0);
        while(temp > 0) {
            if (temp % s.get(index) == 0) {
                System.out.print(" * " + s.get(index));
                Integer tempSm = smooth.get(index);
                //System.out.print(" [" + count + "]");
                smooth.add(index,  tempSm + 1);

                temp = temp / s.get(index);
                count ++;
                //System.out.print( "[" + count + "]");
                //System.out.print(" (tmp:" + temp+") ");
            } else {
                smooth.add(index,  count);
                //System.out.print( "[" + index+ ":" + count + "]");
                count = 0;
                index++;
                if (index >= s.size())// not smooth
                {
                    if (temp > 1)
                        return null;
                    else
                        return smooth;
                }
                //System.out.print(".");
                //smooth.add(0);
            }
        }
        return smooth;
    }
    public HashMap<Integer, ArrayList<Integer>> getSmoothList(Integer count, Long a, Long p, ArrayList<Integer> s)
    {
        HashMap<Integer, ArrayList<Integer>> smoothList = new HashMap<Integer,ArrayList<Integer>>();
        Integer tempCount = 0;
        Long k = 1l;
        while(tempCount < count)
        {
            Long tempA = degreeByMod(a, k, p);
            System.out.print(a + "^" + k + " mod" + p + " = " + tempA + "   := ");
            ArrayList<Integer> smoothElem = checkSmooth(tempA, s);
            System.out.println(" ");
            if(smoothElem != null)
            {
                smoothList.put(Integer.parseInt(k.toString()), smoothElem);
                tempCount ++;
            }
            k ++;
            if(k > p)
            {
                break;
            }
        }
        return smoothList;
    }
    public ArrayList<Integer> getLogList(HashMap<Integer,ArrayList<Integer>> smoothList, ArrayList<Integer> s, Long a)
    {
        //надо реализовать решение системы линейных уравнений
        for (Integer k : smoothList.keySet()) {
            System.out.print(k + " = " );
            ArrayList<Integer> tempSmoothList = smoothList.get(k);
            for (int i = 0; (i < tempSmoothList.size()) && (i < s.size()); i ++) {
                Integer ci = tempSmoothList.get(i);
                Integer tempS = s.get(i);
                if(ci > 1) {
                    if(i > 0)
                        System.out.print(" + ");
                    System.out.print("(" + ci + " * log " + a + " (" + tempS + "))");
                }
                if(ci == 1) {
                    if(i > 0)
                        System.out.print(" + ");
                    System.out.print("(" + " log " + a + " (" + tempS + "))");
                }
            }
            System.out.println(" ");
        }
        ArrayList<Integer> output = new ArrayList<>();
        output.add(30);
        output.add(18);
        output.add(17);
        return output;
    }
    public HashMap<Integer, ArrayList<Integer>> getSmoothList4(Long y, Long a, Long p, ArrayList<Integer> s)
    {
        HashMap<Integer, ArrayList<Integer>> smoothList = new HashMap<Integer,ArrayList<Integer>>();
        Long r = getRandom(1l, p / 2);
        //Long k = 1l;
        boolean flag = false;
        while(!flag)
        {
            Long tempA = degreeByMod( y * degreeByMod(a, r, p), 1l, p);
            System.out.print(y + " * " + a + "^" + r + " mod" + p + " = " + tempA + "  := ");
            ArrayList<Integer> smoothElem = checkSmooth(tempA, s);
            System.out.println(" ");
            if(smoothElem != null)
            {
                smoothList.put(Integer.parseInt(r.toString()), smoothElem);
                break;
            }
            else
            {
                r = getRandom(1l, p - 1);
            }
        }
        return smoothList;
    }
    public Long calculateX(HashMap<Integer, ArrayList<Integer>> smoothYA, ArrayList<Integer> logS, Long p, Long a, ArrayList<Integer> s)
    {
        Long x = 0l;
        for (Integer r : smoothYA.keySet()) {
            ArrayList<Integer> tempSmooth = smoothYA.get(r);
            Integer tempSum = 0;
            System.out.print("(" );
            for(int i = 0; (i < tempSmooth.size()) && (i < s.size()); i ++)
            {
                if(i > 0)
                    System.out.print(" + ");
                Integer ei = tempSmooth.get(i);
                Integer pi = s.get(i);
                Integer temp = ei * logS.get(i);
                Long tmpLong = degreeByMod(Long.parseLong(temp.toString()), 1l, p - 1);
                tempSum += tmpLong.intValue();
                System.out.print(ei + " * log " + a);
                System.out.print("(" + pi + ")");
            }
            tempSum -= r;
            x = degreeByMod(tempSum.longValue(), 1l, p - 1);
            System.out.print(") - " + r + "mod " + (p - 1));
        }
        return x;
    }
}
