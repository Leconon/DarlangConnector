package com.darlang.dataconnector.core.converter;

/**
 *
 * @author leonardo
 */
public class CommandProps {

    private Command command;
    private String args;

    public CommandProps() {
    }

    public CommandProps(Command command, String args) {
        this.command = command;
        this.args = args;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

}
