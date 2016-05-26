package com.goweii.mycalc;

import java.math.BigDecimal;

/**
 * Created by cuizhen on 16/05/26.
 */
public class OutputFormat {
    private String output_number_format;
    private String result;
    private String newResult;

    public OutputFormat(String output_number_format, String result){
        this.output_number_format = output_number_format;
        this.result = result;
    }

    public String getResult(){
        toDecimal();
        if(output_number_format == "DECIMAL"){
            return newResult;
        }
        return result;
    }

    private void toDecimal(){
        BigDecimal bigDecimal = new BigDecimal(result);
        newResult = bigDecimal.toPlainString();
    }
}
