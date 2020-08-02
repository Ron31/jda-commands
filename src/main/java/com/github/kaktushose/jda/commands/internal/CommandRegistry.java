package com.github.kaktushose.jda.commands.internal;

import com.github.kaktushose.jda.commands.annotations.Command;
import com.github.kaktushose.jda.commands.annotations.CommandController;
import com.github.kaktushose.jda.commands.annotations.ParameterOptions;
import com.github.kaktushose.jda.commands.settings.CommandSettings;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandRegistry {

    protected static final Logger log = LoggerFactory.getLogger(CommandRegistry.class);
    protected final Set<CommandBean> commands;
    protected final CommandSettings settings;

    public CommandRegistry(CommandSettings settings) {
        this.commands = new HashSet<>();
        this.settings = settings;
    }

    final Set<CommandBean> getCommands() {
        return commands;
    }

    protected void indexCommands() {
        Reflections reflections = new Reflections("");
        Set<Class<?>> controllerSet = reflections.getTypesAnnotatedWith(CommandController.class);
        log.debug("Indexing commands...");
        for (Class<?> controllerClass : controllerSet) {
            // validating CommandController
            log.debug("Found CommandController {}", controllerClass.getName());
            CommandController commandController = controllerClass.getAnnotation(CommandController.class);
            if (!commandController.isActive()) {
                log.debug("CommandController is set to inactive. Skipping this CommandController");
                continue;
            }
            // creating instance for method invoking
            Object instance;
            try {
                instance = controllerClass.getConstructors()[0].newInstance();
            } catch (Exception e) {
                log.warn("Unable to instantiate CommandController {}! Skipping this controller and its commands!",
                        controllerClass.getName(),
                        e);
                continue;
            }

            for (Method method : controllerClass.getDeclaredMethods()) {
                // validating each Command
                log.debug("Found command {}", method.getName());
                boolean isValid = true;
                Command command = method.getAnnotation(Command.class);
                if (!method.isAnnotationPresent(Command.class)) {
                    continue;
                }
                if (!command.isActive()) {
                    log.debug("Command is set inactive. Skipping this command");
                    continue;
                }
                if (!method.getReturnType().equals(Void.TYPE) || !Modifier.isPublic(method.getModifiers())) {
                    log.warn("Command {} has an invalid return type or access modifier! " +
                                    "Method must be declared as public void! Skipping this command!",
                            method.getName());
                    continue;
                }

                // creating all possible labels for a command
                List<String> labels = new ArrayList<>();
                for (String controllerLabel : commandController.value()) {
                    for (String commandLabel : command.value()) {
                        labels.add((controllerLabel + " " + commandLabel).trim());
                    }
                }

                // validating method signature
                List<String> parameter = new ArrayList<>();
                AnnotatedType[] parameterTypes = method.getAnnotatedParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    String name = parameterTypes[i].getType().getTypeName();

                    if (!isValid) {
                        continue;
                    }

                    // first method argument is not a CommandEvent
                    if (i == 0) {
                        isValid = name.equals(CommandEvent.class.getName());
                        if (!isValid) {
                            log.warn("Command {} has an invalid method signature! First parameter must be of type {}! " +
                                            "Skipping this command!",
                                    method.getName(),
                                    CommandEvent.class.getName());
                        }
                        continue;
                    }
                    name = ParameterType.wrap(name);
                    isValid = ParameterType.validate(name);
                    if (!isValid) {
                        log.warn("Command {} has an invalid method signature! {} is an unsupported method parameter! " +
                                        "Skipping this command!",
                                method.getName(),
                                name);
                    }
                    parameter.add(name);
                }
                if (!isValid) {
                    continue;
                }
                // validating ParameterOptions
                boolean concat = false;
                boolean isOptional = false;
                if (method.isAnnotationPresent(ParameterOptions.class)) {
                    ParameterOptions parameterOptions = method.getAnnotation(ParameterOptions.class);
                    concat = parameterOptions.concat();
                    isOptional = parameterOptions.isOptional();
                    if (concat) {
                        isValid = parameter.get(parameter.size() - 1).equals("java.lang.String");
                    }
                    if (!isValid) {
                        log.warn("Command {} has invalid ParameterOptions! " +
                                        "Concatenate is set true but last parameter is not a String! " +
                                        "Skipping this command!",
                                method.getName());
                        continue;
                    }
                }

                // this must change if command overloading is implemented
                if (commands.stream().anyMatch(commandBean -> commandBean.getLabels().stream().anyMatch(labels::contains))) {
                    log.warn("The labels for the command {} are already registered! Skipping this command!", method.getName());
                    continue;
                }
                String prefix = settings.getPrefix();
                        commands.add(new CommandBean(
                                labels,
                                format(command.name(), prefix),
                                format(command.desc(), prefix),
                                format(command.usage(), prefix),
                                command.isDefault(),
                                concat,
                                isOptional,
                                parameter,
                                method,
                                instance));
            }
        }
        commands.forEach(commandBean -> log.debug("Registered command {}", commandBean));
        log.info("Command indexing done! Indexed a total of {} commands!", commands.size());
    }

    protected String format(String format, String prefix) {
        return format.replaceAll("\\{prefix}", prefix);
    }

}
