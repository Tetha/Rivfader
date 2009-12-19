package edu.rivfader.profiling;

import edu.rivfader.data.Database;

import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.evaluation.Evaluator;
import edu.rivfader.evaluation.ProjectionEvaluationIterator;
import edu.rivfader.relalg.Selection;
import edu.rivfader.evaluation.SelectionEvaluationIterator;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.IQualifiedNameRow;

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
