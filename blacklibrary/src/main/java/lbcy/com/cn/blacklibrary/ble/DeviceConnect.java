package lbcy.com.cn.blacklibrary.ble;

import java.util.ArrayList;

/**
 * Created by chenjie on 2017/8/6.
 */

public interface DeviceConnect {
    void connect();//连接成功回调
    void scan(ArrayList data);//sdk自带扫描方法回调
}
