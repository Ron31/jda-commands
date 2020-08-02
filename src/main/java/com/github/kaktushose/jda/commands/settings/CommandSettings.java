package com.github.kaktushose.jda.commands.settings;

import java.util.HashSet;
import java.util.Set;

public final class CommandSettings {

    private String prefix;
    private long cooldown;
    private boolean ignoreBots, ignoreCase;
    private final Set<Long> mutedChannels;

    public CommandSettings() {
        this.mutedChannels = new HashSet<>();
        ignoreBots = true;
        ignoreCase = true;
    }

    public CommandSettings(String prefix) {
        this.mutedChannels = new HashSet<>();
        this.prefix = prefix;
        ignoreBots = true;
        ignoreCase = true;
    }

    public String getPrefix() {
        return prefix;
    }

    public CommandSettings setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public long getCooldown() {
        return cooldown;
    }

    public CommandSettings setCooldown(long cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public CommandSettings muteChannels(long... channelIds) {
        for (long id : channelIds)
            mutedChannels.add(id);
        return this;
    }

    public CommandSettings unmuteChannels(long... channelIds) {
        for (long id : channelIds)
            mutedChannels.remove(id);
        return this;
    }

    public Set<Long> getMutedChannels() {
        return mutedChannels;
    }

    public boolean isIgnoreBots() {
        return ignoreBots;
    }

    public void setIgnoreBots(boolean ignoreBots) {
        this.ignoreBots = ignoreBots;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

}
