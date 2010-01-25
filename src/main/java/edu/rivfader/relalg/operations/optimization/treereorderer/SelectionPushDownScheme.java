package edu.rivfader.relalg.operations.optimization.treereorderer;

import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.data.Database;
import edu.rivfader.relalg.operations.RequiredColumnsAnalysis;
import edu.rivfader.relalg.operations.ProvidedColumnsAnalysis;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import java.util.Collection;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
public class SelectionPushDownScheme implements IReorderingScheme {
    private Database context;
    private IRelAlgExpr acceptedSelection;

    public SelectionPushDownScheme(Database pContext) {
        context = pContext;
    }

    public boolean acceptsProjection(Projection p) {
        return false;
    }

    public boolean acceptsSelection(Selection s) {
        acceptedSelection = s;
        return true;
    }

    public IReorderingScheme.ActionAlongUnaryOperation moveAlongSelection(Selection s) {
        return IReorderingScheme.ActionAlongUnaryOperation.TRIVIAL_MOVE_DOWN;
    }

    public IReorderingScheme.ActionAlongUnaryOperation moveAlongProjection(Projection p) {
        return IReorderingScheme.ActionAlongUnaryOperation.MOVE_DOWN;
    }

    public IReorderingScheme.ActionAlongBinaryOperation moveAlongProduct(Product p) {
        ProvidedColumnsAnalysis providedColumns = new ProvidedColumnsAnalysis(context);
        RequiredColumnsAnalysis requiredColumnsAnalysis = new RequiredColumnsAnalysis();
        Collection<IQualifiedColumnName> requiredColumns
            = requiredColumnsAnalysis.transform(acceptedSelection);

        if (providedColumns.transform(p.getLeft())
                .containsAll(requiredColumns)) {
            return IReorderingScheme.ActionAlongBinaryOperation.MOVE_LEFT;
        }

        if (providedColumns.transform(p.getRight())
                .containsAll(requiredColumns)) {
            return IReorderingScheme.ActionAlongBinaryOperation.MOVE_RIGHT;
        }

        return IReorderingScheme.ActionAlongBinaryOperation.NO_OPERATION;
    }
}
