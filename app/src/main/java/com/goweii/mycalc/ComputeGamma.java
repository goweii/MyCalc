package com.goweii.mycalc;

public class ComputeGamma
{
	static double x = 0;
	static double[] p =
			{ -1.71618513886549492533811e+0, 2.47656508055759199108314e+1, -3.79804256470945635097577e+2, 6.29331155312818442661052e+2, 8.66966202790413211295064e+2, -3.14512729688483675254357e+4, -3.61444134186911729807069e+4, 6.64561438202405440627855e+4 };
	static double[] q =
			{ -3.08402300119738975254353e+1, 3.15350626979604161529144e+2, -1.01515636749021914166146e+3, -3.10777167157231109440444e+3, 2.25381184209801510330112e+4, 4.75584627752788110767815e+3, -1.34659959864969306392456e+5, -1.15132259675553483497211e+5 };
	static double[] c =
			{ -1.910444077728e-03, 8.4171387781295e-04, -5.952379913043012e-04, 7.93650793500350248e-04, -2.777777777777681622553e-03, 8.333333333333333331554247e-02, 5.7083835261e-03 };

	public static double getValue(double a)
	{
		x = a;
		double xp = x;
		double res = 0;
		double xn = 0;
		double z = 0;
		double xnum = 0;
		double xden = 0;
		double ysq = 0;
		double sum = 0;
		if (x == 0)
		{
			return 1;
		}
		if (x <= 1)
		{
			xp = xp + 1;
		}
		if (x <= 12)
		{
			xn = Math.floor(xp) - 1;
			xp = xp - xn;
			z = xp - 1;
			xnum = 0;
			xden = xnum + 1;
			for (int i = 0; i < p.length; i++)
			{
				xnum = (xnum + p[i]) * z;
				xden = xden * z + q[i];
			}
			res = xnum / xden + 1;
		}
		if (x <= 1)
		{
			res = res / x;
		}
		if (x > 2 && x <= 12)
		{
			for (int i = 0; i < xn; i++)
			{
				res = res * xp;
				xp = xp + 1;
			}
		}
		if (x > 12)
		{
			ysq = xp * xp;
			sum = c[6];
			for (int i = 0; i < c.length - 1; i++)
			{
				sum = sum / ysq + c[i];
			}
			double spi = 0.9189385332046727417803297;
			sum = sum / xp - xp + spi;
			sum = sum + (xp - 0.5) * Math.log(xp);
			res = Math.exp(sum);
		}
		return res;
	}
}