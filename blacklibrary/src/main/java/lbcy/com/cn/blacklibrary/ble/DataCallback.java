package lbcy.com.cn.blacklibrary.ble;

/**
 * Created by chenjie on 2017/8/6.
 */

public interface DataCallback<T> {
    void OnSuccess(T data);
    void OnFailed();
    void OnFinished();
}
