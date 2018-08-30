package cn.com.common.baselib.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * author: Guoqiang_Sun
 * date : 2018/3/28 0028
 * desc :
 */

public abstract class BaseSpTool {

    private final SharedPreferences sp;

    public BaseSpTool(Context mCtx, String name){
        sp = mCtx.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    protected Editor getEditor(){
        return sp.edit();
    }


    public void putString(String key, String value) {
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void putInt(String key, int value) {
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void putLong(String key, long value) {
        Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public void putFloat(String key, float value) {
        Editor edit = sp.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    public void putBoolean(String key, boolean value) {
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }
}
