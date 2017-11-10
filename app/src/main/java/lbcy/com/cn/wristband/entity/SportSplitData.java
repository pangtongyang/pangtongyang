package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/17.
 */

@Entity
public class SportSplitData {
    @Id
    private Long id;
    private String date;
    private int steps;
    private String duration;
    @Generated(hash = 413466299)
    public SportSplitData(Long id, String date, int steps, String duration) {
        this.id = id;
        this.date = date;
        this.steps = steps;
        this.duration = duration;
    }
    @Generated(hash = 1218089997)
    public SportSplitData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getSteps() {
        return this.steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }
    public String getDuration() {
        return this.duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
}
