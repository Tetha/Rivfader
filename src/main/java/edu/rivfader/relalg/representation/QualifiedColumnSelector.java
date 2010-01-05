package edu.rivfader.relalg.representation;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * projects a fully qualified column name into the result set.
 * @author harald
 */
public class QualifiedColumnSelector implements IColumnProjection {
    /**
     * contains the name to project.
     */
    private IQualifiedColumnName projectedName;

    /**
     * constructs the new projection.
     * @param pProjectedName the name to project
     */
    public QualifiedColumnSelector(IQualifiedColumnName pProjectedName) {
        projectedName = pProjectedName;
    }

    @Override
    public Collection<IQualifiedColumnName> project(
            IQualifiedNameRow projectedRow) {
        Set<IQualifiedColumnName> o; // output

        o = new HashSet<IQualifiedColumnName>();
        o.add(projectedName);
        return o;
    }
}
