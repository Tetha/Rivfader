package edu.rivfader.optimizations;

import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.Product;

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
