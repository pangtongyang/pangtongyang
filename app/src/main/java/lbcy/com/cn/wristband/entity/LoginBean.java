package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chenjie on 2017/9/30.
 */

public class LoginBean {

    /**
     * code : 200
     * message : null
     * data : {"account_no":"123","name":"123","logo":"http://www.XXX.com","token":"D1E744D9BBA8A0FAB313669E0E17702E9B486C0C00708354DC79EFAE749B2317B7A9041FEAF70D46CE1B3DB40F3AAB9B5755843150B207F1D01D5A4F81AF7BCE03CE614D421492E2CF0ACA60EEDD6DC57B8EE30499D0FDC05D940C91B96D09DE02E1BC89AE19163CDE84EF73CB1C523C","mac_address":"CC:BC:0E:0E:AC:W2","device_type":"2","role":1}
     */

    private int code;
    private Object message;
    private LoginData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

}
