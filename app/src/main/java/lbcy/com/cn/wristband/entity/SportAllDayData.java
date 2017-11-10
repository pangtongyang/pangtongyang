package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/17.
 */

@Entity
public class SportAllDayData {
    @Id
    private Long id;
    private String date;
    private int done_steps;
    private boolean isUpload = false;
    @Generated(hash = 875603292)
    public SportAllDayData(Long id, String date, int done_steps, boolean isUpload) {
        this.id = id;
        this.date = date;
        this.done_steps = done_steps;
        this.isUpload = isUpload;
    }
    @Generated(hash = 409760744)
    public SportAllDayData() {
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
    public int getDone_steps() {
        return this.done_steps;
    }
    public void setDone_steps(int done_steps) {
        this.done_steps = done_steps;
    }
    public boolean getIsUpload() {
        return this.isUpload;
    }
    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}
