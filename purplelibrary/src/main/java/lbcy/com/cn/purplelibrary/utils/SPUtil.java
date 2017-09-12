package lbcy.com.cn.purplelibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 *
 */
public class SPUtil {
    private Context context;
    private String dbName;
    public SPUtil(Context context, String dbName){
        this.context=context;
        this.dbName=dbName;
    }
    //插入数据库
    public Boolean putString(String key, String value){
        Boolean success=false;
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(getDbName(), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        success=editor.commit();
        return success;
    }
    //清空数据库
    public Boolean clearData(){
        Boolean success=false;
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(getDbName(), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        success=editor.clear().commit();
        return success;
    }
    //获取值
    public String getString(String key){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(getDbName(), context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
    //获取值
    public String getString(String key, String defValue){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(getDbName(), context.MODE_PRIVATE);
        String valueStr=sharedPreferences.getString(key, null);
        if(valueStr==null){
            valueStr=defValue;
        }
        return valueStr;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
