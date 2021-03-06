package com.goweii.mycalc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenjishi.slideupdemo.SlidingUpPaneLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class MainActivity extends Activity {

    private SlidingMenu slidingMenu;

    /**
     * （暂时未实现）
     * 设置输出数字的显示格式：
     * 默认（长度大于15以科学计数法，否则以小数    DEFAULT
     * 小数(默认格式） DECIMAL
     * 百分数         PERCENTAGE
     * 科学计数法     SCIENTIFIC
     */
    private String output_number_format = "DECIMAL";

    private EditText editTextIn;
    private EditText editTextOut;
    private TextView textViewI1;
    private TextView textViewO1;
    private TextView textViewI2;
    private TextView textViewO2;
    private TextView textViewI3;
    private TextView textViewO3;
    private TextView textViewI4;
    private TextView textViewO4;
    private TextView textViewI5;
    private TextView textViewO5;
    private TextView textViewI6;
    private TextView textViewO6;
    private TextView textViewI7;
    private TextView textViewO7;
    private TextView textViewI8;
    private TextView textViewO8;

    /**
     * 双击退出时长
     */
    private long mExitTime;

    //存放历史记录！！
    private SaveAndRead saveAndRead;
    String strIn;
    String strOut;
    String strI1;
    String strO1;
    String strI2;
    String strO2;
    String strI3;
    String strO3;
    String strI4;
    String strO4;
    String strI5;
    String strO5;
    String strI6;
    String strO6;
    String strI7;
    String strO7;
    String strI8;
    String strO8;

    private static final String STRINGI = "strIn";
    private static final String STRINGR = "strOut";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slding_menu);
        //设置有米广告
        initYoumiAd();
        //设置SlidingUpPaneLayout相关动画和阴影
        setSlidingUpPaneLayout();
        slidingMenu = new SlidingMenu(this);
        //获取控件ID并设置OnClickListener监听事件
        viewSetOnClickListener();
        //还原系统杀掉前EditTextIn/Out的数据
        initEditTextInAndOut(savedInstanceState);
        //初始化历史记录textView
        initHistory();

        //=============屏蔽软键盘===可以移动光标====================
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editTextIn.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editTextIn, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        editTextOut.setInputType(InputType.TYPE_NULL);

    }

    //还原系统杀掉前EditTextIn/Out的数据
    public void initEditTextInAndOut(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            if (savedInstanceState.containsKey(STRINGI)) {
                editTextIn.setText(savedInstanceState.getString(STRINGI));
//                //设置新光标位置
//                int i = editTextIn.getText().toString().length();
//                System.out.println(i);
//                editTextIn.setSelection(i);
            }
            if (savedInstanceState.containsKey(STRINGR)) {
                editTextOut.setText(savedInstanceState.getString(STRINGR));
            }
        }
    }

    //设置有米广告
    public void initYoumiAd() {
        // 实例化广告条
        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
        // 获取要嵌入广告条的布局
        LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
        // 将广告条加入到布局中
        adLayout.addView(adView);
        // 监听广告条接口
        adView.setAdListener(new AdViewListener() {
            @Override
            public void onSwitchedAd(AdView adView) {
                Log.i("youmi", "广告条切换");
            }

            @Override
            public void onReceivedAd(AdView adView) {
                Log.i("youmi", "请求广告条成功");
            }

            @Override
            public void onFailedToReceivedAd(AdView adView) {
                Log.i("youmi", "请求广告条失败");
            }
        });
    }

    //设置SlidingUpPaneLayout相关动画和阴影
    public void setSlidingUpPaneLayout() {
        final float heightPixels = getResources().getDisplayMetrics().heightPixels;
        SlidingUpPaneLayout slidingUpPaneLayout = (SlidingUpPaneLayout) findViewById(R.id.sliding_up_layout);
        slidingUpPaneLayout.setParallaxDistance((int) (heightPixels / 2));
        slidingUpPaneLayout.setShadowResourceTop(R.drawable.slidinguppane_shadow);
    }

    //获取手机状态栏高度
    public static int getStatusBarHeight (Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    //获取控件ID并设置OnClickListener监听事件
    private void viewSetOnClickListener() {
        //获取控件ID
        editTextIn = (EditText) this.findViewById(R.id.editText_in);
        editTextOut = (EditText) this.findViewById(R.id.editText_out);
        textViewI8 = (TextView) this.findViewById(R.id.textView_i8);
        textViewO8 = (TextView) this.findViewById(R.id.textView_o8);
        textViewI7 = (TextView) this.findViewById(R.id.textView_i7);
        textViewO7 = (TextView) this.findViewById(R.id.textView_o7);
        textViewI6 = (TextView) this.findViewById(R.id.textView_i6);
        textViewO6 = (TextView) this.findViewById(R.id.textView_o6);
        textViewI5 = (TextView) this.findViewById(R.id.textView_i5);
        textViewO5 = (TextView) this.findViewById(R.id.textView_o5);
        textViewI4 = (TextView) this.findViewById(R.id.textView_i4);
        textViewO4 = (TextView) this.findViewById(R.id.textView_o4);
        textViewI3 = (TextView) this.findViewById(R.id.textView_i3);
        textViewO3 = (TextView) this.findViewById(R.id.textView_o3);
        textViewI2 = (TextView) this.findViewById(R.id.textView_i2);
        textViewO2 = (TextView) this.findViewById(R.id.textView_o2);
        textViewI1 = (TextView) this.findViewById(R.id.textView_i1);
        textViewO1 = (TextView) this.findViewById(R.id.textView_o1);

        Button button_menu_setting = (Button) this.findViewById(R.id.button_menu_setting);
        Button button_menu_about = (Button) this.findViewById(R.id.button_menu_about);
        Button button_menu_quit = (Button) this.findViewById(R.id.button_menu_quit);

        Button button_pai = (Button) this.findViewById(R.id.button_pai);
        Button button_e = (Button) this.findViewById(R.id.button_e);
        Button button_fai = (Button) this.findViewById(R.id.button_fai);
        Button button_tan = (Button) this.findViewById(R.id.button_tan);
        Button button_atan = (Button) this.findViewById(R.id.button_atan);

        Button button_lg = (Button) this.findViewById(R.id.button_lg);
        Button button_ln = (Button) this.findViewById(R.id.button_ln);
        Button button_log = (Button) this.findViewById(R.id.button_log);
        Button button_cos = (Button) this.findViewById(R.id.button_cos);
        Button button_acos = (Button) this.findViewById(R.id.button_acos);

        Button button_x2 = (Button) this.findViewById(R.id.button_x2);
        Button button_10x = (Button) this.findViewById(R.id.button_10x);
        Button button_xy = (Button) this.findViewById(R.id.button_xy);
        Button button_sin = (Button) this.findViewById(R.id.button_sin);
        Button button_asin = (Button) this.findViewById(R.id.button_asin);

        Button button_ac = (Button) this.findViewById(R.id.button_ac);
        Button button_2gx = (Button) this.findViewById(R.id.button_2gx);
        Button button_ygx = (Button) this.findViewById(R.id.button_ygx);
        Button button_bfh = (Button) this.findViewById(R.id.button_bfh);
        Button button_jc = (Button) this.findViewById(R.id.button_jc);

        Button button_7 = (Button) this.findViewById(R.id.button_7);
        Button button_8 = (Button) this.findViewById(R.id.button_8);
        Button button_9 = (Button) this.findViewById(R.id.button_9);
        Button button_qkh = (Button) this.findViewById(R.id.button_qkh);
        Button button_hkh = (Button) this.findViewById(R.id.button_hkh);

        Button button_4 = (Button) this.findViewById(R.id.button_4);
        Button button_5 = (Button) this.findViewById(R.id.button_5);
        Button button_6 = (Button) this.findViewById(R.id.button_6);
        Button button_jian = (Button) this.findViewById(R.id.button_jian);
        Button button_chu = (Button) this.findViewById(R.id.button_chu);

        Button button_1 = (Button) this.findViewById(R.id.button_1);
        Button button_2 = (Button) this.findViewById(R.id.button_2);
        Button button_3 = (Button) this.findViewById(R.id.button_3);
        Button button_jia = (Button) this.findViewById(R.id.button_jia);
        Button button_cheng = (Button) this.findViewById(R.id.button_cheng);

        Button button_zhengfu = (Button) this.findViewById(R.id.button_zhengfu);
        Button button_0 = (Button) this.findViewById(R.id.button_0);
        Button button_dian = (Button) this.findViewById(R.id.button_dian);
        Button button_deng = (Button) this.findViewById(R.id.button_deng);
        Button button_del = (Button) this.findViewById(R.id.button_del);

        Button button_history_clean = (Button) this.findViewById(R.id.button_history_clean);
        button_history_clean.setOnClickListener(new ButtonClickListener_history_clean());

        button_menu_setting.setOnClickListener(new ButtonClickListener_menu_setting());
        button_menu_about.setOnClickListener(new ButtonClickListener_menu_about());
        button_menu_quit.setOnClickListener(new ButtonClickListener_menu_quit());

        button_pai.setOnClickListener(new ButtonClickListener_pai());
        button_e.setOnClickListener(new ButtonClickListener_e());
        button_fai.setOnClickListener(new ButtonClickListener_fai());
        button_tan.setOnClickListener(new ButtonClickListener_tan());
        button_atan.setOnClickListener(new ButtonClickListener_atan());

        button_lg.setOnClickListener(new ButtonClickListener_lg());
        button_ln.setOnClickListener(new ButtonClickListener_ln());
        button_log.setOnClickListener(new ButtonClickListener_log());
        button_cos.setOnClickListener(new ButtonClickListener_cos());
        button_acos.setOnClickListener(new ButtonClickListener_acos());

        button_x2.setOnClickListener(new ButtonClickListener_x2());
        button_10x.setOnClickListener(new ButtonClickListener_10x());
        button_xy.setOnClickListener(new ButtonClickListener_xy());
        button_sin.setOnClickListener(new ButtonClickListener_sin());
        button_asin.setOnClickListener(new ButtonClickListener_asin());

        button_ac.setOnClickListener(new ButtonClickListener_ac());
        button_2gx.setOnClickListener(new ButtonClickListener_2gx());
        button_ygx.setOnClickListener(new ButtonClickListener_ygx());
        button_bfh.setOnClickListener(new ButtonClickListener_bfh());
        button_jc.setOnClickListener(new ButtonClickListener_jc());

        button_7.setOnClickListener(new ButtonClickListener_7());
        button_8.setOnClickListener(new ButtonClickListener_8());
        button_9.setOnClickListener(new ButtonClickListener_9());
        button_qkh.setOnClickListener(new ButtonClickListener_qkh());
        button_hkh.setOnClickListener(new ButtonClickListener_hkh());

        button_4.setOnClickListener(new ButtonClickListener_4());
        button_5.setOnClickListener(new ButtonClickListener_5());
        button_6.setOnClickListener(new ButtonClickListener_6());
        button_jian.setOnClickListener(new ButtonClickListener_jian());
        button_chu.setOnClickListener(new ButtonClickListener_chu());

        button_1.setOnClickListener(new ButtonClickListener_1());
        button_2.setOnClickListener(new ButtonClickListener_2());
        button_3.setOnClickListener(new ButtonClickListener_3());
        button_jia.setOnClickListener(new ButtonClickListener_jia());
        button_cheng.setOnClickListener(new ButtonClickListener_cheng());

        button_zhengfu.setOnClickListener(new ButtonClickListener_zhengfu());
        button_0.setOnClickListener(new ButtonClickListener_0());
        button_dian.setOnClickListener(new ButtonClickListener_dian());
        button_deng.setOnClickListener(new ButtonClickListener_deng());
        button_del.setOnClickListener(new ButtonClickListener_del());

        //--------历史记录按钮----------------
        textViewI8.setOnClickListener(new ButtonClickListener_i8());
        textViewO8.setOnClickListener(new ButtonClickListener_o8());
        textViewI7.setOnClickListener(new ButtonClickListener_i7());
        textViewO7.setOnClickListener(new ButtonClickListener_o7());
        textViewI6.setOnClickListener(new ButtonClickListener_i6());
        textViewO6.setOnClickListener(new ButtonClickListener_o6());
        textViewI5.setOnClickListener(new ButtonClickListener_i5());
        textViewO5.setOnClickListener(new ButtonClickListener_o5());
        textViewI4.setOnClickListener(new ButtonClickListener_i4());
        textViewO4.setOnClickListener(new ButtonClickListener_o4());
        textViewI3.setOnClickListener(new ButtonClickListener_i3());
        textViewO3.setOnClickListener(new ButtonClickListener_o3());
        textViewI2.setOnClickListener(new ButtonClickListener_i2());
        textViewO2.setOnClickListener(new ButtonClickListener_o2());
        textViewI1.setOnClickListener(new ButtonClickListener_i1());
        textViewO1.setOnClickListener(new ButtonClickListener_o1());

        editTextOut.setOnClickListener(null);
    }

    //初始化历史记录textView
    public void initHistory() {
        //----------------初始化历史记录，恢复输入输出框-----------------------------
        saveAndRead = new SaveAndRead(this);
        Map<String, String> params;
        params = saveAndRead.read8();
        textViewI8.setText(params.get("strI8"));
        textViewO8.setText(params.get("strO8"));
        params = saveAndRead.read7();
        textViewI7.setText(params.get("strI7"));
        textViewO7.setText(params.get("strO7"));
        params = saveAndRead.read6();
        textViewI6.setText(params.get("strI6"));
        textViewO6.setText(params.get("strO6"));
        params = saveAndRead.read5();
        textViewI5.setText(params.get("strI5"));
        textViewO5.setText(params.get("strO5"));
        params = saveAndRead.read4();
        textViewI4.setText(params.get("strI4"));
        textViewO4.setText(params.get("strO4"));
        params = saveAndRead.read3();
        textViewI3.setText(params.get("strI3"));
        textViewO3.setText(params.get("strO3"));
        params = saveAndRead.read2();
        textViewI2.setText(params.get("strI2"));
        textViewO2.setText(params.get("strO2"));
        params = saveAndRead.read1();
        textViewI1.setText(params.get("strI1"));
        textViewO1.setText(params.get("strO1"));
        params = saveAndRead.read0();
        editTextIn.setText(params.get("strIn"));
        editTextOut.setText(params.get("strOut"));

        //--------------------初始赋�?，以备�?出时历史记录清空---------------------
        strI8 = textViewI8.getText().toString();
        strO8 = textViewO8.getText().toString();
        strI7 = textViewI7.getText().toString();
        strO7 = textViewO7.getText().toString();
        strI6 = textViewI6.getText().toString();
        strO6 = textViewO6.getText().toString();
        strI5 = textViewI5.getText().toString();
        strO5 = textViewO5.getText().toString();
        strI4 = textViewI4.getText().toString();
        strO4 = textViewO4.getText().toString();
        strI3 = textViewI3.getText().toString();
        strO3 = textViewO3.getText().toString();
        strI2 = textViewI2.getText().toString();
        strO2 = textViewO2.getText().toString();
        strI1 = textViewI1.getText().toString();
        strO1 = textViewO1.getText().toString();
        strIn = editTextIn.getText().toString();
        strOut = editTextOut.getText().toString();
    }

    private final class ButtonClickListener_menu_setting implements View.OnClickListener {
        public void onClick(View v) {
            slidingMenu.showContent(true);
        }
    }

    private final class ButtonClickListener_menu_about implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.about_enter, R.anim.about_out);
        }
    }

    private final class ButtonClickListener_menu_quit implements View.OnClickListener {
        public void onClick(View v) {
            saveAndRead.save(strIn, strOut,
                    strI1, strO1,
                    strI2, strO2,
                    strI3, strO3,
                    strI4, strO4,
                    strI5, strO5,
                    strI6, strO6,
                    strI7, strO7,
                    strI8, strO8);
            MainActivity.this.finish();
        }
    }

    // ---------------------------------------------------------------------------------
    private void ButtonClickListenerInputProcess(String strInputCurrent){
        //赋初始值
        String inputCurrent = strInputCurrent;
        //用于输入字符
        Editable editable = editTextIn.getEditableText();
        //获取光标位置和已输入表达式
        int index = editTextIn.getSelectionStart();
        String inputExpression = editTextIn.getText().toString();
        //获取新光标位置，并设置
        WithinOperator withinOperator = new WithinOperator(inputExpression, index);
        index = withinOperator.getNewIndex();
        editTextIn.setSelection(index);
        //获取新输入字符
        NewInputCurrent newInputCurrent = new NewInputCurrent(inputExpression, inputCurrent, index);
        inputCurrent = newInputCurrent.getNewInputCurrent();
        //判断新字符是否为空，为空不可输入报错
        if (inputCurrent == null){
            editTextOut.setText(getResources().getString(R.string.error_invalid));
        } else {
            editable.insert(index, inputCurrent);
            inputExpression = editTextIn.getText().toString();
            InputConnect inputConnect = new InputConnect(inputExpression);
            String strEditTextOut = inputConnect.checkConnect();
            editTextOut.setText(strEditTextOut);
        }
    }

    private final class ButtonClickListener_1 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("1");
        }
    }

    private final class ButtonClickListener_2 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("2");
        }
    }

    private final class ButtonClickListener_3 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("3");
        }
    }

    private final class ButtonClickListener_4 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("4");
        }
    }

    private final class ButtonClickListener_5 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("5");
        }
    }

    private final class ButtonClickListener_6 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("6");
        }
    }

    private final class ButtonClickListener_7 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("7");
        }
    }

    private final class ButtonClickListener_8 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("8");
        }
    }

    private final class ButtonClickListener_9 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("9");
        }
    }

    private final class ButtonClickListener_0 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("0");
        }
    }

    ///////// 123+456+789 -----> 123+(-456)+789
    private final class ButtonClickListener_zhengfu implements View.OnClickListener {
        public void onClick(View v) {
            int index = editTextIn.getSelectionStart();
            Editable edit = editTextIn.getEditableText();
            String strEditTextIn = editTextIn.getText().toString();
            WithinOperator withinOperator = new WithinOperator(strEditTextIn, index);
            int newIndex = withinOperator.getNewIndex();
            if (index == 0) {
            } else if (newIndex != index) {
                editTextIn.setSelection(newIndex);
            }
            if (editTextIn.length() == 2 &&
                    edit.charAt(index - 1) == '-' &&
                    edit.charAt(index - 2) == '(') {
                edit.delete(index - 2, index);
            } else if (editTextIn.length() > 0) {
                if (index == 0) {
                    //======数字前插入====1234===（-1234）==================
                    if (edit.charAt(index) >= '0' && edit.charAt(index) <= '9') {
                        edit.insert(index, "(-");
                        int i;
                        for (i = index; i < strEditTextIn.length(); i++) {
                            if ((strEditTextIn.charAt(i) >= '0' && strEditTextIn.charAt(i) <= '9') ||
                                    strEditTextIn.charAt(i) == '.') {
                            } else {
                                break;
                            }
                        }
                        edit.insert(i + 2, ")");
                    }
                    //======（前输入=======（1234）====（-（1234））=====
                    else if (edit.charAt(index) == '(') {
                        edit.insert(index, "(-");
                        int num1 = 0;
                        int num2 = 0;
                        int i;
                        for (i = index; i < strEditTextIn.length(); i++) {
                            if (strEditTextIn.charAt(i) == '(')
                                num1++;
                            if (strEditTextIn.charAt(i) == ')')
                                num2++;
                            if (num1 == num2) {
                                edit.insert(i + 2, ")");
                                break;
                            }
                        }
                    }
                } else {
                    //======删除====（-1234）===1234===========
                    if (index > 1 &&
                            (edit.charAt(index - 2) == '(' && edit.charAt(index - 1) == '-')) {
                        if (edit.charAt(index) >= '0' && edit.charAt(index) <= '9') {
                            edit.delete(index - 2, index);
                            int i;
                            for (i = index; i < strEditTextIn.length(); i++) {
                                if ((strEditTextIn.charAt(i) >= '0' && strEditTextIn.charAt(i) <= '9') ||
                                        strEditTextIn.charAt(i) == '.') {
                                } else
                                    break;
                            }
                            i = i - 1;
                            edit.delete(i - 1, i);
                        }
                        //======删除====（-（1234））====（1234）===========
                        else if (edit.charAt(index) == '(') {
                            edit.delete(index - 2, index);
                            int num1 = 0;
                            int num2 = 0;
                            int i;
                            for (i = index; i < strEditTextIn.length(); i++) {
                                if (strEditTextIn.charAt(i) == '(')
                                    num1++;
                                if (strEditTextIn.charAt(i) == ')')
                                    num2++;
                                if (num1 == num2) {
                                    i = i - 1;
                                    edit.delete(i - 1, i);
                                    break;
                                }
                            }
                        } else {
                        }
                    } else {
                        //======符号前添加==============================
                        if (index > 0 && index < strEditTextIn.length() &&
                                (edit.charAt(index - 1) == '(' ||
                                        edit.charAt(index - 1) == '+' ||
                                        edit.charAt(index - 1) == '-' ||
                                        edit.charAt(index - 1) == '×' ||
                                        edit.charAt(index - 1) == '÷' ||
                                        edit.charAt(index - 1) == 'n' ||
                                        edit.charAt(index - 1) == 's' ||
                                        edit.charAt(index - 1) == 'g' ||
                                        edit.charAt(index - 1) == '㏑' ||
                                        edit.charAt(index - 1) == '㏒' ||
                                        edit.charAt(index - 1) == '√')) {
                            if (edit.charAt(index) >= '0' && edit.charAt(index) <= '9') {
                                edit.insert(index, "(-");
                                int i;
                                for (i = index; i < strEditTextIn.length(); i++) {
                                    if ((strEditTextIn.charAt(i) >= '0' && strEditTextIn.charAt(i) <= '9') ||
                                            strEditTextIn.charAt(i) == '.') {
                                    } else
                                        break;
                                }
                                edit.insert(i + 2, ")");
                            } else if (edit.charAt(index) == '(') {
                                edit.insert(index, "(-");
                                int num1 = 0;
                                int num2 = 0;
                                int i;
                                for (i = index; i < strEditTextIn.length(); i++) {
                                    if (strEditTextIn.charAt(i) == '(')
                                        num1++;
                                    if (strEditTextIn.charAt(i) == ')')
                                        num2++;
                                    if (num1 == num2) {
                                        edit.insert(i + 2, ")");
                                        break;
                                    }
                                }
                            }
                        }
                        if (index == strEditTextIn.length() &&
                                (edit.charAt(index - 1) == '(' ||
                                        edit.charAt(index - 1) == '+' ||
                                        edit.charAt(index - 1) == '-' ||
                                        edit.charAt(index - 1) == '×' ||
                                        edit.charAt(index - 1) == '÷' ||
                                        edit.charAt(index - 1) == 'n' ||
                                        edit.charAt(index - 1) == 's' ||
                                        edit.charAt(index - 1) == 'g' ||
                                        edit.charAt(index - 1) == '㏑' ||
                                        edit.charAt(index - 1) == '㏒' ||
                                        edit.charAt(index - 1) == '√')) {
                            edit.insert(index, "(-");
                        }
                    }
                }
            } else {
                edit.insert(index, "(-");
            }
            String strEditTextInNew = editTextIn.getText().toString();
            InputConnect inputConnect = new InputConnect(strEditTextInNew);
            String strEditTextOut = inputConnect.checkConnect();
            editTextOut.setText(strEditTextOut);
        }
    }

    private final class ButtonClickListener_dian implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess(".");
        }
    }

    private final class ButtonClickListener_pai implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("π");
        }
    }

    private final class ButtonClickListener_e implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("e");
        }
    }

    private final class ButtonClickListener_fai implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("Φ");
        }
    }

    private final class ButtonClickListener_bfh implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("%");
        }
    }

    private final class ButtonClickListener_x2 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("²");
        }
    }

    private final class ButtonClickListener_xy implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("^");
        }
    }

    private final class ButtonClickListener_10x implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("10^");
        }
    }

    private final class ButtonClickListener_log implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("log");
        }
    }

    private final class ButtonClickListener_lg implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("lg");
        }
    }

    private final class ButtonClickListener_ln implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("ln");
        }
    }

    private final class ButtonClickListener_2gx implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("²√");
        }
    }

    private final class ButtonClickListener_ygx implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("√");
        }
    }

    private final class ButtonClickListener_jc implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("!");
        }
    }

    private final class ButtonClickListener_sin implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("sin");
        }
    }

    private final class ButtonClickListener_cos implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("cos");
        }
    }

    private final class ButtonClickListener_tan implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("tan");
        }
    }

    private final class ButtonClickListener_asin implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("asin");
        }
    }

    private final class ButtonClickListener_acos implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("acos");
        }
    }

    private final class ButtonClickListener_atan implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("atan");
        }
    }


    private final class ButtonClickListener_qkh implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("(");
        }
    }

    private final class ButtonClickListener_hkh implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess(")");
        }
    }

    private final class ButtonClickListener_jia implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("+");
        }
    }

    private final class ButtonClickListener_jian implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("-");
        }
    }

    private final class ButtonClickListener_cheng implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("×");
        }
    }

    private final class ButtonClickListener_chu implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerInputProcess("÷");
        }
    }

    private final class ButtonClickListener_del implements View.OnClickListener {
        public void onClick(View v) {
            int index = editTextIn.getSelectionStart();
            Editable edit = editTextIn.getEditableText();
            String strEditTextIn = editTextIn.getText().toString();
            WithinOperator withinOperator = new WithinOperator(strEditTextIn, index);
            int newIndex = withinOperator.getNewIndex();
            if (index == 0) {
            }
            if (index > 0) {
                if (newIndex != index) {
                    editTextIn.setSelection(newIndex);
                    edit.delete(newIndex - withinOperator.getDeleteNum(), newIndex);
                    String strEditTextInNew = editTextIn.getText().toString();
                    InputConnect inputConnect = new InputConnect(strEditTextInNew);
                    String strEditTextOut = inputConnect.checkConnect();
                    editTextOut.setText(strEditTextOut);
                }
                if (newIndex == index) {
                    if (newIndex > 3 && ((strEditTextIn.charAt(newIndex - 4) == 'a' && strEditTextIn.charAt(newIndex - 3) == 't'
                            && strEditTextIn.charAt(newIndex - 2) == 'a' && strEditTextIn.charAt(newIndex - 1) == 'n')
                            || (strEditTextIn.charAt(newIndex - 4) == 'a' && strEditTextIn.charAt(newIndex - 3) == 'c'
                            && strEditTextIn.charAt(newIndex - 2) == 'o' && strEditTextIn.charAt(newIndex - 1) == 's')
                            || (strEditTextIn.charAt(newIndex - 4) == 'a' && strEditTextIn.charAt(newIndex - 3) == 's'
                            && strEditTextIn.charAt(newIndex - 2) == 'i' && strEditTextIn.charAt(newIndex - 1) == 'n'))) {
                        edit.delete(newIndex - 4, newIndex);
                        String strEditTextInNew = editTextIn.getText().toString();
                        InputConnect inputConnect = new InputConnect(strEditTextInNew);
                        String strEditTextOut = inputConnect.checkConnect();
                        editTextOut.setText(strEditTextOut);
                    } else if (newIndex > 2 && ((strEditTextIn.charAt(newIndex - 3) == 'l' && strEditTextIn.charAt(newIndex - 2) == 'o'
                            && strEditTextIn.charAt(newIndex - 1) == 'g')
                            || (strEditTextIn.charAt(newIndex - 3) == 't' && strEditTextIn.charAt(newIndex - 2) == 'a'
                            && strEditTextIn.charAt(newIndex - 1) == 'n')
                            || (strEditTextIn.charAt(newIndex - 3) == 'c' && strEditTextIn.charAt(newIndex - 2) == 'o'
                            && strEditTextIn.charAt(newIndex - 1) == 's')
                            || (strEditTextIn.charAt(newIndex - 3) == 's' && strEditTextIn.charAt(newIndex - 2) == 'i'
                            && strEditTextIn.charAt(newIndex - 1) == 'n'))) {
                        edit.delete(newIndex - 3, newIndex);
                        String strEditTextInNew = editTextIn.getText().toString();
                        InputConnect inputConnect = new InputConnect(strEditTextInNew);
                        String strEditTextOut = inputConnect.checkConnect();
                        editTextOut.setText(strEditTextOut);
                    } else if (newIndex > 1 && ((strEditTextIn.charAt(newIndex - 2) == 'l' && strEditTextIn.charAt(newIndex - 1) == 'n')
                            || (strEditTextIn.charAt(newIndex - 2) == 'l' && strEditTextIn.charAt(newIndex - 1) == 'g')
                            || (strEditTextIn.charAt(newIndex - 2) == '²' && strEditTextIn.charAt(newIndex - 1) == '√'))) {
                        edit.delete(newIndex - 2, newIndex);
                        String strEditTextInNew = editTextIn.getText().toString();
                        InputConnect inputConnect = new InputConnect(strEditTextInNew);
                        String strEditTextOut = inputConnect.checkConnect();
                        editTextOut.setText(strEditTextOut);
                    } else {
                        edit.delete(newIndex - 1, newIndex);
                        String strEditTextInNew = editTextIn.getText().toString();
                        InputConnect inputConnect = new InputConnect(strEditTextInNew);
                        String strEditTextOut = inputConnect.checkConnect();
                        editTextOut.setText(strEditTextOut);
                    }
                }
            }
        }
    }

    private final class ButtonClickListener_ac implements View.OnClickListener {
        public void onClick(View v) {
            editTextIn.setText("");
            editTextOut.setText("");
        }
    }

    /**
     * 等号得到结果！！！
     */
    private final class ButtonClickListener_deng implements View.OnClickListener {
        public void onClick(View v) {
            if (editTextIn.length() == 0) {
            } else {
                String strEditTextIn = editTextIn.getText().toString();
                Doctor doctor = new Doctor(strEditTextIn);
                InputConnect inputConnect = new InputConnect(strEditTextIn);
                String strEditTextOut = inputConnect.checkConnect();

                int num1 = 0;
                int num2 = 0;
                char[] chars = strEditTextIn.toCharArray();
                for (int i = 0; i < chars.length; i++)
                    if (chars[i] == '(')
                        num1++;
                for (int i = 0; i < chars.length; i++)
                    if (chars[i] == ')')
                        num2++;
                if (num1 > num2) {
                    for(int i = 0; i < num1 - num2; i ++) {
                        strEditTextIn = strEditTextIn + ")";
                    }
                } else if(num1 < num2) {
                    for(int i = 0; i < num1 - num2; i ++) {
                        strEditTextIn = "(" + strEditTextIn;
                    }
                }

                if (strEditTextOut.indexOf(":") > -1) {
                    editTextOut.setText(strEditTextOut);
                } else {
                    strEditTextIn = doctor.getNewInputExpression();
                    Expression expression = new Expression(strEditTextIn);//计算结果
                    String result = expression.getresult();    //得到结果
                    double doubleResult = Double.parseDouble(result);
                    if(Double.isNaN(doubleResult)){
                        result = getResources().getString(R.string.error_nan);
                    } else if(Double.isInfinite(doubleResult)){
                        result = getResources().getString(R.string.error_infinite);
                    } else {
                        OutputFormat outputFormat = new OutputFormat(output_number_format, result);
                        result = outputFormat.getResult();
                        result = doctor.getResult(result);
                    }
                    editTextOut.setText(result);
                }
            }
            historyRefresh();
        }
    }
    private void historyRefresh(){
        //-----历史记录-----
        strIn = editTextIn.getText().toString();
        strOut = editTextOut.getText().toString();
        strI1 = textViewI1.getText().toString();
        strO1 = textViewO1.getText().toString();
        strI2 = textViewI2.getText().toString();
        strO2 = textViewO2.getText().toString();
        strI3 = textViewI3.getText().toString();
        strO3 = textViewO3.getText().toString();
        strI4 = textViewI4.getText().toString();
        strO4 = textViewO4.getText().toString();
        strI5 = textViewI5.getText().toString();
        strO5 = textViewO5.getText().toString();
        strI6 = textViewI6.getText().toString();
        strO6 = textViewO6.getText().toString();
        strI7 = textViewI7.getText().toString();
        strO7 = textViewO7.getText().toString();
        strI8 = textViewI8.getText().toString();
        strO8 = textViewO8.getText().toString();

        strI8 = strI7;
        strO8 = strO7;
        strI7 = strI6;
        strO7 = strO6;
        strI6 = strI5;
        strO6 = strO5;
        strI5 = strI4;
        strO5 = strO4;
        strI4 = strI3;
        strO4 = strO3;
        strI3 = strI2;
        strO3 = strO2;
        strI2 = strI1;
        strO2 = strO1;
        strI1 = strIn;
        strO1 = strOut;

        textViewI8.setText(strI8);
        textViewO8.setText(strO8);
        textViewI7.setText(strI7);
        textViewO7.setText(strO7);
        textViewI6.setText(strI6);
        textViewO6.setText(strO6);
        textViewI5.setText(strI5);
        textViewO5.setText(strO5);
        textViewI4.setText(strI4);
        textViewO4.setText(strO4);
        textViewI3.setText(strI3);
        textViewO3.setText(strO3);
        textViewI2.setText(strI2);
        textViewO2.setText(strO2);
        textViewI1.setText(strI1);
        textViewO1.setText(strO1);
    }

    //---------历史记录按钮时间。上屏！-------------
    private void ButtonClickListenerHistoryInputProcess(TextView textView){
        String strHistory = textView.getText().toString();
        if (textView.length() > 0
                && !(strHistory.indexOf(":")  > -1
                || strHistory.indexOf(":")  > -1)) {
            int index = editTextIn.getSelectionStart();
            Editable edit = editTextIn.getEditableText();
            if (index == 0) {
                edit.insert(index, "(" + strHistory + ")");
            } else if (edit.charAt(index - 1) != '.') {
                edit.insert(index, "(" + strHistory + ")");
            }
        }
    }

    private final class ButtonClickListener_i1 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI1);
        }
    }

    private final class ButtonClickListener_o1 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO1);
        }
    }

    private final class ButtonClickListener_i2 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI2);
        }
    }

    private final class ButtonClickListener_o2 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO2);
        }
    }

    private final class ButtonClickListener_i3 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI3);
        }
    }

    private final class ButtonClickListener_o3 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO3);
        }
    }

    private final class ButtonClickListener_i4 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI4);
        }
    }

    private final class ButtonClickListener_o4 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO4);
        }
    }

    private final class ButtonClickListener_i5 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI5);
        }
    }

    private final class ButtonClickListener_o5 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO5);
        }
    }

    private final class ButtonClickListener_i6 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI6);
        }
    }

    private final class ButtonClickListener_o6 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO6);
        }
    }

    private final class ButtonClickListener_i7 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI7);
        }
    }

    private final class ButtonClickListener_o7 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO7);
        }
    }

    private final class ButtonClickListener_i8 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewI8);
        }
    }

    private final class ButtonClickListener_o8 implements View.OnClickListener {
        public void onClick(View v) {
            ButtonClickListenerHistoryInputProcess(textViewO8);
        }
    }


    //--------------------再按一次退出---------------------------------------
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 800) {
                Toast.makeText(this, R.string.doubletoexit, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                saveAndRead.save(strIn, strOut,
                        strI1, strO1,
                        strI2, strO2,
                        strI3, strO3,
                        strI4, strO4,
                        strI5, strO5,
                        strI6, strO6,
                        strI7, strO7,
                        strI8, strO8);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //--------------关闭应用，杀掉进程---------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //-------------在系统杀死进程前，储存用户在edit1中输入了一半的数据---------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        strIn = editTextIn.getText().toString();
        strOut = editTextOut.getText().toString();

        saveAndRead.save(strIn, strOut, strI1, strO1, strI2, strO2,
                strI3, strO3, strI4, strO4, strI5, strO5,
                strI6, strO6, strI7, strO7, strI8, strO8);
    }

    /**
     * 清空历史记录
     * 若清空后，未进行任何计算，则不保存清空状态，即下次打开任然为之前未清空状态
     */
    private final class ButtonClickListener_history_clean implements View.OnClickListener {
        public void onClick(View v) {
            textViewI8.setText("");
            textViewO8.setText("");
            textViewI7.setText("");
            textViewO7.setText("");
            textViewI6.setText("");
            textViewO6.setText("");
            textViewI5.setText("");
            textViewO5.setText("");
            textViewI4.setText("");
            textViewO4.setText("");
            textViewI3.setText("");
            textViewO3.setText("");
            textViewI2.setText("");
            textViewO2.setText("");
            textViewI1.setText("");
            textViewO1.setText("");
        }
    }

}
