package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import java.util.Iterator;
/**
 * This is the common interface for all relational algebra tree members.
 * @author harald
 */
public interface IRelAlgExpr {
    /**
     * This returns the (lazily evaluated) result set of rows.
     * @param context the database to evaluate the query in
     * @return an iterator over the row set
     */
    @Deprecated
    Iterator<IQualifiedNameRow> evaluate(Database context);
}
