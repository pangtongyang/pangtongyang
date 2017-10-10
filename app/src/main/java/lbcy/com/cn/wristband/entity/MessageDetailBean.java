package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/5.
 */

public class MessageDetailBean {

    /**
     * code : 200
     * message : null
     * data : {"id":123,"abstracts":"消息摘要","time":"2017-09-10 00:30:00","status":1,"detail":"消息内容"}
     */

    private int code;
    private Object message;
    private MessageDetailData data;

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

    public MessageDetailData getData() {
        return data;
    }

    public void setData(MessageDetailData data) {
        this.data = data;
    }

}
