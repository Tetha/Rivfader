package edu.rivfader.relalg.operations.optimization.treereorderer;

import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;

public interface IReorderingScheme {
    public enum ActionAlongUnaryOperation {
        /**
         * tells the TreeReorderer to move the upper operation below
         * the lower one.
         */
        MOVE_DOWN,

        /**
         * Same as MOVE_DOWN, but is considered trivial.
         */
        TRIVIAL_MOVE_DOWN,

        /**
         * tells the tree reorderer to not do anything.
         */
        NO_OPERATION
    };

    public enum ActionAlongBinaryOperation {
        /**
         * tells the tree reorderer to make the upper node the left sub node
         * of the lower node.
         */
        MOVE_LEFT,

        /**
         * tells the tree reorderer to make the upper node the right sub node
         * of the lower node.
         */
        MOVE_RIGHT,

        /**
         * tells the tree reorderer to do nothing.
         */
        NO_OPERATION
    };

    boolean acceptsProjection(Projection potentialRock);
    boolean acceptsSelection(Selection potentialRock);

    ActionAlongUnaryOperation moveAlongProjection(Projection potentialObstacle);
    ActionAlongUnaryOperation moveAlongSelection(Selection potentialObstacle);
    ActionAlongBinaryOperation moveAlongProduct(Product potentialObstacle);
}
