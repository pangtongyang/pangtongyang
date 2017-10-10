package lbcy.com.cn.wristband.utils.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import lbcy.com.cn.wristband.entity.DaoMaster;
import lbcy.com.cn.wristband.entity.LoginDataDao;

/**
 * Created by chenjie on 2017/10/7.
 */

public class UpgradeHelper extends DaoMaster.OpenHelper {

    public UpgradeHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.getInstance().migrate(db, LoginDataDao.class);
    }
}
