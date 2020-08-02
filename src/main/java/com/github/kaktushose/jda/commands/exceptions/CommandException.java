package com.github.kaktushose.jda.commands.exceptions;

public class CommandException extends RuntimeException {

    public CommandException(String error) {
        super(error);
    }

    public CommandException(String error, Throwable t) {
        super(error, t);
    }

    public CommandException(Throwable t) {
        super(t);
    }

}
