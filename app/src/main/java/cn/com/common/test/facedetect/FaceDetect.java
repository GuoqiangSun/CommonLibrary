package cn.com.common.test.facedetect;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.Log;

import cn.com.common.test.R;

/**
 * author Guoqiang_Sun
 * date 2019/8/19
 * desc
 */

/*
 * TutorialOnFaceDetect1
 *
 * [AUTHOR]: Chunyen Liu
 * [SDK   ]: Android SDK 2.1 and up
 * [NOTE  ]: developer.com tutorial, "Face Detection with Android APIs"
 */
public class FaceDetect {

    public FaceDetect(MyImageView mIV, Resources res) {
        this.mIV = mIV;
        this.res = res;
    }

    private Resources res;
    private MyImageView mIV;
    private Bitmap mFaceBitmap;
    private int mFaceWidth = 200;
    private int mFaceHeight = 200;
    private static final int MAX_FACES = 10;
    private static String TAG = "TutorialOnFaceDetect";

    public void faceDetection() {

        // load the photo
        Bitmap b = BitmapFactory.decodeResource(res, R.drawable.face3);
        mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);
        b.recycle();

        mFaceWidth = mFaceBitmap.getWidth();
        mFaceHeight = mFaceBitmap.getHeight();
        mIV.setImageBitmap(mFaceBitmap);

        // perform face detection and set the feature points
        setFace();

        mIV.invalidate();
    }

    private void setFace() {
        FaceDetector fd;
        FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
        PointF midpoint = new PointF();
        int[] fpx = null;
        int[] fpy = null;
        int count = 0;

        try {
            fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);
            count = fd.findFaces(mFaceBitmap, faces);
        } catch (Exception e) {
            Log.e(TAG, "setFace(): " + e.toString());
            return;
        }

        // check if we detect any faces
        if (count > 0) {
            fpx = new int[count];
            fpy = new int[count];

            for (int i = 0; i < count; i++) {
                try {
                    faces[i].getMidPoint(midpoint);

                    fpx[i] = (int) midpoint.x;
                    fpy[i] = (int) midpoint.y;
                } catch (Exception e) {
                    Log.e(TAG, "setFace(): face " + i + ": " + e.toString());
                }
            }
        }

        mIV.setDisplayPoints(fpx, fpy, count, 0);
    }

}
