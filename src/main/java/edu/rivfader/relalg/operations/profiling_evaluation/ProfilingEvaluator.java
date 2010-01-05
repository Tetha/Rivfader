package edu.rivfader.relalg.operations.profiling_evaluation;

import edu.rivfader.data.Database;

import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.operation.evaluation.Evaluator;
import edu.rivfader.relalg.operation.evaluation.ProjectionEvaluationIterator;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.operation.evaluation.SelectionEvaluationIterator;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.IQualifiedNameRow;

import java.util.Iterator;

public class ProfilingEvaluator extends Evaluator {
    private ICostAccumulator statisticsDestination;
    public ProfilingEvaluator(Database context,
                              ICostAccumulator pStatisticsDestination) {
        super(context);
        statisticsDestination = pStatisticsDestination;
    }

    @Override
    public Iterator<IQualifiedNameRow> transformSelection(Selection s) {
        return new SelectionStatisticsIterator(statisticsDestination, s,
                                                super.transformSelection(s));
    }

    @Override
    public Iterator<IQualifiedNameRow> transformProjection(Projection p) {
        return new ProjectionStatisticsIterator(statisticsDestination, p,
                                                super.transformProjection(p));
    }

    @Override
    public Iterator<IQualifiedNameRow> transformProduct(Product p) {
        return new ProductStatisticsIterator(statisticsDestination, p,
                                             super.transformProduct(p));
    }
}
