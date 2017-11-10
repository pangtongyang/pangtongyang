package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/31.
 */

@Entity
public class HeartBeatsHistory {
    @Id
    Long id;
    String time;
    private int heartbeats;
    @Generated(hash = 2004281410)
    public HeartBeatsHistory(Long id, String time, int heartbeats) {
        this.id = id;
        this.time = time;
        this.heartbeats = heartbeats;
    }
    @Generated(hash = 1666661527)
    public HeartBeatsHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getHeartbeats() {
        return this.heartbeats;
    }
    public void setHeartbeats(int heartbeats) {
        this.heartbeats = heartbeats;
    }
}
