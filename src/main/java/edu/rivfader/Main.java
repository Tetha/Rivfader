package edu.rivfader;

import edu.rivfader.commands.ICommand;
import edu.rivfader.data.Database;
import edu.rivfader.errors.NoColumnValueMappingPossible;
import edu.rivfader.errors.UnknownColumnName;

import java.io.Reader;
import java.io.Writer;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.List;

/**
 * This is the central class which ties everything together and contains the
 * main method.
 * @author harald
 */
public class Main {
    /**
     * contains the database name to operate on.
     */
    private String databaseName;
    /**
     * initializes the core to use the database.
     * @param pDatabaseName a path to a database directory
     */
    public Main(final String pDatabaseName) {
        databaseName = pDatabaseName;
    }

    /**
     * executes the input string.
     * @param input a valid simpleSQL-Query.
     */
    public String run(final String input) {
        Reader inputStream = new StringReader(input);
        Writer output = new StringWriter();
        SimpleSQLParser parser = new SimpleSQLParser(inputStream);
        Database database = new Database(databaseName);

        List<ICommand> commands;
        try {
            commands = parser.File();
        } catch (ParseException e) {
            System.out.println(e.toString());
            return "Parse error: " + e.toString();
        }

        for(ICommand c : commands) {
            try {
                c.execute(database, output);
            } catch (IOException e) {
                System.out.println(e.toString());
                e.printStackTrace();
                return "Error: " + e.toString() + "\n";
            } catch (NoColumnValueMappingPossible e) {
                return "Error: " + e.getMessage() + "\n";
            } catch (UnknownColumnName e) {
                return "Error: " + e.getMessage() + "\n";
            }
        }
        return output.toString();
    }

    public static void main(String[] args) throws IOException {
        Main core = null;
        String queryFilename;
        try {
            core = new Main(args[0]);
            queryFilename = args[1];
        } catch (IndexOutOfBoundsException e) {
            System.err.println("USAGE: program DATABASE QUERYFILE");
            return;
        }

        StringBuilder query = new StringBuilder();
        BufferedReader fromQueryFile = new BufferedReader(
                                        new FileReader(
                                            new File(queryFilename)));
        while(true) {
            String line = fromQueryFile.readLine();
            if (line == null) {
                break;
            }
            query.append(line);
        }

        System.out.print(core.run(query.toString()));
    }
}
