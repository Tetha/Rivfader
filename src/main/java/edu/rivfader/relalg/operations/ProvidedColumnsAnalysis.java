package edu.rivfader.relalg.operations;

import java.util.Collection;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.RenameTable;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.data.Database;
import java.util.HashSet;
import edu.rivfader.relalg.representation.IColumnProjection;
import edu.rivfader.relalg.representation.QualifiedNameRow;

public class ProvidedColumnsAnalysis
                extends BaseRelalgTransformation<Collection<IQualifiedColumnName>> {
    private Database context;

    public ProvidedColumnsAnalysis(Database pContext) {
        context = pContext;
    }

    public Collection<IQualifiedColumnName> transformLoadTable(LoadTable l) {
        l.setDatabase(context);
        return l.getColumnNames();
    }

    public Collection<IQualifiedColumnName> transformRenameTable(RenameTable r) {
        r.setDatabase(context);
        return r.getColumnNames();
    }

    public Collection<IQualifiedColumnName> transformProduct(Product p) {
        Collection<IQualifiedColumnName> result =
            new HashSet<IQualifiedColumnName>();
        result.addAll(transform(p.getLeft()));
        result.addAll(transform(p.getRight()));
        return result;
    }

    public Collection<IQualifiedColumnName> transformSelection(Selection s) {
        return transform(s.getSubExpression());
    }

    public Collection<IQualifiedColumnName> transformProjection(Projection p) {
        Collection<IQualifiedColumnName> subResult = transform(p.getSubExpression());
        Collection<IQualifiedColumnName> result =
            new HashSet<IQualifiedColumnName>();
        for (IColumnProjection colproj : p.getSelectedFields()) {
            result.addAll(colproj.project(new QualifiedNameRow(subResult)));
        }
        return result;
    }
}
