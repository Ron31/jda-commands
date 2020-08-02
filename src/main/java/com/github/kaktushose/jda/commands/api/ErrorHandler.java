package com.github.kaktushose.jda.commands.api;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ErrorHandler {

    public void onCommandMappingFailed(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedFactory.getCommandNotFoundEmbed().build()).queue();
    }

    public void onCommandException(GuildMessageReceivedEvent event, Throwable throwable) {
        event.getChannel().sendMessage(EmbedFactory.getExceptionEmbed(throwable).build()).queue();
    }

}
