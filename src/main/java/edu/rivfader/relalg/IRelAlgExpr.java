package edu.rivfader.relalg;

import edu.rivfader.data.Row;
import java.util.Iterator;
/**
 * This is the common interface for all relational algebra tree members.
 * @author harald
 */
public interface IRelAlgExpr {
    /**
     * This returns the (lazily evaluated) result set of rows.
     * @return an iterator over the row set
     */
    Iterator<Row> evaluate();
}
