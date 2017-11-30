package com.leotech.monitor.model.modifier;

//Interface for all our modifier enums
//to extend
public interface Modifier {
    /**
     * Sets the parameter or column name needed for the associated modifier
     * If this method is not called, the modifier will often put
     * '%sParam' in place of where the parameter/column will go (if anywhere)
     * @param parameterOrColumn An ordered list of parameters or columns to use. Often this will only be a single element
     */
    Modifier setParam(String[] parameterOrColumn);
    String getPureModifier();
    String getStandardWhereClause();
    String getConcatenatedClause();
}
