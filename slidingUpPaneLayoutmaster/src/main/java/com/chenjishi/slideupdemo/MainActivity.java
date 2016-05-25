package com.chenjishi.slideupdemo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
//    /**
//     * 计算应用区域高度（仅去除状态栏后高度）
//     */
//    public int appArea;
//    /**
//     * 获取状态栏高度
//     */
//    Rect frame = new Rect();
//    int statusBarHeight;
//    int screenHeight;
//
//    public int getAppArea(int screenHeight,int statusBarHeight) {
//        /**
//         * 计算应用区域高度
//         */
//        appArea = screenHeight - statusBarHeight;
//        return appArea;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

//      getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//      statusBarHeight = frame.top;
//      /**
//       * 获取屏幕高度
//       */
//      DisplayMetrics dm = new DisplayMetrics();
//      this.getWindowManager().getDefaultDisplay().getMetrics(dm);//this指当前activity
//      screenHeight = dm.heightPixels;
////
//      Log.i("appArea", "" + getAppArea(statusBarHeight, screenHeight));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final float density = getResources().getDisplayMetrics().density;

        SlidingUpPaneLayout slidingUpPaneLayout = (SlidingUpPaneLayout) findViewById(R.id.sliding_up_layout);
        slidingUpPaneLayout.setParallaxDistance((int) (200 * density));
        slidingUpPaneLayout.setShadowResourceTop(R.drawable.shadow_top);

        /**
         * limit scroll zone to 32dp, if you want whole view can scroll
         * just ignore this method, don't call it
         */
//        slidingUpPaneLayout.setEdgeSize((int) (density * 32));

        slidingUpPaneLayout.openPane();

    }
}
