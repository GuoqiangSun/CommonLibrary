package cn.com.swain.baselib.permission;

/**
 * author: Guoqiang_Sun
 * date: 2019/3/20 0020
 * desc:
 */
public class FloatPermissionHelper {

    public static final int REQUEST_CODE = 0x9637;

    private static volatile FloatWindowPermission instance;

    public static FloatWindowPermission getInstance() {
        if (instance == null) {
            synchronized (FloatWindowPermission.class) {
                if (instance == null) {
                    instance = new FloatWindowPermission(REQUEST_CODE);
                }
            }
        }
        return instance;
    }


}
