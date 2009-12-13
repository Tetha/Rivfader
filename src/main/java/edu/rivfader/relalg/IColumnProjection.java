package edu.rivfader.relalg;

import edu.rivfader.relalg.IQualifiedNameRow;

import java.util.Collection;
/**
 * This is a single column projection, which is used to implement
 * entire table projections by using multiple single column projections.
 * @author harald
 */
public interface IColumnProjection {
    /**
     * Find the right column and select it by adding it to the returned
     * collection.
     * @param projectedRow the row with columns to project
     * @return a collection of projected columns.
     */
    Collection<IQualifiedColumnName> project(IQualifiedNameRow projectedRow);
}
