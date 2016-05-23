package com.goweii.mycalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity
{
    // 延时1500毫秒
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
