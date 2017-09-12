package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 勿扰时间段设置
 */
@Entity
public class NotFazeTime {
    @Id
    private Long id;//id
    private String startTime;//开始时间
    private String endTime;//结束时间
    private int state;//状态,1启用，0未启用
    @Generated(hash = 1585826908)
    public NotFazeTime(Long id, String startTime, String endTime, int state) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }
    @Generated(hash = 1978924254)
    public NotFazeTime() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }

}
