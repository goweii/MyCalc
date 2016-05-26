/**
 * 判断当前键入字符可否与前面字符连接
 */

package com.goweii.mycalc;

public class NewInputCurrent {
    private String inputExpression;
    private String inputCurrent;
    private char eyeFront = ' ';
    private char eyeCurrent = ' ';
    private int index;

    public NewInputCurrent(String inputExpression, String inputCurrent, int index) {
        this.inputExpression = inputExpression;
        this.inputCurrent = inputCurrent;
        this.index = index;
    }

    public String getNewInputCurrent() {
        judgeEye();
        judgeFront();
        if (eyeCurrent == 'n') {
            if(eyeFront == 'n')
                return inputCurrent;
            if(eyeFront == 'c')
                return "×" + inputCurrent;
            if(eyeFront == 'l')
                return "×" + inputCurrent;
            if(eyeFront == 'r')
                return inputCurrent;
            if(eyeFront == 't')
                return inputCurrent;
            if(eyeFront == 'k')
                return inputCurrent;
            if(eyeFront == 'h')
                return "×" + inputCurrent;
            if(eyeFront == 'd')
                return inputCurrent;
            if(eyeFront == ' ')
                return inputCurrent;
        }
        if (eyeCurrent == 'c') {
            if(eyeFront == 'n')
                return inputCurrent;//计算式替换为6*e
            if(eyeFront == 'c')
                return inputCurrent;//计算式替换为e*e
            if(eyeFront == 'l')
                return "×" + inputCurrent;
            if(eyeFront == 'r')
                return inputCurrent;
            if(eyeFront == 't')
                return inputCurrent;
            if(eyeFront == 'k')
                return inputCurrent;
            if(eyeFront == 'h')
                return "×" + inputCurrent;
            if(eyeFront == 'd')
                return null;
            if(eyeFront == ' ')
                return inputCurrent;
        }
        if (eyeCurrent == 'l') {
            if(eyeFront == 'n')
                return inputCurrent;
            if(eyeFront == 'c')
                return inputCurrent;
            if(eyeFront == 'l')
                return inputCurrent;
            if(eyeFront == 'r')
                return null;
            if(eyeFront == 't')
                return null;
            if(eyeFront == 'k')
                return null;
            if(eyeFront == 'h')
                return inputCurrent;
            if(eyeFront == 'd')
                return null;
            if(eyeFront == ' ')
                return null;
        }
        if (eyeCurrent == 'r') {
            if(eyeFront == 'n')
                return "×" + inputCurrent;
            if(eyeFront == 'c')
                return "×" + inputCurrent;
            if(eyeFront == 'l')
                return "×" + inputCurrent;
            if(eyeFront == 'r')
                return "(" + inputCurrent;
            if(eyeFront == 't')
                return "(" + inputCurrent;
            if(eyeFront == 'k')
                return inputCurrent;
            if(eyeFront == 'h')
                return "×" + inputCurrent;
            if(eyeFront == 'd')
                return null;
            if(eyeFront == ' ')
                return inputCurrent;
        }
        if (eyeCurrent == 't') {
            if(eyeFront == 'n')
                return inputCurrent;
            if(eyeFront == 'c')
                return inputCurrent;
            if(eyeFront == 'l')
                return inputCurrent;
            if(eyeFront == 'r')
                return null;
            if(eyeFront == 't')
                return null;
            if(eyeFront == 'k')
                return null;
            if(eyeFront == 'h')
                return inputCurrent;
            if(eyeFront == 'd')
                return null;
            if(eyeFront == ' ')
                return null;
        }
        if (eyeCurrent == 'k') {
            if(eyeFront == 'n')
                return inputCurrent;//计算式替换6*（
            if(eyeFront == 'c')
                return inputCurrent;//计算式替换e*（
            if(eyeFront == 'l')
                return inputCurrent;//计算式替换！*（
            if(eyeFront == 'r')
                return inputCurrent;
            if(eyeFront == 't')
                return inputCurrent;
            if(eyeFront == 'k')
                return inputCurrent;
            if(eyeFront == 'h')
                return inputCurrent;//计算式替换）*（
            if(eyeFront == 'd')
                return null;
            if(eyeFront == ' ')
                return inputCurrent;
        }
        if (eyeCurrent == 'h') {
            if(eyeFront == 'n')
                return inputCurrent;
            if(eyeFront == 'c')
                return inputCurrent;
            if(eyeFront == 'l')
                return inputCurrent;
            if(eyeFront == 'r')
                return null;
            if(eyeFront == 't')
                return null;
            if(eyeFront == 'k')
                return null;
            if(eyeFront == 'h')
                return inputCurrent;
            if(eyeFront == 'd')
                return null;
            if(eyeFront == ' ')
                return null;
        }
        if (eyeCurrent == 'd') {
            if(eyeFront == 'n')
                return inputCurrent;
            if(eyeFront == 'c')
                return null;
            if(eyeFront == 'l')
                return null;
            if(eyeFront == 'r')
                return "0" + inputCurrent;
            if(eyeFront == 't')
                return "0" + inputCurrent;
            if(eyeFront == 'k')
                return "0" + inputCurrent;
            if(eyeFront == 'h')
                return null;
            if(eyeFront == 'd')
                return null;
            if(eyeFront == ' ')
                return "0" + inputCurrent;
        }
        return null;
    }

