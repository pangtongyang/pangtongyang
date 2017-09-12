package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 心率
 */
@Entity
public class HeartRateInfo {
    @Id
    private Long id;//id

    private String dateTime;//日期

    private String state;//标识,0未上报,1上报

    private String content;//内容

    @Generated(hash = 1516324077)
    public HeartRateInfo(Long id, String dateTime, String state, String content) {
        this.id = id;
        this.dateTime = dateTime;
        this.state = state;
        this.content = content;
    }

    @Generated(hash = 1759599401)
    public HeartRateInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
