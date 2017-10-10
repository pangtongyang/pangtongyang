package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/4.
 */

public class MessageBean {

    /**
     * code : 200
     * message : ok
     * data : null
     */

    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
