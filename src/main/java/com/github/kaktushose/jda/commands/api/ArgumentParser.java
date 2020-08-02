package com.github.kaktushose.jda.commands.api;

import com.github.kaktushose.jda.commands.exceptions.CommandException;
import com.github.kaktushose.jda.commands.internal.ParameterType;
import net.dv8tion.jda.api.entities.*;

import java.util.Optional;

public class ArgumentParser {

    private Guild guild;

    public Optional<?> parse(String argument, String parameter, Guild guild) {
        this.guild = guild;
        if (ParameterType.isJDAEntity(parameter)) {
            return parseJDAEntity(argument, parameter);
        }
        return parseByStringConstructor(argument, parameter);
    }

    protected Optional<?> parseByStringConstructor(String argument, String parameter) {
        try {
            Class<?> clazz = Class.forName(parameter);
            return Optional.of(clazz.getConstructor(String.class).newInstance(argument));
        } catch (Exception ignore) {
            return Optional.empty();
        }
    }

    protected Optional<?> parseJDAEntity(String argument, String parameter) {
        try {
            ParameterType type = ParameterType.getByName(parameter);
            switch (type) {
                case MEMBER:
                    return parseMember(argument);
                case USER:
                    return parseUser(argument);
                case ROLE:
                    return parseRole(argument);
                case TEXTCHANNEL:
                    return parseChannel(argument);
                default:
                    return Optional.empty();
            }
        } catch (Exception e) {
            throw new CommandException("Fatal! Argument parsing failed!", e);
        }
    }

    protected Optional<Member> parseMember(String argument) {
        argument = argument.replaceFirst("@", "");
        Member member;
        if (argument.matches("\\d+")) {
            member = guild.getMemberById(argument);
        } else {
            member = guild.getMembersByName(argument, true).stream().findFirst().orElse(null);
        }
        if (member == null) {
            return Optional.empty();
        }
        return Optional.of(member);
    }

    protected Optional<User> parseUser(String argument) {
        argument = argument.replaceFirst("@", "");
        User user;
        if (argument.matches("\\d+")) {
            user = guild.getJDA().getUserById(argument);
        } else {
            user = guild.getJDA().getUsersByName(argument, true).stream().findFirst().orElse(null);
        }
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    protected Optional<TextChannel> parseChannel(String argument) {
        argument = argument.replaceFirst("#", "");
        TextChannel textChannel;
        if (argument.matches("\\d+")) {
            textChannel = guild.getTextChannelById(argument);
        } else {
            textChannel = guild.getTextChannelsByName(argument, true).stream().findFirst().orElse(null);
        }
        if (textChannel == null) {
            return Optional.empty();
        }
        return Optional.of(textChannel);
    }

    protected Optional<Role> parseRole(String argument) {
        argument = argument.replaceFirst("@", "");
        Role role;
        if (argument.matches("\\d+")) {
            role = guild.getRoleById(argument);
        } else {
            role = guild.getRolesByName(argument, true).stream().findFirst().orElse(null);
        }
        if (role == null) {
            return Optional.empty();
        }
        return Optional.of(role);
    }

}
