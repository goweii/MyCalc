package com.goweii.mycalc;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SaveAndRead {

    private Context context;

    public SaveAndRead (Context context)
    {
        this.context = context;
    }

    /**
     * 保存历史记录！！！
     */
    public void save(String strIn, String strOut,
                     String strI1, String strO1,
                     String strI2, String strO2,
                     String strI3, String strO3,
                     String strI4, String strO4,
                     String strI5, String strO5,
                     String strI6, String strO6,
                     String strI7, String strO7,
                     String strI8, String strO8)
    {
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("strIn", strIn);
        editor.putString("strOut", strOut);
        editor.putString("strI1", strI1);
        editor.putString("strO1", strO1);
        editor.putString("strI2", strI2);
        editor.putString("strO2", strO2);
        editor.putString("strI3", strI3);
        editor.putString("strO3", strO3);
        editor.putString("strI4", strI4);
        editor.putString("strO4", strO4);
        editor.putString("strI5", strI5);
        editor.putString("strO5", strO5);
        editor.putString("strI6", strI6);
        editor.putString("strO6", strO6);
        editor.putString("strI7", strI7);
        editor.putString("strO7", strO7);
        editor.putString("strI8", strI8);
        editor.putString("strO8", strO8);
        editor.commit();
    }
    /**
     * 获取历史记录，并打印在edit中！！
     * 分9次获取！！
     * @return
     */
    public Map<String, String> read0()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strIn", preferences.getString("strIn", ""));
        params.put("strOut", preferences.getString("strOut", ""));
        return params;
    }
    public Map<String, String> read1()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI1", preferences.getString("strI1", ""));
        params.put("strO1", preferences.getString("strO1", ""));
        return params;
    }
    public Map<String, String> read2()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI2", preferences.getString("strI2", ""));
        params.put("strO2", preferences.getString("strO2", ""));
        return params;
    }
    public Map<String, String> read3()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI3", preferences.getString("strI3", ""));
        params.put("strO3", preferences.getString("strO3", ""));
        return params;
    }
    public Map<String, String> read4()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI4", preferences.getString("strI4", ""));
        params.put("strO4", preferences.getString("strO4", ""));
        return params;
    }
    public Map<String, String> read5()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI5", preferences.getString("strI5", ""));
        params.put("strO5", preferences.getString("strO5", ""));
        return params;
    }
    public Map<String, String> read6()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI6", preferences.getString("strI6", ""));
        params.put("strO6", preferences.getString("strO6", ""));
        return params;
    }
    public Map<String, String> read7()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI7", preferences.getString("strI7", ""));
        params.put("strO7", preferences.getString("strO7", ""));
        return params;
    }
    public Map<String, String> read8()
    {
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences =
                context.getSharedPreferences("saveandread", context.MODE_PRIVATE);
        params.put("strI8", preferences.getString("strI8", ""));
        params.put("strO8", preferences.getString("strO8", ""));
        return params;
    }
}
