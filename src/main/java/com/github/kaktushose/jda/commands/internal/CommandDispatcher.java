package com.github.kaktushose.jda.commands.internal;

import com.github.kaktushose.jda.commands.api.ArgumentParser;
import com.github.kaktushose.jda.commands.api.CommandMapper;
import com.github.kaktushose.jda.commands.api.EmbedFactory;
import com.github.kaktushose.jda.commands.api.ErrorHandler;
import com.github.kaktushose.jda.commands.settings.CommandSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandDispatcher extends ListenerAdapter {

    protected final CommandSettings settings;
    protected final List<CommandBean> commands;
    protected final ErrorHandler errorHandler;
    protected final ArgumentParser argumentParser;
    protected final CommandMapper commandMapper;
    protected static final Logger log = LoggerFactory.getLogger(CommandDispatcher.class);
    private final JDA jda;
    private final CommandRegistry commandRegistry;

    protected CommandDispatcher(JDA jda, CommandSettings settings, ErrorHandler errorHandler) {
        this.jda = jda;
        this.settings = settings;
        this.errorHandler = errorHandler;
        commands = new ArrayList<>();
        commandRegistry = new CommandRegistry(settings);
        argumentParser = new ArgumentParser();
        commandMapper = new CommandMapper();

    }

    final void start() {
        jda.addEventListener(this);
        commandRegistry.indexCommands();
        commands.addAll(commandRegistry.getCommands());
    }

    protected boolean validateEvent(GuildMessageReceivedEvent event) {
        if (settings.isIgnoreBots() && event.getAuthor().isBot()) {
            return false;
        }
        if (!event.getMessage().getContentDisplay().startsWith(settings.getPrefix())) {
            return false;
        }
        if (settings.getMutedChannels().contains(event.getMessageIdLong())) {
            return false;
        }
        return true;
    }

    protected String[] parseMessage(GuildMessageReceivedEvent event) {
        String contentRaw = event.getMessage().getContentStripped();
        while (contentRaw.contains("  ")) {
            contentRaw = contentRaw.replaceAll(" {2}", " ");
        }
        return contentRaw.trim().replaceFirst(settings.getPrefix(), "").split(" ");
    }

    @Override
    @SubscribeEvent
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!validateEvent(event)) {
            return;
        }

        event.getChannel().sendMessage(EmbedFactory.getDefaultHelpEmbed(commands).build()).queue();

        String[] input = parseMessage(event);

        // searching a matching command
        Optional<CommandBean> command = commandMapper.findCommand(commands, input);
        if (command.isEmpty()) {
            errorHandler.onCommandMappingFailed(event);
            return;
        }
        CommandBean commandBean = command.get();

        // argument parsing
        int from = commandBean.getLabels().get(0).split(" ").length;
        List<String> parameters = commandBean.getParameters();
        List<String> arguments = Arrays.asList(Arrays.copyOfRange(input, from, input.length));
        List<Object> actualArgs = new ArrayList<>();
        actualArgs.add(new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), commandBean));
        EmbedBuilder syntaxEmbed = EmbedFactory.getSyntaxErrorEmbed(commandBean, arguments);

        for (int i = 0; i < parameters.size(); i++) {

            // check if argument size is matching
            if (arguments.size() == i) {
                if (commandBean.isOptional() && i == parameters.size() - 1) {
                    actualArgs.add(null);
                    break;
                } else {
                    event.getChannel().sendMessage(syntaxEmbed.build()).queue();
                    return;
                }
            }

            // actual argument parsing
            Optional<?> optional = argumentParser.parse(arguments.get(i), parameters.get(i), event.getGuild());
            if (optional.isEmpty()) {
                event.getChannel().sendMessage(syntaxEmbed.build()).queue();
                return;
            }

            // check for concat parameter
            if (i == parameters.size() - 1 && commandBean.isConcat()) {
                StringBuilder sb = new StringBuilder();
                for (String s : Arrays.copyOfRange(input, i + 1, input.length)) {
                    sb.append(s).append(" ");
                }
                actualArgs.add(sb.toString().trim());
                break;
            }
            actualArgs.add(optional.get());
        }
        // command method invocation
        try {
            commandBean.getMethod().invoke(commandBean.getInstance(), actualArgs.toArray());
        } catch (Exception e) {
            errorHandler.onCommandException(event, e);
        }
    }
}
