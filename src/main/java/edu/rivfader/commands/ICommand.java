package edu.rivfader.commands;

import edu.rivfader.data.Database;

import java.io.Writer;
import java.io.IOException;

/**
 * This is the interface for all toplevel commands.
 *
 * A toplevel command is basically a way of representing a
 * certain type of query, for example, there is a toplevel
 * command to drop a table, to update a table or to query 
 * a table (which is the print toplevel command).
 * This design was chosen, as it solves the problems
 * 'how to write in relational algebra?' and 
 * 'why does a select query print things on the console?'
 * in a clean way: They are just toplevel commands which
 * are parametrized with relational algebra expressions.
 * @author harald
 */
public interface ICommand {
    /**
     * this is called to execute the toplevel command.
     * @param context the database to evaluate the command in
     * @param output a place to write output to
     */
    void execute(Database context, Writer output) throws IOException;
}
