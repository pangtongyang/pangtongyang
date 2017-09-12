package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 推送信息
 */
@Entity
public class AppPushInfo {
    @Id
    private Long id;//id

    private String did;//设备id

    private String packageName;//包名

    private Integer isEnabled ;//是否开启,0不可用，1可用

    @Generated(hash = 863991852)
    public AppPushInfo(Long id, String did, String packageName, Integer isEnabled) {
        this.id = id;
        this.did = did;
        this.packageName = packageName;
        this.isEnabled = isEnabled;
    }

    @Generated(hash = 1050854255)
    public AppPushInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDid() {
        return this.did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

}
