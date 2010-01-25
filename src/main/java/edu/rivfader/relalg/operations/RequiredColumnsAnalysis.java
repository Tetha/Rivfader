package edu.rivfader.relalg.operations;

import edu.rivfader.relalg.representation.IQualifiedColumnName;
import java.util.Collection;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.RenameTable;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import java.util.HashSet;
import edu.rivfader.rowselector.operations.RequiredColumnsOfRowSelector;


public class RequiredColumnsAnalysis
    extends BaseRelalgTransformation<Collection<IQualifiedColumnName>> {

    public Collection<IQualifiedColumnName> transformLoadTable(LoadTable l) {
        return new HashSet<IQualifiedColumnName>();
    }

    public Collection<IQualifiedColumnName> transformRenameTable(RenameTable r) {
        return new HashSet<IQualifiedColumnName>();
    }

    public Collection<IQualifiedColumnName> transformSelection(Selection s) {
        Collection<IQualifiedColumnName> result =
            new HashSet<IQualifiedColumnName>();
        RequiredColumnsOfRowSelector subAnalysis =
            new RequiredColumnsOfRowSelector();
        result.addAll(subAnalysis.transform(s.getPredicate()));
        result.addAll(transform(s.getSubExpression()));
        return result;
    }

    public Collection<IQualifiedColumnName> transformProjection(Projection p) {
        return transform(p.getSubExpression());
    }

    public Collection<IQualifiedColumnName> transformProduct(Product p) {
        Collection<IQualifiedColumnName> result =
            new HashSet<IQualifiedColumnName>();
        result.addAll(transform(p.getLeft()));
        result.addAll(transform(p.getRight()));
        return result;
    }
}
