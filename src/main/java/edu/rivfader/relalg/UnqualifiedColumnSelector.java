package edu.rivfader.relalg;

import java.util.Collection;

import java.util.Set;
import java.util.HashSet;

/**
 * Projects according to an unqualified column name.
 * @author harald
 */
public class UnqualifiedColumnSelector implements IColumnProjection {
    /**
     * contains the unqualified column name to project.
     */
    private String columnName;
    /**
     * constructs a column selector to select the designated unqualified
     * column.
     * @param pColumnName the column name to select.
     */
    public UnqualifiedColumnSelector(final String pColumnName) {
        columnName = pColumnName;
    }

    @Override
    public Collection<IQualifiedColumnName> project(
            final IQualifiedNameRow input) {
        Set<IQualifiedColumnName> r = new HashSet<IQualifiedColumnName>();
        r.add(input.resolveUnqualifiedName(columnName));
        return r;
    }
}
