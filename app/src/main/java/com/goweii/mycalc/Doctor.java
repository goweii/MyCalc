package com.goweii.mycalc;

import java.text.DecimalFormat;


public class Doctor
{
	private String input;
	private String result;
	private int error = 0;

	public void di1()			//判断输入是否以数字结尾！！
	{
		if (!(input.endsWith("π") || input.endsWith("e")
				|| input.endsWith("Φ") || input.endsWith("%")
				|| input.endsWith("²") || input.endsWith("!")
				|| input.endsWith("1") || input.endsWith("2")
				|| input.endsWith("3") || input.endsWith("4")
				|| input.endsWith("5") || input.endsWith("6")
				|| input.endsWith("7") || input.endsWith("8")
				|| input.endsWith("9") || input.endsWith("0")
				|| input.endsWith(")")))
		{
			input = "ERROR: cannot end with '" + input.charAt(input.length() - 1) + "'";
			error = 1;
		}
		else {
			error = 0;
		}
		System.out.println(input);
	}

	public void di2()			//判断输入首部是否真确
	{
		if (input.indexOf("㏒") == 0)
		{
			input = "ERROR: cannot start with '㏒'";
			error = 1;
		}
		else {
			error = 0;
		}
		System.out.println(input);
	}

	public void di3()			//判断（）的数量是否相等！！
	{
		int num1 = 0;
		int num2 = 0;
		char[] chars = input.toCharArray();
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == '(')
				num1++;
		for (int i = 0; i < chars.length; i++)
			if (chars[i] == ')')
				num2++;
		if (num1 != num2)
		{
			input = "ERROR: the number of '(' and ')'";
			error = 1;
		}
		else {
			error = 0;
		}
		System.out.println(input);
	}

	public void di4()			//判断（）的顺序是否正确！
	{
		int num1 = 0;
		int num2 = 0;
		char[] chars = input.toCharArray();
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
				input = "ERROR: the order of '(' and ')'";
				error = 1;
				break;
			}
			else {
				error = 0;
			}
		}
		System.out.println(input);
	}

	public void di5()			//判断是否有两个小数点的数据！！！
	{
		String doublexiaoshudian = input.replaceAll("[0-9]+\\.[0-9]+\\.[0-9]", "double.");
		if (doublexiaoshudian.indexOf("double.") > -1)
		{
			input = "ERROR: a number has more '.'";
			error = 1;
		}
		else {
			error = 0;
		}
		System.out.println(input);
	}

	public void di6()			//判断是否有连接错误！！！
	{
		String string = input;
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
				|| string.indexOf("~") > -1)
		{
			error = 1;
			input = "ERROR: order of connection has wrong";
		} else
		{
			error = 0;
		}
	}

	public void di7()			//修正input!!!
	{
		input = input.replace("×", "*");
		input = input.replace("÷", "/");
		input = input.replace(")", "+0)"); // 避免（123）情况崩溃
		input = input.replace("(-", "(0-"); // 避免（-123）情况崩溃
		input = input.replace("(", "(0+"); //避免（1*1）情况崩溃
		input = input.replace("%", "/100)");
		input = input.replace("²√", "2√");
		input = input.replace("²", "^2");
		input = input.replace("lg", "10g");
		input = input.replace("㏑", "eg");
		input = input.replace("㏒", "g");
		input = input.replace("asin", "1d");
		input = input.replace("acos", "1v");
		input = input.replace("atan", "1y");
		input = input.replace("sin", "1s");
		input = input.replace("cos", "1c");
		input = input.replace("tan", "1t");
		input = input.replace("!", "!1");
		//----------------------πeΦ--------------------------
		input = input.replace("ππ", "(π*π)");
		input = input.replace("ee", "(e*e)");
		input = input.replace("ΦΦ", "(Φ*Φ)");
		input = input.replace("πe", "(π*e)");
		input = input.replace("πΦ", "(π*Φ)");
		input = input.replace("eΦ", "(e*Φ)");
		input = input.replace("eπ", "(e*π)");
		input = input.replace("Φπ", "(Φ*π)");
		input = input.replace("Φe", "(Φ*e)");
		//-------------------pai--------------------------------
		input = input.replace(")π", ")*3.1415926535897932");
		input = input.replace("0π", "0*3.1415926535897932");
		input = input.replace("1π", "1*3.1415926535897932");
		input = input.replace("2π", "2*3.1415926535897932");
		input = input.replace("3π", "3*3.1415926535897932");
		input = input.replace("4π", "4*3.1415926535897932");
		input = input.replace("5π", "5*3.1415926535897932");
		input = input.replace("6π", "6*3.1415926535897932");
		input = input.replace("7π", "7*3.1415926535897932");
		input = input.replace("8π", "8*3.1415926535897932");
		input = input.replace("9π", "9*3.1415926535897932");
		input = input.replace("π", "3.1415926535897932");
		//--------------------e-------------------------------
		input = input.replace(")e", ")*2.7182818284590452");
		input = input.replace("0e", "0*2.7182818284590452");
		input = input.replace("1e", "1*2.7182818284590452");
		input = input.replace("2e", "2*2.7182818284590452");
		input = input.replace("3e", "3*2.7182818284590452");
		input = input.replace("4e", "4*2.7182818284590452");
		input = input.replace("5e", "5*2.7182818284590452");
		input = input.replace("6e", "6*2.7182818284590452");
		input = input.replace("7e", "7*2.7182818284590452");
		input = input.replace("8e", "8*2.7182818284590452");
		input = input.replace("9e", "9*2.7182818284590452");
		input = input.replace("e", "2.7182818284590452");
		//--------------------fai-----------------------------
		input = input.replace(")Φ", ")*6.1803398874989485");
		input = input.replace("0Φ", "0*6.1803398874989485");
		input = input.replace("1Φ", "1*6.1803398874989485");
		input = input.replace("2Φ", "2*6.1803398874989485");
		input = input.replace("3Φ", "3*6.1803398874989485");
		input = input.replace("4Φ", "4*6.1803398874989485");
		input = input.replace("5Φ", "5*6.1803398874989485");
		input = input.replace("6Φ", "6*6.1803398874989485");
		input = input.replace("7Φ", "7*6.1803398874989485");
		input = input.replace("8Φ", "8*6.1803398874989485");
		input = input.replace("9Φ", "9*6.1803398874989485");
		input = input.replace("Φ", "6.1803398874989485");

		input = input.replace(")", "+0)"); // 避免（123）情况崩溃
		input = input.replace("(-", "(0-"); // 避免（-123）情况崩溃
		input = input.replace("(", "(0+"); //避免（1*1）情况崩溃

		input = "0+" + input;
		System.out.println(input);
	}

	public String di(String str)
	{
		input = str;
		di1();
		if (error == 0)
		{
			di2();
			if (error == 0)
			{
				di3();
				if (error == 0)
				{
					di4();
					if (error == 0)
						di5();
					{
						if (error == 0)
							di6();
						{
							if (error == 0)
								di7();
						}
					}
				}
			}
		}
		return input;
	}

//-----------------------------------------------------------------------------	

	public void dr1()		//修正double类型的误差。去除2.0的.0。
	{
		int i = result.indexOf(".");
		if (i > -1)
		{
			DecimalFormat afFormat = new DecimalFormat("#0.000000000000000");
			result = afFormat.format(Double.parseDouble(result));
			for (int j = result.length() - 1; j > i; j--)
			{
				if (result.charAt(j) == '0')
				{
					result = result.substring(0, j);
				}
				else {
					break;
				}
			}
			if (result.charAt(result.length() - 1) == '.')
			{
				result = result.substring(0, result.length() - 1);
			}
		}
		System.out.println(result);
	}

	public String dr(String str)
	{
		result = str;
		if (result.indexOf("ERROR") > -1)
		{
		} else {
			if (result.indexOf('E') > -1)
			{
				result = result.replaceAll("E", "×10^");
			} else {
				dr1();
			}
		}
		return result;
	}
}
