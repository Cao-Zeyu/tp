package seedu.duke;

import seedu.duke.commands.Command;
import seedu.duke.common.Messages;
import seedu.duke.parser.Parser;
import seedu.duke.storage.Storage;
import seedu.duke.task.BookList;
import seedu.duke.task.CreditList;
import seedu.duke.task.ItemList;
import seedu.duke.task.LinkList;
import seedu.duke.task.ListType;
import seedu.duke.task.ModuleList;
import seedu.duke.task.TaskList;
import seedu.duke.ui.Ui;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point of the Duke application.
 * Initializes the application and starts the interaction with the user.
 */
public class Duke {

    private Storage storage;
    private TaskList tasks;
    private BookList books;
    private CreditList mealCredit;
    private ModuleList modules;
    private Storage linkStorage;
    private LinkList links;
  
    private final Map<ListType, ItemList> listMap = new EnumMap<>(ListType.class);
    private static final Logger dukeLogger = Logger.getLogger(Duke.class.getName());

    public Duke() {

        storage = new Storage();
        boolean errorMessage = false; // nvr show yet
        try {
            tasks = new TaskList(storage.loadTask());          
        } catch (DukeException e) {
            errorMessage = true;
            Ui.showError(e);
            tasks = new TaskList();
            Ui.dukePrint(Messages.MESSAGE_NEW_TASK_FILE);
        }
        try {
            books = new BookList(storage.loadBook());
        } catch (DukeException e) {
            if (!errorMessage) {
                Ui.showError(e);
                errorMessage = true;
            }
            books = new BookList();
            Ui.dukePrint(Messages.MESSAGE_NEW_BOOK_FILE);
        }
        try {
            mealCredit = new CreditList(storage.loadCredit());
        } catch (DukeException e) {
            if (!errorMessage) {
                Ui.showError(e);
                errorMessage = true;
            }
            mealCredit = new CreditList();
            Ui.dukePrint(Messages.MESSAGE_NEW_MEAL_CREDIT_FILE);
        }
        try {
            links = new LinkList(storage.loadLinks());
        } catch (DukeException e) {
            if (!errorMessage) {
                Ui.showError(e);
            }
            links = new LinkList();
            Ui.dukePrint(Messages.MESSAGE_NEW_LINK_FILE);
        }
        try {
            modules = new ModuleList(storage.loadModule());
        } catch (DukeException e) {
            if (!errorMessage) {
                Ui.showError(e);
            }
            modules = new ModuleList();
            Ui.dukePrint(Messages.MESSAGE_NEW_MODULE_FILE);
        }
     
        listMap.put(ListType.TASK_LIST, tasks);
        listMap.put(ListType.BOOK_LIST, books);
        listMap.put(ListType.CREDIT_LIST, mealCredit);
        listMap.put(ListType.LINK_LIST, links);
        listMap.put(ListType.MODULE_LIST, modules);
    }

    /**
     * Reads the user command and executes it, until the user issues the bye command.
     */
    public void run() {
        Ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = Ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(listMap);
                isExit = c.isExit();
                storage.save(tasks, Storage.TASK_STORAGE_FILEPATH);
                storage.save(books, Storage.BOOK_STORAGE_FILEPATH);
                storage.save(mealCredit, Storage.CREDIT_STORAGE_FILEPATH);
                storage.save(links, Storage.DEFAULT_LINK_FILEPATH);
                storage.save(modules, Storage.DEFAULT_MODULE_FILEPATH);
            } catch (DukeException e) {
                Ui.showError(e);
            }
        }
    }

    public static void main(String[] args) {
        dukeLogger.log(Level.INFO, "Logging started");
        new Duke().run();
    }
}