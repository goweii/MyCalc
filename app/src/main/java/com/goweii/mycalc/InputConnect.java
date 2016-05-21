package com.goweii.mycalc;

/**
 * Created by cuizhen on 16/05/18.
 */
public class InputConnect {
    private String inputExpression = " ";
    private String correct = "TRUE";

    /**
     *
     * @param inputExpression   已输入的表达式
     */
    public InputConnect(String inputExpression){
        this.inputExpression = inputExpression;
    }

    /**
     * 获取点击按键后合成的表达式
     * @return
     */
    public String getInputExpression(){
        return inputExpression;
    }

    public String checkConnect()
    {
        if(inputExpression.length() == 0) {
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

    public void checkEndWithNumber()			//判断输入是否以数字结尾！！
    {
        if (!(inputExpression.endsWith("π") || inputExpression.endsWith("e")
                || inputExpression.endsWith("Φ") || inputExpression.endsWith("%")
                || inputExpression.endsWith("²") || inputExpression.endsWith("!")
                || inputExpression.endsWith("1") || inputExpression.endsWith("2")
                || inputExpression.endsWith("3") || inputExpression.endsWith("4")
                || inputExpression.endsWith("5") || inputExpression.endsWith("6")
                || inputExpression.endsWith("7") || inputExpression.endsWith("8")
                || inputExpression.endsWith("9") || inputExpression.endsWith("0")
                || inputExpression.endsWith(")")))
        {
            correct = "ERROR: cannot end with '" + inputExpression.charAt(inputExpression.length() - 1) + "'";
        }
        else {
            correct = "TRUE";
        }
        System.out.println(inputExpression);
    }

    public void checkFirstCharacter()			//判断输入首部是否真确
    {
        if (inputExpression.indexOf("㏒") == 0)
        {
            correct = "ERROR: cannot start with '㏒'";
        }
        else {
            correct = "TRUE";
        }
        System.out.println(inputExpression);
    }

    public void CheckBracketSymmetry()			//判断（）的数量是否相等！！
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
        if (num1 != num2)
        {
            correct = "ERROR: the number of '(' and ')'";
        }
        else {
            correct = "TRUE";
        }
        System.out.println(inputExpression);
    }

    public void CheckBracketOrder()			//判断（）的顺序是否正确！
    {
        int num1 = 0;
        int num2 = 0;
        char[] chars = inputExpression.toCharArray();
        for (int j = 0; j < chars.length; j++)
        {
            for (int i = 0; i < chars.length - j; i++)
                if (chars[i] == '(')
                    num1++;
            for (int i = 0; i < chars.length - j; i++)
                if (chars[i] == ')')
                    num2++;
            if (num1 < num2)
            {
                correct = "ERROR: the order of '(' and ')'";
                break;
            }
            else {
                correct = "TRUE";
            }
        }
        System.out.println(inputExpression);
    }

    public void CheckDecimalPoint()			//判断是否有两个小数点的数据！！！
    {
        String doublePoint = inputExpression.replaceAll("[0-9]+\\.[0-9]+\\.[0-9]", "double.");
        if (doublePoint.indexOf("double.") > -1){
            correct = "ERROR: a number has more '.'";
        } else{
            correct = "TRUE";
        }
        System.out.println(inputExpression);
    }

    public void CheckConnectOrder()			//判断是否有连接错误！！！
    {
        String string = inputExpression;
        string = string.replace("(-", "(0-");
        string = string.replace("+", "#");
        string = string.replace("-", "#");
        string = string.replace("×", "#");
        string = string.replace("÷", "#");
        string = string.replace("%", "@");
        string = string.replace("²√", "#");
        string = string.replace("²", "@");
        string = string.replace("√", "#");
        string = string.replace("㏑", "#");
        string = string.replace("㏒", "#");
        string = string.replace("!", "@");
        string = string.replace(".", "#");
        string = string.replace("^", "#");
        string = string.replace("asin", "&");
        string = string.replace("acos", "&");
        string = string.replace("atan", "&");
        string = string.replace("sin", "&");
        string = string.replace("cos", "&");
        string = string.replace("tan", "&");
        string = string.replaceAll("[0-9]\\(", "~");
        string = string.replaceAll("\\)[0-9]", "~");
        if (string.indexOf("##") > -1 || string.indexOf("&#") > -1 ||
                string.indexOf("#@") > -1 ||
                string.indexOf("(#") > -1 || string.indexOf("(@") > -1 ||
                string.indexOf("#)") > -1 || string.indexOf("&)") > -1
                || string.indexOf("~") > -1)        {
            correct = "ERROR: order of connection has wrong";
        } else{
            correct = "TRUE";
        }
    }

}
