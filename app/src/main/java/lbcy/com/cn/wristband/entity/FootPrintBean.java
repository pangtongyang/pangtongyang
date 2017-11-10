package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/24.
 */

public class FootPrintBean {

    /**
     * code : 200
     * message : null
     * data : http://sportapi.myplat.cn:8866/Home/List?key=04497D757127D6B5
     */

    private int code;
    private Object message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
