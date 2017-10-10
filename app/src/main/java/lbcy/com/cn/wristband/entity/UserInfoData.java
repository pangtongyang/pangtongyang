package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/5.
 */

@Entity
public class UserInfoData {

    @Id
    private Long mId;
    private String id;
    private int role;
    private String name;
    private int sex;
    private String logo;
    private String birth;
    private String mac_address;
    @Generated(hash = 1777068275)
    public UserInfoData(Long mId, String id, int role, String name, int sex,
            String logo, String birth, String mac_address) {
        this.mId = mId;
        this.id = id;
        this.role = role;
        this.name = name;
        this.sex = sex;
        this.logo = logo;
        this.birth = birth;
        this.mac_address = mac_address;
    }
    @Generated(hash = 81812083)
    public UserInfoData() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getRole() {
        return this.role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getLogo() {
        return this.logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getBirth() {
        return this.birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public String getMac_address() {
        return this.mac_address;
    }
    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

}
