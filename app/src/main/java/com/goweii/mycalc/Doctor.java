/**
 * 用来修正输入输出
 * NewInputExpression
 * NewResult
 */

package com.goweii.mycalc;

import java.text.DecimalFormat;

public class Doctor {
    private String NewInputExpression;
    private String NewResult;
    private final String STRING_PAI = "3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555964462294895493038196442881097566593344612847564823378678316527120190914564856692346034861045432664821339360726024914127372458700660631558817488152092096282925409171536436789259036001133053054882046652138414695194151160943305727036575959195309218611738193261179310511854807446237996274956735188575272489122793818301194912983367336244065664308602139494639522473719070217986094370277053921717629317675238467481846766940513200056812714526356082778577134275778960917363717872146844090122495343014654958537105079227968925892354201995611212902196086403441815981362977477130996051870721134999999837297804995105973173281609631859502445945534690830264252230825334468503526193118817101000313783875288658753320838142061717766914730359825349042875546873115956286388235378759375195778185778053217122680661300192787661119590921642019893809525720106548586327886593615338182796823030195203530185296899577362259941389124972177528347913151";
    private final String STRING_E = "2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274";
    private final String STRING_FAI = "1.6180339887498948482045868343656381177203091798057628621354486227052604628189024497072072041893911374847540880753868917521266338622235369317931800607667263544333890865959395829056383226613199282902678806752087668925017116962070322210432162695486262963136144381497587012203408058879544547492461856953648644492410443207713449470495658467885098743394422125448770664780915884607499887124007652170575179788341662562494075890697040002812104276217711177780531531714101170466659914669798731761356006708748071013179523689427521948435305678300228785699782977834784587822891109762500302696156170025046433824377648610283831268330372429267526311653392473167111211588186385133162038400522216579128667529465490681131715993432359734949850904094762132229810172610705961164562990981629055520852479035240602017279974717534277759277862561943208275051312181562855122248093947123414517022373580577278616008688382952304592647878017889921990270776903895321968198615143780314997411069260886742962267575605231727775203536139362107673893764556060605922";

    public Doctor(String inputExpression){
        NewInputExpression = inputExpression;
    }

    public String getNewInputExpression() {
        replace();
        return NewInputExpression;
    }

    //修正double类型的误差。去除2.0的.0。
    public String getResult(String result){
        NewResult = result;
        int i = NewResult.indexOf(".");
        if (i > -1) {
            DecimalFormat afFormat = new DecimalFormat("#0.0000000000000000000000000");
            NewResult = afFormat.format(Double.parseDouble(NewResult));
            for (int j = NewResult.length() - 1; j > i; j--) {
                if (NewResult.charAt(j) == '0') {
                    NewResult = NewResult.substring(0, j);
                } else {
                    break;
                }
            }
            if (NewResult.charAt(NewResult.length() - 1) == '.') {
                NewResult = NewResult.substring(0, NewResult.length() - 1);
            }
        }
        return NewResult;
    }

    //修正input!!!
    public void replace() {
        NewInputExpression = NewInputExpression.replace("×", "*");
        NewInputExpression = NewInputExpression.replace("÷", "/");
        NewInputExpression = NewInputExpression.replace(")", "+0)"); // 避免（123）情况崩溃
        NewInputExpression = NewInputExpression.replace("(-", "(0-"); // 避免（-123）情况崩溃
        NewInputExpression = NewInputExpression.replace("(", "(0+"); //避免（1*1）情况崩溃
        NewInputExpression = NewInputExpression.replace("%", "/100)");
        NewInputExpression = NewInputExpression.replace("²√", "2√");
        NewInputExpression = NewInputExpression.replace("²", "^2");
        NewInputExpression = NewInputExpression.replace("lg", "10g");
        NewInputExpression = NewInputExpression.replace("ln", "eg");
        NewInputExpression = NewInputExpression.replace("log", "g");
        NewInputExpression = NewInputExpression.replace("asin", "1d");
        NewInputExpression = NewInputExpression.replace("acos", "1v");
        NewInputExpression = NewInputExpression.replace("atan", "1y");
        NewInputExpression = NewInputExpression.replace("sin", "1s");
        NewInputExpression = NewInputExpression.replace("cos", "1c");
        NewInputExpression = NewInputExpression.replace("tan", "1t");
        NewInputExpression = NewInputExpression.replace("!", "!1");
        //----------------------πeΦ--------------------------

        NewInputExpression = NewInputExpression.replace("π", "(π)");
        NewInputExpression = NewInputExpression.replace("e", "(e)");
        NewInputExpression = NewInputExpression.replace("Φ", "(Φ)");
        NewInputExpression = NewInputExpression.replace(")(", ")*(");

        NewInputExpression = NewInputExpression.replace("π", STRING_PAI);
        NewInputExpression = NewInputExpression.replace("e", STRING_E);
        NewInputExpression = NewInputExpression.replace("Φ", STRING_FAI);

        NewInputExpression = NewInputExpression.replace("0(", "0*(");
        NewInputExpression = NewInputExpression.replace("1(", "1*(");
        NewInputExpression = NewInputExpression.replace("2(", "2*(");
        NewInputExpression = NewInputExpression.replace("3(", "3*(");
        NewInputExpression = NewInputExpression.replace("4(", "4*(");
        NewInputExpression = NewInputExpression.replace("5(", "5*(");
        NewInputExpression = NewInputExpression.replace("6(", "6*(");
        NewInputExpression = NewInputExpression.replace("7(", "7*(");
        NewInputExpression = NewInputExpression.replace("8(", "8*(");
        NewInputExpression = NewInputExpression.replace("9(", "9*(");
        NewInputExpression = NewInputExpression.replace(")0", ")*0");
        NewInputExpression = NewInputExpression.replace(")1", ")*1");
        NewInputExpression = NewInputExpression.replace(")2", ")*2");
        NewInputExpression = NewInputExpression.replace(")3", ")*3");
        NewInputExpression = NewInputExpression.replace(")4", ")*4");
        NewInputExpression = NewInputExpression.replace(")5", ")*5");
        NewInputExpression = NewInputExpression.replace(")6", ")*6");
        NewInputExpression = NewInputExpression.replace(")7", ")*7");
        NewInputExpression = NewInputExpression.replace(")8", ")*8");
        NewInputExpression = NewInputExpression.replace(")9", ")*9");

        NewInputExpression = NewInputExpression.replace(")", "+0)"); // 避免（123）情况崩溃
        NewInputExpression = NewInputExpression.replace("(-", "(0-"); // 避免（-123）情况崩溃
        NewInputExpression = NewInputExpression.replace("(", "(0+"); //避免（1*1）情况崩溃

        NewInputExpression = "0+" + NewInputExpression;
    }
}
