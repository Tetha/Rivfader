package edu.rivfader.relalg;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public class QualifiedColumnSelector implements IColumnProjection {
    private IQualifiedColumnName projectedName;

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
