package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/5.
 */

public class UserInfoBean {

    /**
     * code : 200
     * message : null
     * data : {"id":"1233","role":2,"name":"张同学","sex":0,"logo":"","birth":"1998-08-12","mac_address":"mac"}
     */

    private int code;
    private Object message;
    private UserInfoData data;

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

    public UserInfoData getData() {
        return data;
    }

    public void setData(UserInfoData data) {
        this.data = data;
    }

}
