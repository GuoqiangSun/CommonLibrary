package cn.com.common.baselib.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author: Guoqiang_Sun
 * date : 2018/6/8 0008
 * desc :
 */
public class SSL {

    public static String generalSsl(Context mContext) {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES);
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(info.signatures[0].toByteArray());
            String s = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            if (s == null) {
                return "";
            }
            return s.trim();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            return "";
        }
    }

}
