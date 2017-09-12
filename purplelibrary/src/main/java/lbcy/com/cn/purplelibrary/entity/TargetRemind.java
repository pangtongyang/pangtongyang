package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 进程提醒时间
 */
@Entity
public class TargetRemind {
    @Id
    private Long id;//id
    private String remindTime;//提醒时间
    @Generated(hash = 2100934927)
    public TargetRemind(Long id, String remindTime) {
        this.id = id;
        this.remindTime = remindTime;
    }
    @Generated(hash = 1192494171)
    public TargetRemind() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRemindTime() {
        return this.remindTime;
    }
    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

}
