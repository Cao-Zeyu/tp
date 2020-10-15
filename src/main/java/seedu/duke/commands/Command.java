package seedu.duke.commands;

import seedu.duke.DukeException;
import seedu.duke.task.TaskList;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents an executable command.
 */

public abstract class Command {

    /**
     * Represents a general command.
     *
     * @return whether to exit Duke application.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Executes the command.
     *
     * @param tasks a TaskList object containing all tasks
     */
    public abstract void execute(TaskList tasks) throws DukeException;
}
