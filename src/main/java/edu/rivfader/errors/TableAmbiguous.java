package edu.rivfader.errors;

import java.util.Collection;
/**
 * This error is thrown if a QualifiedNameRow with multiple Table names
 * is converted to an unqualified Row, as this wold lose information.
 * @author harald
 */
public class TableAmbiguous extends RuntimeException {
    /**
     * constructs a new eror
     * @param tableNames the collectin of names.
     */
    public TableAmbiguous(Collection<String> tableNames) {
        super("Ambiguous tablename: " + tableNames.toString());
    }
}
