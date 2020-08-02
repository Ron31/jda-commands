package com.github.kaktushose.jda.commands.api;

import com.github.kaktushose.jda.commands.internal.CommandBean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandMapper {

    public Optional<CommandBean> findCommand(List<CommandBean> commands, String[] input) {
        Optional<CommandBean> command = Optional.empty();
        for (int i = input.length - 1; i > -1; i--) {

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < i + 1; j++) {
                sb.append(input[j]).append(" ");
            }
            String generatedLabel = sb.toString().trim();
            List<CommandBean> possibleCommands = commands.stream().filter(cmd -> cmd.getLabels().stream().anyMatch(label -> {
                        boolean matches = true;
                        String[] expectedLabels = label.split(" ");
                        String[] actualLabelSplit = generatedLabel.split(" ");
                        if (expectedLabels.length != actualLabelSplit.length) {
                            return false;
                        }
                        for (int k = 0; k < expectedLabels.length; k++) {
                            if (!matches) {
                                return false;
                            }
                            matches = expectedLabels[k].startsWith(actualLabelSplit[k]);
                        }
                        return matches;
                    })
            ).collect(Collectors.toList());

            if (possibleCommands.size() > 1) {
                continue;
            }
            command = possibleCommands.stream().findFirst();
        }
        return command;
    }
}
