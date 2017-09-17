package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 闹钟设置
 */
@Entity
public class AlarmClockInfo {
    @Id
    private Long id;//id

    private Integer state ;//闹钟状态是否启用,0不可用，1可用

    private String weekDays;//闹钟星期设置

    private String hours;//小时

    private String minutes;//分钟

    private String did;//设备id

    @Generated(hash = 2031045244)
    public AlarmClockInfo(Long id, Integer state, String weekDays, String hours,
            String minutes, String did) {
        this.id = id;
        this.state = state;
        this.weekDays = weekDays;
        this.hours = hours;
        this.minutes = minutes;
        this.did = did;
    }

    @Generated(hash = 1903474416)
    public AlarmClockInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getWeekDays() {
        return this.weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getHours() {
        return this.hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return this.minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getDid() {
        return this.did;
    }

    public void setDid(String did) {
        this.did = did;
    }


}
