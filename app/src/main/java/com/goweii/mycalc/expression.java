package com.goweii.mycalc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Expression {
    private ArrayList expression = new ArrayList();// 存储中序表达式
    private ArrayList right = new ArrayList();// 存储右序表达式
    private String result;// 结果

    // 依据输入信息创建对象，将数值与操作符放入arraylist中
    public Expression(String input) {
        StringTokenizer st = new StringTokenizer(input, "+-*/()^g√sctdvy!", true);
        while (st.hasMoreElements()) {
            expression.add(st.nextToken());
        }
    }

    // 将中序表达式转换为右序表达式
    public void toright() {
        Stacks astack = new Stacks();
        String operator;
        int position = 0;
        while (true) {
            if (Calculate.isoperator((String) expression.get(position))) {
                if (astack.top == -1 || ((String) expression.get(position)).equals("(")) {
                    astack.push(expression.get(position));
                } else {
                    if (((String) expression.get(position)).equals(")")) {
                        if (!((String) astack.top()).equals("(")) {
                            operator = (String) astack.pop();
                            right.add(operator);
                        }
                    } else {
                        if (Calculate.priority((String) expression.get(position)) <= Calculate.priority((String) astack.top()) && astack.top != -1) {
                            operator = (String) astack.pop();
                            if (!operator.equals("("))
                                right.add(operator);
                        }
                        astack.push(expression.get(position));
                    }
                }
            } else
                right.add(expression.get(position));
            position++;
            if (position >= expression.size())
                break;
        }
        while (astack.top != -1) {
            operator = (String) astack.pop();
            right.add(operator);
        }
    }

    // 对右序表达式进行求值
    public String getresult() {
        this.toright();
        Stacks astack = new Stacks();
        String op1, op2, is = null;
        Iterator it = right.iterator();
        while (it.hasNext()) {
            is = (String) it.next();
            if (Calculate.isoperator(is)) {
                op1 = (String) astack.pop();
                op2 = (String) astack.pop();
                astack.push(Calculate.tworesult(is, op1, op2));
            } else
                astack.push(is);
        }
        result = (String) astack.pop();
        it = expression.iterator();
        while (it.hasNext()) {
            System.out.print((String) it.next());
        }
        return result;
    }
//	public static void main(String avg[])
//	{
//	}
}