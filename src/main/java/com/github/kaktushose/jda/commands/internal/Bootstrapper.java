package com.github.kaktushose.jda.commands.internal;

import com.github.kaktushose.jda.commands.api.ErrorHandler;
import com.github.kaktushose.jda.commands.settings.CommandSettings;
import net.dv8tion.jda.api.JDA;

public final class Bootstrapper {

    private CommandSettings settings;

    public static void startDefault(JDA jda) {
        run(jda, new CommandSettings("!"), new ErrorHandler());
    }

    public static void start(JDA jda, CommandSettings settings) {
        run(jda, settings, new ErrorHandler());
    }

    public static void start(JDA jda, CommandSettings settings, ErrorHandler errorHandler) {
        run(jda, settings, errorHandler);
    }

    private static void run(JDA jda, CommandSettings settings, ErrorHandler errorHandler) {
        new CommandDispatcher(jda, settings, errorHandler).start();
    }

}
