package com.github.kaktushose.jda.commands.api;

import com.github.kaktushose.jda.commands.internal.CommandBean;
import com.github.kaktushose.jda.commands.settings.CommandSettings;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public class EmbedFactory {

    public static EmbedBuilder getDefaultHelpEmbed(List<CommandBean> commands) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN)
                .setTitle("General Help")
                .setDescription("The following commands are available. Type `!help command` to get specific help");
        commands.forEach(commandBean -> embedBuilder.addField(
                String.format("`%s`", commandBean.getUsage()),
                commandBean.getDescription(),
                false));
        return embedBuilder;
    }

    public static EmbedBuilder getSpecificHelpEmbed(CommandBean commandBean, CommandSettings settings) {
        return null;
    }

    public static EmbedBuilder getCommandNotFoundEmbed() {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Command Not Found")
                .setDescription(String.format("```type %shelp to get a list of all available commands```", "!"));
    }

    public static EmbedBuilder getSyntaxErrorEmbed(CommandBean commandBean, List<String> arguments) {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Syntax Error")
                .addField("Expected", commandBean.getParameters().toString(), false)
                .addField("Actual", arguments.toString(), false);
    }

    public static EmbedBuilder getExceptionEmbed(Throwable throwable) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Command Execution Failed")
                .setDescription(String.format("```%s```", throwable.toString()))
                .setFooter("This exception occurred due to an internal error. Please contact the bot owner for further information.");
    }

}
