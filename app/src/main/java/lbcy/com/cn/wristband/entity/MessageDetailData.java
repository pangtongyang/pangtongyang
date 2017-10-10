package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/5.
 */

@Entity
public class MessageDetailData {
    /**
     * id : 123
     * abstracts : 消息摘要
     * time : 2017-09-10 00:30:00
     * status : 1
     * detail : 消息内容
     */

    @Id
    private Long mId;
    private int id;
    private String abstracts;
    private String time;
    private int status;
    private String detail;
    @Generated(hash = 1727581774)
    public MessageDetailData(Long mId, int id, String abstracts, String time,
            int status, String detail) {
        this.mId = mId;
        this.id = id;
        this.abstracts = abstracts;
        this.time = time;
        this.status = status;
        this.detail = detail;
    }
    @Generated(hash = 37812191)
    public MessageDetailData() {
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
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

}
