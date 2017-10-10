package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/5.
 */

public class HelpBean {

    /**
     * code : 200
     * message : null
     * data : [{"id":123,"question":"","answer":""},{"id":124,"question":"","answer":""}]
     */

    private int code;
    private Object message;
    private List<HelpData> data;

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

    public List<HelpData> getData() {
        return data;
    }

    public void setData(List<HelpData> data) {
        this.data = data;
    }

}
