package cn.com.common.test.facedetect;

/*
 * TutorialOnFaceDetect
 *
 * [AUTHOR]: Chunyen Liu
 * [SDK   ]: Android SDK 2.1 and up
 * [NOTE  ]: developer.com tutorial, "Face Detection with Android APIs"
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout.LayoutParams;

public class TutorialOnFaceDetect extends Activity {
    FaceDetect fd;
    FaceDetect2 fd2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyImageView mIV = new MyImageView(this);
        setContentView(mIV, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        fd = new FaceDetect(mIV, getResources());
        fd2 = new FaceDetect2(mIV, getResources());

        fd.faceDetection();
    }

    private int t = 1;

    private long lastTouchTimes;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long l = System.currentTimeMillis();
        if (Math.abs(l - lastTouchTimes) > 500) {
            if (++t % 2 == 0) {
                fd2.faceDetection();
            } else {
                fd.faceDetection();
            }
            lastTouchTimes = l;
        }
        return super.onTouchEvent(event);
    }
}