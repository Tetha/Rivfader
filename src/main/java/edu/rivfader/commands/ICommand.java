package edu.rivfader.commands;

import edu.rivfader.data.Database;

public interface ICommand {
    void execute(Database context);
}
