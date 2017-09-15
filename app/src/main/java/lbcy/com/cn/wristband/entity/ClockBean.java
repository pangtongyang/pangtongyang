package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chenjie on 2017/9/10.
 */
@Entity
public class ClockBean {
    @Id
    Long id;
    String hour;
    String minute;
    String text;
    String type;
    boolean switchState;
    int position;//位置
    @Generated(hash = 843244155)
    public ClockBean(Long id, String hour, String minute, String text, String type,
            boolean switchState, int position) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.text = text;
        this.type = type;
        this.switchState = switchState;
        this.position = position;
    }
    @Generated(hash = 513947775)
    public ClockBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getHour() {
        return this.hour;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }
    public String getMinute() {
        return this.minute;
    }
    public void setMinute(String minute) {
        this.minute = minute;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean getSwitchState() {
        return this.switchState;
    }
    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }


}
