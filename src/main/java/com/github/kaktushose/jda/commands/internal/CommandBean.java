package com.github.kaktushose.jda.commands.internal;

import java.lang.reflect.Method;
import java.util.List;

public class CommandBean {

    private final List<String> labels;
    private final String name;
    private final String description;
    private final String usage;
    private final boolean isDefault, isConcat, isOptional;
    private final List<String> parameters;
    private final Method method;
    private final Object instance;

    public CommandBean(List<String> labels,
                       String name,
                       String description,
                       String usage,
                       boolean isDefault,
                       boolean isConcat,
                       boolean isOptional,
                       List<String> parameters,
                       Method method,
                       Object instance) {
        this.labels = labels;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.isDefault = isDefault;
        this.isConcat = isConcat;
        this.isOptional = isOptional;
        this.parameters = parameters;
        this.method = method;
        this.instance = instance;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    boolean isDefault() {
        return isDefault;
    }

    public List<String> getParameters() {
        return parameters;
    }

    Method getMethod() {
        return method;
    }

    Object getInstance() {
        return instance;
    }

    boolean isConcat() {
        return isConcat;
    }

    boolean isOptional() {
        return isOptional;
    }

    @Override
    public String toString() {
        return method.getName() + "{" +
                "labels=" + labels +
                ", parameters=" + parameters +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", usage='" + usage + '\'' +
                ", isConcat=" + isConcat +
                ", isOptional=" + isOptional +
                ", instance=" + instance +
                '}';
    }
}
