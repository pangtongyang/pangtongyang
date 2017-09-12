package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/9/10.
 */

public class ClockBean {
    public ClockBean(String hour, String minute, String text, boolean switchState){
        this.hour = hour;
        this.minute = minute;
        this.text = text;
        this.switchState = switchState;
    }
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    String hour;
    String minute;
    String text;
    boolean switchState;
}
