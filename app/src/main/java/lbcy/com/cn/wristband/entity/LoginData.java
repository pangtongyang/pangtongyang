package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chenjie on 2017/9/30.
 */

@Entity
public class LoginData {
    /**
     * account_no : 123
     * name : 123
     * logo : http://www.XXX.com
     * token : D1E744D9BBA8A0FAB313669E0E17702E9B486C0C00708354DC79EFAE749B2317B7A9041FEAF70D46CE1B3DB40F3AAB9B5755843150B207F1D01D5A4F81AF7BCE03CE614D421492E2CF0ACA60EEDD6DC57B8EE30499D0FDC05D940C91B96D09DE02E1BC89AE19163CDE84EF73CB1C523C
     * mac_address : CC:BC:0E:0E:AC:W2
     * device_type : 2
     * role : 1
     */

    @Id
    private Long id;
    private String account_no;
    private String name;
    private String logo;
    private String token;
    private String mac_address;
    private String device_type;
    private int role;
    private String appType;//标识设备，1 -> ios ， 2 -> android
    @Generated(hash = 805002209)
    public LoginData(Long id, String account_no, String name, String logo, String token, String mac_address, String device_type, int role, String appType) {
        this.id = id;
        this.account_no = account_no;
        this.name = name;
        this.logo = logo;
        this.token = token;
        this.mac_address = mac_address;
        this.device_type = device_type;
        this.role = role;
        this.appType = appType;
    }
    @Generated(hash = 1578814127)
    public LoginData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount_no() {
        return this.account_no;
    }
    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogo() {
        return this.logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getMac_address() {
        return this.mac_address;
    }
    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
    public String getDevice_type() {
        return this.device_type;
    }
    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
    public int getRole() {
        return this.role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    public String getAppType() {
        return this.appType;
    }
    public void setAppType(String appType) {
        this.appType = appType;
    }

}
