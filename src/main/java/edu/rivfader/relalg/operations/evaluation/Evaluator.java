package edu.rivfader.relalg.operation.evaluation;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;

import edu.rivfader.relalg.operations.BaseRelalgTransformation;
import edu.rivfader.relalg.operations.IRelAlgExprTransformation;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.RenameTable;
import edu.rivfader.relalg.representation.IQualifiedNameRow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.io.IOException;

/**
 * evaluates a rel alg expression in a database into a lazily computed
 * set of rows.
 * @author harald
 */
public class Evaluator
    extends BaseRelalgTransformation<Iterator<IQualifiedNameRow>>
    implements IRelAlgExprTransformation<Iterator<IQualifiedNameRow>> {

    private Database context;

    public Evaluator(Database pContext) {
        context = pContext;
    }

    @Override
    public Iterator<IQualifiedNameRow> transformProduct(Product p){
        return new ProductEvaluationIterator(p, this);
    }

    @Override
    public Iterator<IQualifiedNameRow> transformProjection(Projection p){
        return new ProjectionEvaluationIterator(p, this);
    }

    @Override
    public Iterator<IQualifiedNameRow> transformSelection(Selection s){
        return new SelectionEvaluationIterator(s, this);
    }

    @Override
    public Iterator<IQualifiedNameRow> transformLoadTable(LoadTable l){
        try {
            return new LoadTableEvaluationIterator(
                                        context.loadTable(l.getName()),
                                        l.getName());
        } catch (IOException e) {
            throw new RuntimeException("loading the table did not work", e);
        }
    }

    @Override
    public Iterator<IQualifiedNameRow> transformRenameTable(RenameTable r){
        r.setDatabase(context);
        return new RenameTableEvaluationIterator(r.getSource().load(),
                                                 r.getName());
    }
}
