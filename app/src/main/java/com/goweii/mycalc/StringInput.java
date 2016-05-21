package com.goweii.mycalc;
/**
 * Created by cuizhen on 16/05/20.
 */

public class StringInput {
    private String newInputExpression;
    private int newIndex;
    private int deleteNum;

    public StringInput(String inputExpression, int index){
        newIndex = index;
        newInputExpression = inputExpression;
    }

    /**
     * 判断传入的光标位置是否在sin等运算符中间
     * 如果在中间则返回运算符后一字符位置
     * 若果不在直接返回
     * @return
     */
    public int getNewIndex(){
        //a.tan
        if((0 < newIndex && newIndex < newInputExpression.length() - 2
                && newInputExpression.charAt(newIndex - 1) == 'a'
                && newInputExpression.charAt(newIndex) == 't'
                && newInputExpression.charAt(newIndex + 1) == 'a'
                && newInputExpression.charAt(newIndex + 2) == 'n')
                || (0 < newIndex && newIndex < newInputExpression.length() - 2
                && newInputExpression.charAt(newIndex - 1) == 'a'
                && newInputExpression.charAt(newIndex) == 'c'
                && newInputExpression.charAt(newIndex + 1) == 'o'
                && newInputExpression.charAt(newIndex + 2) == 's')
                || (0 < newIndex && newIndex < newInputExpression.length() - 2
                && newInputExpression.charAt(newIndex - 1) == 'a'
                && newInputExpression.charAt(newIndex) == 's'
                && newInputExpression.charAt(newIndex + 1) == 'i'
                && newInputExpression.charAt(newIndex + 2) == 'n')){
            deleteNum =4;
            return newIndex + 3;
        }
        //at.an
        else if((1 < newIndex && newIndex < newInputExpression.length() - 1
                && newInputExpression.charAt(newIndex - 2) == 'a'
                && newInputExpression.charAt(newIndex - 1) == 't'
                && newInputExpression.charAt(newIndex) == 'a'
                && newInputExpression.charAt(newIndex + 1) == 'n')
                || (1 < newIndex && newIndex < newInputExpression.length() - 1
                && newInputExpression.charAt(newIndex - 2) == 'a'
                && newInputExpression.charAt(newIndex - 1) == 'c'
                && newInputExpression.charAt(newIndex) == 'o'
                && newInputExpression.charAt(newIndex + 1) == 's')
                || (1 < newIndex && newIndex < newInputExpression.length() - 1
                && newInputExpression.charAt(newIndex - 2) == 'a'
                && newInputExpression.charAt(newIndex - 1) == 's'
                && newInputExpression.charAt(newIndex) == 'i'
                && newInputExpression.charAt(newIndex + 1) == 'n')){
            deleteNum =4;
            return newIndex + 2;
        }
        //ata.n
        else if((2 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 3) == 'a'
                && newInputExpression.charAt(newIndex - 2) == 't'
                && newInputExpression.charAt(newIndex - 1) == 'a'
                && newInputExpression.charAt(newIndex) == 'n')
                || (2 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 3) == 'a'
                && newInputExpression.charAt(newIndex - 2) == 'c'
                && newInputExpression.charAt(newIndex - 1) == 'o'
                && newInputExpression.charAt(newIndex) == 's')
                || (2 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 3) == 'a'
                && newInputExpression.charAt(newIndex - 2) == 's'
                && newInputExpression.charAt(newIndex - 1) == 'i'
                && newInputExpression.charAt(newIndex) == 'n')){
            deleteNum =4;
            return newIndex + 1;
        }
        //t.an
        else if((0 < newIndex && newIndex < newInputExpression.length() - 1
                && newInputExpression.charAt(newIndex - 1) == 't'
                && newInputExpression.charAt(newIndex) == 'a'
                && newInputExpression.charAt(newIndex + 1) == 'n')
                || (0 < newIndex && newIndex < newInputExpression.length() - 1
                && newInputExpression.charAt(newIndex - 1) == 'c'
                && newInputExpression.charAt(newIndex) == 'o'
                && newInputExpression.charAt(newIndex + 1) == 's')
                || (0 < newIndex && newIndex < newInputExpression.length() - 1
                && newInputExpression.charAt(newIndex - 1) == 's'
                && newInputExpression.charAt(newIndex) == 'i'
                && newInputExpression.charAt(newIndex + 1) == 'n')
                || (0 < newIndex && newIndex < newInputExpression.length() - 1
                && newInputExpression.charAt(newIndex - 1) == 'l'
                && newInputExpression.charAt(newIndex) == 'o'
                && newInputExpression.charAt(newIndex + 1) == 'g')){
            deleteNum =3;
            return newIndex + 2;
        }
        //ta.n
        else if((1 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 2) == 't'
                && newInputExpression.charAt(newIndex - 1) == 'a'
                && newInputExpression.charAt(newIndex) == 'n')
                || (1 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 2) == 'c'
                && newInputExpression.charAt(newIndex - 1) == 'o'
                && newInputExpression.charAt(newIndex) == 's')
                || (1 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 2) == 's'
                && newInputExpression.charAt(newIndex - 1) == 'i'
                && newInputExpression.charAt(newIndex) == 'n')
                || (1 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 2) == 'l'
                && newInputExpression.charAt(newIndex - 1) == 'o'
                && newInputExpression.charAt(newIndex) == 'g')){
            deleteNum =3;
            return newIndex + 1;
        }
        //l.g
        else if((0 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 1) == 'l'
                && newInputExpression.charAt(newIndex) == 'g')
                || (0 < newIndex && newIndex < newInputExpression.length()
                && newInputExpression.charAt(newIndex - 1) == 'l'
                && newInputExpression.charAt(newIndex) == 'n')){
            deleteNum =2;
            return newIndex + 1;
        }
        //没有在整体符号之间之间返回
        return newIndex;
    }

    public int getDeleteNum(){
        return deleteNum;
    }
}
