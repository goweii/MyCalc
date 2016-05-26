/**
 * 判断输入表达式是否有连接错误
 */

package com.goweii.mycalc;

public class InputConnect {
    private String inputExpression = "";
    private String correct = "TRUE";

    /**
     * @param inputExpression 已输入的表达式
     */
    public InputConnect(String inputExpression) {
        this.inputExpression = inputExpression;
    }

    /**
     * 获取点击按键后合成的表达式
     *
     * @return
     */
    public String getInputExpression() {
        return inputExpression;
    }

    public String checkConnect() {
        if (inputExpression.length() == 0) {
        } else {
            if (correct == "TRUE") {
                checkEndWithNumber();
                if (correct == "TRUE") {
                    checkFirstCharacter();
                    if (correct == "TRUE") {
                        CheckBracketSymmetry();
                        if (correct == "TRUE") {
                            CheckBracketOrder();
                            if (correct == "TRUE") {
                                CheckDecimalPoint();
                                if (correct == "TRUE") {
                                    CheckConnectOrder();
                                }
                            }
                        }
                    }
                }
            }
        }
        return correct;
    }
    //判断输入是否以数字结尾！！
    public void checkEndWithNumber()
    {
        if (!(inputExpression.endsWith("π") || inputExpression.endsWith("e")
                || inputExpression.endsWith("Φ") || inputExpression.endsWith("%")
                || inputExpression.endsWith("²") || inputExpression.endsWith("!")
                || inputExpression.endsWith("1") || inputExpression.endsWith("2")
                || inputExpression.endsWith("3") || inputExpression.endsWith("4")
                || inputExpression.endsWith("5") || inputExpression.endsWith("6")
                || inputExpression.endsWith("7") || inputExpression.endsWith("8")
                || inputExpression.endsWith("9") || inputExpression.endsWith("0")
                || inputExpression.endsWith(")"))) {
            correct = "ERROR: cannot end with that";
        } else {
            correct = "TRUE";
        }
    }
    //判断输入首部是否真确
    public void checkFirstCharacter()
    {
        if (inputExpression.length() > 3 && inputExpression.charAt(0) == 'l'
                && inputExpression.charAt(1) == 'o' && inputExpression.charAt(2) == 'g') {
            correct = "ERROR: cannot start with 'log'";
        } else {
            correct = "TRUE";
        }
    }
    //判断（）的数量是否相等！！
    public void CheckBracketSymmetry()
    {
        int num1 = 0;
        int num2 = 0;
        char[] chars = inputExpression.toCharArray();
        for (int i = 0; i < chars.length; i++)
            if (chars[i] == '(')
                num1++;
        for (int i = 0; i < chars.length; i++)
            if (chars[i] == ')')
                num2++;
        if (num1 > num2) {
            correct = "WARNING: '(' > ')' 默认在末尾追加 ')'";
        } else if(num1 < num2) {
            correct = "WARNING: '(' < ')' 默认在开始追加 '('";
        } else {
            correct = "TRUE";
        }
    }
    //判断（）的顺序是否正确！
    public void CheckBracketOrder()
    {
        int num1 = 0;
        int num2 = 0;
        char[] chars = inputExpression.toCharArray();
        for (int j = 0; j < chars.length; j++) {
            for (int i = 0; i < chars.length - j; i++)
                if (chars[i] == '(')
                    num1++;
            for (int i = 0; i < chars.length - j; i++)
                if (chars[i] == ')')
                    num2++;
            if (num1 < num2) {
                correct = "ERROR: the order of '(' and ')'";
                break;
            } else {
                correct = "TRUE";
            }
        }
    }
    //判断是否有两个小数点的数据！！！
    public void CheckDecimalPoint()
    {
        String doublePoint = inputExpression.replaceAll("[0-9]+\\.[0-9]+\\.[0-9]", "double.");
        if (doublePoint.indexOf("double.") > -1) {
            correct = "ERROR: a number has more '.'";
        } else {
            correct = "TRUE";
        }
    }
    //判断是否有连接错误！！！
    public void CheckConnectOrder()
    {
        String string = inputExpression;
        string = string.replace("asin", "r");
        string = string.replace("acos", "r");
        string = string.replace("atan", "r");
        string = string.replace("sin", "r");
        string = string.replace("cos", "r");
        string = string.replace("tan", "r");
        string = string.replace("log", "t");
        string = string.replace("lg", "r");
        string = string.replace("ln", "r");
        string = string.replace("²√", "r");
        string = string.replace("+", "t");
        string = string.replace("-", "t");
        string = string.replace("×", "t");
        string = string.replace("÷", "t");
        string = string.replace("%", "l");
        string = string.replace("²", "l");
        string = string.replace("√", "t");
        string = string.replace("!", "l");
        string = string.replace("^", "t");
        string = string.replace("π", "c");
        string = string.replace("e", "c");
        string = string.replace("Φ", "c");
        string = string.replace(".", "#");
        string = string.replace("(", "k");
        string = string.replace(")", "h");
        string = string.replaceAll("[0-9]", "n");
        if (string.indexOf("dc") > -1 || string.indexOf("rl") > -1
                || string.indexOf("tl") > -1 || string.indexOf("kl") > -1
                || string.indexOf("dl") > -1 || string.indexOf("dr") > -1
                || string.indexOf("rt") > -1 || string.indexOf("tt") > -1
                || string.indexOf("kt") > -1 || string.indexOf("dt") > -1
                || string.indexOf("dk") > -1 || string.indexOf("rh") > -1
                || string.indexOf("rh") > -1 || string.indexOf("th") > -1
                || string.indexOf("kh") > -1 || string.indexOf("dh") > -1
                || string.indexOf("cd") > -1 || string.indexOf("ld") > -1
                || string.indexOf("hd") > -1 || string.indexOf("dd") > -1) {
            correct = "ERROR: order of connection has wrong";
        } else {
            correct = "TRUE";
        }
    }

}
