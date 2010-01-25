package edu.rivfader.rowselector.operations;

import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.rowselector.representation.Always;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.BinaryOperation;

import java.util.Collection;
import java.util.HashSet;

public class RequiredColumnsOfRowSelector
    extends BaseRowSelectorTransformation<Collection<IQualifiedColumnName>> {

    protected Collection<IQualifiedColumnName> transformAlways(Always a) {
        return new HashSet<IQualifiedColumnName>();
    }

    protected Collection<IQualifiedColumnName>
        transformComparision(Comparision c) {
        Collection<IQualifiedColumnName> result =
            new HashSet<IQualifiedColumnName>();
        result.addAll(c.getLeft().getRequiredColumns());
        result.addAll(c.getRight().getRequiredColumns());
        return result;
    }

    protected Collection<IQualifiedColumnName>
        transformBinaryOperation(BinaryOperation bo) {
        Collection<IQualifiedColumnName> result =
            new HashSet<IQualifiedColumnName>();
        result.addAll(transform(bo.getLeft()));
        result.addAll(transform(bo.getRight()));
        return result;
    }
}