    private void judgeFront() {
        for (boolean i = true; i; i = false) {
            if ((3 < index) &&
                    ((inputExpression.charAt(index - 4) == 'a' && inputExpression.charAt(index - 3) == 't'
                            && inputExpression.charAt(index - 2) == 'a' && inputExpression.charAt(index - 1) == 'n')
                            || (inputExpression.charAt(index - 4) == 'a' && inputExpression.charAt(index - 3) == 'c'
                            && inputExpression.charAt(index - 2) == 'o' && inputExpression.charAt(index - 1) == 's')
                            || (inputExpression.charAt(index - 4) == 'a' && inputExpression.charAt(index - 3) == 's'
                            && inputExpression.charAt(index - 2) == 'i' && inputExpression.charAt(index - 1) == 'n'))) {
                eyeFront = 'r';
                break;
            }
            if ((2 < index) &&
                    (inputExpression.charAt(index - 3) == 'l' && inputExpression.charAt(index - 2) == 'o'
                            && inputExpression.charAt(index - 1) == 'g')) {
                eyeFront = 't';
                break;
            }
            if ((2 < index) &&
                    ((inputExpression.charAt(index - 3) == 't' && inputExpression.charAt(index - 2) == 'a'
                            && inputExpression.charAt(index - 1) == 'n')
                            || (inputExpression.charAt(index - 3) == 'c' && inputExpression.charAt(index - 2) == 'o'
                            && inputExpression.charAt(index - 1) == 's')
                            || (inputExpression.charAt(index - 3) == 's' && inputExpression.charAt(index - 2) == 'i'
                            && inputExpression.charAt(index - 1) == 'n')
                            || (inputExpression.charAt(index - 3) == '1' && inputExpression.charAt(index - 2) == '0'
                            && inputExpression.charAt(index - 1) == '^'))) {
                eyeFront = 'r';
                break;
            }
            if ((1 < index) &&
                    ((inputExpression.charAt(index - 2) == 'l' && inputExpression.charAt(index - 1) == 'g')
                            || (inputExpression.charAt(index - 2) == 'l' && inputExpression.charAt(index - 1) == 'n')
                            || (inputExpression.charAt(index - 2) == '²' && inputExpression.charAt(index - 1) == '√'))) {
                eyeFront = 'r';
                break;
            }
            if ((0 < index) &&
                    (inputExpression.charAt(index - 1) == '0'
                            || inputExpression.charAt(index - 1) == '1'
                            || inputExpression.charAt(index - 1) == '2'
                            || inputExpression.charAt(index - 1) == '3'
                            || inputExpression.charAt(index - 1) == '4'
                            || inputExpression.charAt(index - 1) == '5'
                            || inputExpression.charAt(index - 1) == '6'
                            || inputExpression.charAt(index - 1) == '7'
                            || inputExpression.charAt(index - 1) == '8'
                            || inputExpression.charAt(index - 1) == '9')) {
                eyeFront = 'n';
                break;
            }
            if ((0 < index) &&
                    (inputExpression.charAt(index - 1) == 'π'
                            || inputExpression.charAt(index - 1) == 'e'
                            || inputExpression.charAt(index - 1) == 'Φ')) {
                eyeFront = 'c';
                break;
            }
            if ((0 < index) &&
                    (inputExpression.charAt(index - 1) == '%'
                            || inputExpression.charAt(index - 1) == '²'
                            || inputExpression.charAt(index - 1) == '!')) {
                eyeFront = 'l';
                break;
            }
            if ((0 < index) &&
                    (inputExpression.charAt(index - 1) == '^'
                            || inputExpression.charAt(index - 1) == '√'
                            || inputExpression.charAt(index - 1) == '+'
                            || inputExpression.charAt(index - 1) == '-'
                            || inputExpression.charAt(index - 1) == '×'
                            || inputExpression.charAt(index - 1) == '÷')) {
                eyeFront = 't';
                break;
            }
            if ((0 < index) &&
                    (inputExpression.charAt(index - 1) == '(')) {
                eyeFront = 'k';
                break;
            }
            if ((0 < index) &&
                    (inputExpression.charAt(index - 1) == ')')) {
                eyeFront = 'h';
                break;
            }
            if ((0 < index) &&
                    (inputExpression.charAt(index - 1) == '.')) {
                eyeFront = 'd';
                break;
            }
            if ((0 < index) &&
                    (inputExpression == null)) {
                eyeFront = ' ';
                break;
            }
        }
    }

    private void judgeEye() {
        for (boolean i = true; i; i = false) {
            if (inputCurrent == "0" || inputCurrent == "1" || inputCurrent == "2"
                    || inputCurrent == "3" || inputCurrent == "4" || inputCurrent == "5"
                    || inputCurrent == "6" || inputCurrent == "7" || inputCurrent == "8"
                    || inputCurrent == "9") {
                eyeCurrent = 'n';
                break;
            }
            if (inputCurrent == "π" || inputCurrent == "e" || inputCurrent == "Φ") {
                eyeCurrent = 'c';
                break;
            }
            if (inputCurrent == "%" || inputCurrent == "²" || inputCurrent == "!") {
                eyeCurrent = 'l';
                break;
            }
            if (inputCurrent == "lg" || inputCurrent == "ln" || inputCurrent == "²√"
                    || inputCurrent == "tan" || inputCurrent == "cos" || inputCurrent == "sin"
                    || inputCurrent == "atan" || inputCurrent == "acos" || inputCurrent == "asin"
                    || inputCurrent == "10^") {
                eyeCurrent = 'r';
                break;
            }
            if (inputCurrent == "log" || inputCurrent == "^" || inputCurrent == "√"
                    || inputCurrent == "+" || inputCurrent == "-" || inputCurrent == "×"
                    || inputCurrent == "÷") {
                eyeCurrent = 't';
                break;
            }
            if (inputCurrent == "(") {
                eyeCurrent = 'k';
                break;
            }
            if (inputCurrent == ")") {
                eyeCurrent = 'h';
                break;
            }
            if (inputCurrent == ".") {
                eyeCurrent = 'd';
                break;
            }
        }
    }


}
