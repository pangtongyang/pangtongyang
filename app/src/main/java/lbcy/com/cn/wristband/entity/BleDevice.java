package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/8/9.
 */

public class BleDevice {
    public BleDevice(String name, String macAddress){
        this.name = name;
        this.macAddress = macAddress;
    }

    String name;

    String macAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
