package com.github.kaktushose.jda.commands.internal;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class CommandEvent extends GuildMessageReceivedEvent {

    private final CommandBean commandBean;

    public CommandEvent(@Nonnull JDA api, long responseNumber, @Nonnull Message message, CommandBean commandBean) {
        super(api, responseNumber, message);
        this.commandBean = commandBean;
    }

    public void reply(String message) {
        getChannel().sendMessage(message).queue();
    }

    public void reply(String format, Object... args) {
        reply(String.format(format, args));
    }

    public void reply(String message, Consumer<Message> success) {
        getChannel().sendMessage(message).queue(success);
    }

    public void reply(MessageBuilder messageBuilder) {
        getChannel().sendMessage(messageBuilder.build()).queue();
    }

    public void reply(MessageBuilder messageBuilder, Consumer<Message> success) {
        getChannel().sendMessage(messageBuilder.build()).queue(success);
    }

    public void reply(EmbedBuilder embedBuilder) {
        getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public void reply(EmbedBuilder embedBuilder, Consumer<Message> success) {
        getChannel().sendMessage(embedBuilder.build()).queue(success);
    }

    public CommandBean getCommandBean() {
        return commandBean;
    }
}
