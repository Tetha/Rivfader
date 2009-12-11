package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;

/**
 * A valueprovider encapsulates means to get a value.
 *
 * Such means include, but are not limited to:
 *  - literal values (such that the means are 'do nothing')
 *  - column values (such that the means are 'ask row for column')
 * @author harald
 */
public interface IValueProvider {
    /**
     * Method called to get the data.
     * @param data the row to get data from
     * @return the value of this value provider
     */
    String getValue(IQualifiedNameRow data);
}

