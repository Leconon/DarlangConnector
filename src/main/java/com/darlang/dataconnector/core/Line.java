package com.darlang.dataconnector.core;

import com.darlang.dataconnector.core.converter.SqlCommandConstructor;

/**
 *
 * @author leonardo
 */
public class Line {

    private final int number;
    private final String input;
    private String generetedSql;
    private boolean executed = false;
    private boolean ok = false;

    public Line(int number, String input) {
        this.number = number;
        this.input = input;
    }

    public int getNumber() {
        return number;
    }

    public String getInput() {
        return input;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public String getGeneretedSql() {
        return generetedSql;
    }

    public void setGeneretedSql(String generetedSql) {
        this.generetedSql = generetedSql;
    }

    public boolean isProcessed() {
        return generetedSql != null;
    }

    public void process() throws Exception {
        SqlCommandConstructor constructor = new SqlCommandConstructor(number);
        generetedSql = constructor.lineToSql(input);
        System.out.println(generetedSql);
    }

}
