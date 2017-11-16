package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/11/2.
 */

@Entity
public class HeartBeatsAllDayMaxMin {
    @Id
    private Long id;
    private String date;
    private int min_heartbeats;
    private int max_heartbeats;
    private boolean isUpload = false;
    @Generated(hash = 2120650402)
    public HeartBeatsAllDayMaxMin(Long id, String date, int min_heartbeats,
            int max_heartbeats, boolean isUpload) {
        this.id = id;
        this.date = date;
        this.min_heartbeats = min_heartbeats;
        this.max_heartbeats = max_heartbeats;
        this.isUpload = isUpload;
    }
    @Generated(hash = 19177012)
    public HeartBeatsAllDayMaxMin() {
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
    public int getMin_heartbeats() {
        return this.min_heartbeats;
    }
    public void setMin_heartbeats(int min_heartbeats) {
        this.min_heartbeats = min_heartbeats;
    }
    public int getMax_heartbeats() {
        return this.max_heartbeats;
    }
    public void setMax_heartbeats(int max_heartbeats) {
        this.max_heartbeats = max_heartbeats;
    }
    public boolean getIsUpload() {
        return this.isUpload;
    }
    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}
