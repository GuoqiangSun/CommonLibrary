package cn.com.common.test.facedetect;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import cn.com.common.test.R;
import cn.com.swain.baselib.log.Tlog;

/**
 * author Guoqiang_Sun
 * date 2019/8/19
 * desc
 */
public class FaceDetect2 {
    private Resources res;
    private MyImageView mIV;
    private Bitmap mFaceBitmap;
    private int mFaceWidth = 200;
    private int mFaceHeight = 200;
    private static final int MAX_FACES = 10;
    private static String TAG = "TutorialOnFaceDetect";
    private Handler mHandler;


    public FaceDetect2(MyImageView mIV, Resources res) {
        this.mIV = mIV;
        this.res = res;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void faceDetection() {

        // load the photo
        Bitmap b = BitmapFactory.decodeResource(res, R.drawable.face3);
        mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true);
        b.recycle();

        mFaceWidth = mFaceBitmap.getWidth();
        mFaceHeight = mFaceBitmap.getHeight();
        mIV.setImageBitmap(mFaceBitmap);
        mIV.invalidate();

        // perform face detection in setFace() in a background thread
        doLengthyCalc();
    }

    private void setFace() {
        FaceDetector fd;
        FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
        PointF eyescenter = new PointF();
        float eyesdist = 0.0f;
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
            fpx = new int[count * 2];
            fpy = new int[count * 2];

            for (int i = 0; i < count; i++) {
                try {
                    faces[i].getMidPoint(eyescenter);
                    eyesdist = faces[i].eyesDistance();

                    // set up left eye location
                    fpx[2 * i] = (int) (eyescenter.x - eyesdist / 2);
                    fpy[2 * i] = (int) eyescenter.y;

                    // set up right eye location
                    fpx[2 * i + 1] = (int) (eyescenter.x + eyesdist / 2);
                    fpy[2 * i + 1] = (int) eyescenter.y;

                    Tlog.e(TAG, "setFace(): face " + i + ": confidence = " + faces[i].confidence()
                            + ", eyes distance = " + faces[i].eyesDistance()
                            + ", pose = (" + faces[i].pose(FaceDetector.Face.EULER_X) + ","
                            + faces[i].pose(FaceDetector.Face.EULER_Y) + ","
                            + faces[i].pose(FaceDetector.Face.EULER_Z) + ")"
                            + ", eyes midpoint = (" + eyescenter.x + "," + eyescenter.y + ")");
                } catch (Exception e) {
                    Tlog.e(TAG, "setFace(): face " + i + ": " + e.toString());
                }
            }
        }

        mIV.setDisplayPoints(fpx, fpy, count * 2, 1);
    }

    private void doLengthyCalc() {
        Thread t = new Thread() {
            public void run() {
                try {
                    setFace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mIV.invalidate();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "doLengthyCalc(): " + e.toString());
                }
            }
        };

        t.start();
    }


}
