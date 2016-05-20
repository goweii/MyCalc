package com.goweii.mycalc;

public class calculate
{
	/**
	 * 判断是否是运算符
	 *
	 * @param operator
	 * @return
	 */
	public static boolean isoperator(String operator)
	{
		if (operator.equals("!") ||
				operator.equals("s") || operator.equals("d") ||
				operator.equals("c") || operator.equals("v") ||
				operator.equals("t") || operator.equals("y") ||
				operator.equals("√") || operator.equals("g") ||
				operator.equals("^") || operator.equals("+") ||
				operator.equals("-") || operator.equals("*") ||
				operator.equals("/") || operator.equals("(") ||
				operator.equals(")"))
			return true;
		else
			return false;
	}

	/**
	 * 设置优先级
	 *
	 * @param operator
	 * @return
	 */
	public static int priority(String operator)
	{
		if (operator.equals("+") || operator.equals("-") || operator.equals("("))
			return 1;
		else if (operator.equals("*") || operator.equals("/"))
			return 2;
		else if (operator.equals("g") || operator.equals("√") || operator.equals("^"))
			return 3;
		else if (operator.equals("s") || operator.equals("c") || operator.equals("t") ||
				operator.equals("d") || operator.equals("v") || operator.equals("y"))
			return 4;
		else if (operator.equals("!"))
			return 5;
		else
			return 0;
	}

	/**
	 * 计算结果
	 *
	 * @param operator
	 * @param a
	 * @param b
	 * @return
	 */
	public static String tworesult(String operator, String a, String b)
	{
		try
		{
			String op = operator;
			String rs = new String();
			double x = Double.parseDouble(b);
			double y = Double.parseDouble(a);
			double z = 0;
			if (op.equals("+"))
				z = x + y;
			else if (op.equals("-"))
				z = x - y;
			else if (op.equals("*"))
				z = x * y;
			else if (op.equals("/"))
				z = x / y;
			else if (op.equals("g"))
				z = Math.log10(y) / Math.log10(x);
			else if (op.equals("^"))
				z = Math.pow(x, y);
			else if (op.equals("√"))
				z = Math.pow(y, 1 / x);
			else if (op.equals("s"))
				z = Math.sin(Math.PI / (180 / y));
			else if (op.equals("c"))
				z = Math.cos(Math.PI / (180 / y));
			else if (op.equals("t"))
				z = Math.tan(Math.PI / (180 / y));
			else if (op.equals("d"))
				z = 180 / (Math.PI / Math.asin(y));
			else if (op.equals("v"))
				z = 180 / (Math.PI / Math.acos(y));
			else if (op.equals("y"))
				z = 180 / (Math.PI / Math.atan(y));
			else if (op.equals("!"))		//阶乘，用到了伽马函数，computegamma!!
			{
				z = y;
				if (x % 1 == 0)
				{
					if (x >= 0)		//如：2！=2*1
						for (int i = 0; i < (int) x; i ++)
							z = z * (x - i);
					else			//如：（-2）！=1/3!
					{
						for (int i = 0; i < (int) (0 - x + 1); i ++)
							z = z * ((0 - x + 1) - i);
						z = 1 / z;
					}
				}else {
					if (x >= 0)		//如：2.1!=2.1*1.1*0.1*gamma(0.1)
					{
						for (int i = 0; i <= (int) x; i ++)
							z = z * (x - i);
						double gam = ComputeGamma.getValue(x - (int) x);
						z = z * gam;
					}
					else {
						for (int i = 0; i <= (int) x; i ++)
							z = z * (x - i);
						double gam = ComputeGamma.getValue(x - (int) x);
						z = z * gam;
						z = 1 / z;
					}
				}
			}
			else
				z = 0;
			return rs + z;
		}
		catch (NumberFormatException e)
		{
			return "ERROR: undefined error";
		}
	}
}
