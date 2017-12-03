package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/5.
 */

public class MessageListBean {

    /**
     * code : 200
     * message : null
     * data : [{"id":123,"abstracts":"消息摘要","time":"2017-09-10 00:30:00","status":1},{"id":124,"abstracts":"消息摘要","time":"2017-09-10 00:30:00","status":1}]
     */

    private int code;
    private Object message;
    private List<MessageListData> data;

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

    public List<MessageListData> getData() {
        return data;
    }

    public void setData(List<MessageListData> data) {
        this.data = data;
    }

}
