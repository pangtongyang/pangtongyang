package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/11/10.
 */

@Entity
public class MessageListData {
    /**
     * id : 123
     * abstracts : 消息摘要
     * time : 2017-09-10 00:30:00
     * status : 1
     */

    @Id
    private Long mId;
    private int id;
    private String abstracts;
    private String time;
    private int status;
    @Generated(hash = 1901887628)
    public MessageListData(Long mId, int id, String abstracts, String time,
            int status) {
        this.mId = mId;
        this.id = id;
        this.abstracts = abstracts;
        this.time = time;
        this.status = status;
    }
    @Generated(hash = 1650272673)
    public MessageListData() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAbstracts() {
        return this.abstracts;
    }
    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
}
