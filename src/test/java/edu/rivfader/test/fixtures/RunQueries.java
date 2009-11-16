package edu.rivfader.test.fixtures;

import edu.rivfader.Main;

public class RunQueries {
    private Main core;
    private String output;

    public RunQueries() {
    }

    public void setInput(String query) {
        System.err.println(query);
        core = new Main("/tmp/fitnesse-database");
        output = core.run(query);
    }

    public String output() {
        return output;
    }
}
