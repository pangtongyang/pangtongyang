package lbcy.com.cn.purplelibrary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/11/6.
 */

@Entity
public class SportData {
    @Id
    Long id;
    String date;
    int step;
    boolean isUsed = false;
    @Generated(hash = 702095402)
    public SportData(Long id, String date, int step, boolean isUsed) {
        this.id = id;
        this.date = date;
        this.step = step;
        this.isUsed = isUsed;
    }
    @Generated(hash = 241431299)
    public SportData() {
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
    public int getStep() {
        return this.step;
    }
    public void setStep(int step) {
        this.step = step;
    }
    public boolean getIsUsed() {
        return this.isUsed;
    }
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
}
