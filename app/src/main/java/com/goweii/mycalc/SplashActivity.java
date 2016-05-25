package com.goweii.mycalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import net.youmi.android.AdManager;

public class SplashActivity extends Activity
{
    // 延时1000毫秒
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**
         * //有米广告初始化应用信息：请务必在应用第一个 Activity（启动的第一个类）的 onCreate 中调用以下代码
         * 注意：
         * appId 和 appSecret 分别为应用的发布 ID 和密钥，由有米后台自动生成，通过在有米后台 > 应用详细信息 可以获得。
         * isTestModel : 是否开启测试模式，true 为是，false 为否。（上传有米审核及发布到市场版本，请设置为 false）
         */
        AdManager.getInstance(this).init("96ec4c7f431ec865", "2780e2fb8207f90f", true);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                Intent mianIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mianIntent);
                SplashActivity.this.finish();
                SplashActivity.this.overridePendingTransition(R.anim.splash_enter, R.anim.splash_out);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
