package cn.com.swain.baselib.util;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.com.swain.baselib.log.Tlog;

/**
 * author: Guoqiang_Sun
 * date : 2018/10/12 0012
 * desc :
 */
public class PhotoUtils {

    public static Uri getTakePhotoURI(Application app, File savePhotoFile) {
        Uri imageUri;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 从文件中创建uri
            imageUri = Uri.fromFile(savePhotoFile);
        } else {
            String authority = app.getPackageName() + ".photo.provider";
            imageUri = FileProvider.getUriForFile(app, authority, savePhotoFile);
        }
        return imageUri;
    }

    /**
     * 请求照相
     */
    public static Intent requestTakePhoto(Uri imageUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        return intent;
    }


    /**
     * 请求本地图片
     */
    public static Intent requestLocalPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");

        return intent;
    }

    /**
     * 请求本地资源文件
     */
    public static Intent requestContent() {
        Intent intent;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        return intent;
    }

    /**
     * @param inputUri 输入路径
     * @param outUri   输出路径
     * @return 系统压缩的intent
     */
    public static Intent cropImg(Uri inputUri, Uri outUri) {

        Intent intent = new Intent("com.android.camera.action.CROP");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);

        intent.putExtra("crop", "true");

//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectX);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);

        intent.putExtra("return-data", false);
        //黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);

        return intent;
    }


    public static void compressImage(String srcPath) throws IOException {

        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        //这里设置高度为800f
        //这里设置宽度为480f

        PhotoUtils.compressImage(srcPath, srcPath, 800f, 480f, 100, 100 * 1024);
    }


    /**
     * 压缩
     *
     * @param srcPath      输入路径
     * @param outPath      输出路径
     * @param hh           设置高度
     * @param ww           设置宽度
     * @param options      压缩比
     * @param compressSize 压缩后的文件大小
     * @throws IOException
     */
    public static void compressImage(String srcPath, String outPath, float hh, float ww, int options, long compressSize) throws IOException {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inJustDecodeBounds = false;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (options < 0) {
            options = 10;
        }

        if (compressSize < 0) {
            compressSize = 100 * 1024;
        }


        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);

        while (baos.toByteArray().length > compressSize) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options < 0) {
                break;
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(outPath);
        //不断把stream的数据写文件输出流中去
        fileOutputStream.write(baos.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();
    }


    /**
     * 压缩摄像头数据
     *
     * @param data
     * @param width
     * @param height
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream compressCameraData(byte[] data, int width, int height) {

//        Tlog.v("compressImg", " -- compressImg -- ");

        ByteArrayOutputStream compressOfYuv = compressOfYuv(data, width, height);

        if (compressOfYuv != null) {

            final byte[] byteArray = compressOfYuv.toByteArray();

            return compressOfOpts(byteArray);

        }

        return null;
    }

    /**
     * convert to jpg
     *
     * @param CameraBufferData
     * @param width
     * @param height
     * @return ByteArrayOutputStream
     */
    public static ByteArrayOutputStream compressOfYuv(byte[] CameraBufferData, int width, int height) {
        if (CameraBufferData == null || CameraBufferData.length <= 0) {
            return null;
        }
//        Tlog.v("compressImg", " before CompressOfYuv : " + CameraBufferData.length);

        YuvImage yuvImage = new YuvImage(CameraBufferData, ImageFormat.NV21, width, height, null);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, width, height), 35, byteArrayOutputStream);

//        Tlog.v("compressImg", " after CompressOfYuv : " + byteArrayOutputStream.size());
//
//        Tlog.v("compressImg",
//                " CompressOfYuv difference : " + (CameraBufferData.length - byteArrayOutputStream.size()));

        return byteArrayOutputStream;
    }

    public static ByteArrayOutputStream compressOfOpts(byte[] data) {
        if (data == null || data.length <= 0) {
            return null;
        }
        Tlog.v("compressImg", " before compressOfOpts : " + data.length);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        // // 不占用内存 只计算宽高
        // opts.inJustDecodeBounds = true;
        // Bitmap decodeByteArray = BitmapFactory.decodeByteArray(data, 0,
        // data.length, opts);
        // // 计算图片的宽高
        // int outWidth = opts.outWidth;
        // int outHeight = opts.outHeight;

        // 真正的把图片加载到内存里面去
        opts.inJustDecodeBounds = false;

        // 设置 这两个值 图片占用内存会更小
        opts.inDither = true; // 防抖动
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888; // 设置图片格式RGB8888

        opts.inPurgeable = true;
        opts.inInputShareable = false;

        // 设置压缩比
        opts.inSampleSize = 2;

        Bitmap decodeByteArrayBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decodeByteArrayBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

//        Tlog.v("compressImg", " after compressOfOpts : " + baos.size());

//        Tlog.v("compressImg", " compressOfOpts difference : " + (data.length - baos.size()));

        return baos;

    }


}
