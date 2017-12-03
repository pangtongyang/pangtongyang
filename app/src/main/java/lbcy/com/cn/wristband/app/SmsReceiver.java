package lbcy.com.cn.wristband.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;


import lbcy.com.cn.purplelibrary.config.CommonConfiguration;
import lbcy.com.cn.purplelibrary.manager.PurpleDeviceManagerNew;
import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;

/**
 * Created by chenjie on 2017/11/7.
 */

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage msg;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            if (smsObj == null)
                return;
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
//                Date date = new Date(msg.getTimestampMillis());//时间
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                String receiveTime = format.format(date);
//                System.out.println("number:" + msg.getOriginatingAddress()
//                        + "   body:" + msg.getDisplayMessageBody() + "  time:"
//                        + msg.getTimestampMillis());
                SPUtil spUtil = new SPUtil(context, CommonConfiguration.SHAREDPREFERENCES_NAME);

                if (spUtil.getString("sms_switch", "0").equals("0"))
                    return;

                if (spUtil.getString("which_device", "2").equals("2")){
                    Message message = new Message();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type", Consts.MMS);
                    bundle1.putString("title", msg.getOriginatingAddress());
                    bundle1.putString("content", msg.getDisplayMessageBody());
                    message.what = Consts.SHOW_MESSAGE_FROM_APPS;
                    message.setData(bundle1);

                    RxBus.getInstance().post(Consts.NOTIFICATION_LISTENER, message);

                } else {
                    PurpleDeviceManagerNew.getInstance().sendNotification(msg.getOriginatingAddress(), msg.getDisplayMessageBody());
                }

            }
        }
    }
}
