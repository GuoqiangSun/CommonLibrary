package cn.com.common.test.testScrollView;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;

import cn.com.common.test.R;
import cn.com.common.view.scrollview.AutoScrollMarqueeView;

public class ScrollViewActivity extends AppCompatActivity {

    ViewGroup rootView;
    AutoScrollMarqueeView marqueeView;
    RelativeLayout.LayoutParams marqueeParams;
    String originalStr;
    String showStr;
    String secondShowStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);


        rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        addView();

    }


    private void addView() {

        originalStr = getShowStr().trim();
//        String  showStr = originalStr.substring(0,500);
//        String secondShowStr = originalStr.substring(500,1000);
        showStr = "给我一片蓝天 一轮初升的太阳,给我一";
        secondShowStr = "片绿草 绵延向远方，给我一只雄鹰 一个威武的汉子.";
//                RelativeLayout.LayoutParams marqueeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        marqueeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 300);
        marqueeParams.setMargins(0, 100, 0, 0);

        marqueeView = new AutoScrollMarqueeView(getApplicationContext());
        marqueeView.setBackgroundColor(Color.parseColor("#FF00FF"));
        marqueeView.setLayoutParams(marqueeParams);
        marqueeView.setSpeed(8);
        marqueeView.setTextSize(130);

//        marqueeView.setText(showStr+secondShowStr);
//        marqueeView.setText(originalStr.substring(0, 5));
        marqueeView.setText(originalStr);

        marqueeView.setTextColor("#000000");
        marqueeView.requestFocus();
        rootView.addView(marqueeView);
    }


    private String getShowStr() {

        AssetManager assets = getAssets();
        try {
            InputStream taomaxuan = assets.open("taomaxuan");
            StringBuffer sb = new StringBuffer();

            int length = taomaxuan.available();

            byte[] buf = new byte[length];
            int read = -1;
            while ((read = taomaxuan.read(buf)) != -1) {

                String s;
                if (read == length) {
                    s = new String(buf, "utf-8");
                } else {
                    byte[] tmpBuf = new byte[read];
                    System.arraycopy(buf, 0, tmpBuf, 0, read);
                    s = new String(tmpBuf, "utf-8");
                }
                sb.append(s);
            }
            taomaxuan.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }

    }


}
