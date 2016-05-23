/**
 * 判断当前键入字符可否与前面字符连接
 */

package com.goweii.mycalc;

public class CanFrontConnect {
    String inputExpression;
    String inputCurrent;
    int judge = 0;
    int index;

    public CanFrontConnect(String inputExpression, String inputCurrent,int index){
        this.inputExpression = inputExpression;
        this.inputCurrent = inputCurrent;
        this.index = index;
    }

    public boolean getJudge(){
        return true;
    }

    

}
