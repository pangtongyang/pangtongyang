package lbcy.com.cn.blacklibrary.utils;

import android.util.Log;

/**
 * Created by lixiaoning on 2017/5/10.
 */

public class Util {
    //16转2
    public String HToB(String a) {

        String b = Integer.toBinaryString(Integer.parseInt(a, 16));
        Log.e("PJ", "sendSuccessFormat:" + b);
        return b;
    }
    private static int getEachResult(int i, int hasDatas)
    {
        return  ((hasDatas & 0xffffffff) >> i) & 0x01;
    }
    public static String getDataBinString(int allData, int index, boolean isOpen)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 31; i >= 0; i--)
        {
            if(i == index)
            {
                String a = "0";
                if(!isOpen)
                {
                    a = "1";
                }
                sb.append(a);
            }
            else
            {
                sb.append(String.valueOf(getEachResult(i, allData)));
            }
        }
        return sb.toString();
    }

    // 二进制转十六进制
    public static String BToH(String a) {
        // 将二进制转为十进制再从十进制转为十六进制
        String b = Integer.toHexString(Integer.valueOf(toD(a, 2)));
        return b;
    }

    // 任意进制数转为十进制数
    public  static String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }

    // 将十六进制中的字母转为对应的数字
    public static int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {
            i = 11;
        }
        if (a.equals("c")) {
            i = 12;
        }
        if (a.equals("d")) {
            i = 13;
        }
        if (a.equals("e")) {
            i = 14;
        }
        if (a.equals("f")) {
            i = 15;
        }
        return i;
    }
}
