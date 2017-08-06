package lbcy.com.cn.blacklibrary.ble;

import java.util.ArrayList;

/**
 * Created by chenjie on 2017/8/6.
 */

public interface DeviceConnect {
    void connect();
    void scan(ArrayList data);
}
