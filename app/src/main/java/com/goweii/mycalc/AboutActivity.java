package com.goweii.mycalc;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity  extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button button1 = (Button) this.findViewById(R.id.button1);
        button1.setOnClickListener(new ButtonClickListener1());
    }

    private final class ButtonClickListener1 implements View.OnClickListener
    {
        public void onClick(View v)
        {
            AboutActivity.this.finish();
            AboutActivity.this.overridePendingTransition(R.anim.splash_enter, R.anim.splash_out);
        }
    }
}
