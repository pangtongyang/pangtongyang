package lbcy.com.cn.wristband.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenjie on 2017/9/29.
 */

public class StringUtil {
    public static int getNumFromString(String data){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(data);
        return Integer.valueOf(m.replaceAll("").trim());
    }
}
