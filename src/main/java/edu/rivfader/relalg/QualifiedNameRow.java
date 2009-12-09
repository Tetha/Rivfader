package edu.rivfader.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.errors.AmbiguousColumnName;
import edu.rivfader.errors.UnknownColumnName;
import edu.rivfader.errors.TableAmbiguous;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class QualifiedNameRow implements IQualifiedNameRow {
    private Map<QualifiedColumnName, String> values;

    public QualifiedNameRow(QualifiedColumnName... columnNames) {
        values = new HashMap<QualifiedColumnName, String>();
        for (QualifiedColumnName cn : columnNames) {
            values.put(cn, null);
        }
    }

    public QualifiedNameRow(Collection<QualifiedColumnName> columnNames) {
        values = new HashMap<QualifiedColumnName, String>();
        for(QualifiedColumnName cn : columnNames) {
            values.put(cn, null);
        }
    }

    @Override
    public Collection<QualifiedColumnName> columns() {
        return new HashSet<QualifiedColumnName>(values.keySet());
    }

    @Override
    public QualifiedColumnName resolveUnqualifiedName(final String columnName) {
        QualifiedColumnName r = null; // result
        for(QualifiedColumnName n : values.keySet()) { // name
            if (n.getColumn().equals(columnName)) {
                if (r == null) {
                    r = n;
                } else {
                    throw new AmbiguousColumnName(columnName);
                }
            }
        }
        if (r == null) {
            throw new UnknownColumnName(columnName);
        }
        return r;
    }

    @Override
    public String getData(final QualifiedColumnName column) {
        if (column == null) {
            throw new IllegalArgumentException("column == null");
        }
        return values.get(column);
    }

    @Override
    public void setData(final QualifiedColumnName column,
                        final String newData) {
        if (column == null) {
            throw new IllegalArgumentException("column == null");
        }
        if (newData == null) {
            throw new IllegalArgumentException("newData == null");
        }
        if (!values.containsKey(column)) {
            throw new UnknownColumnName(column.toString());
        }
        values.put(column, newData);
    }

    /**
     * constructs a QualifiedNameRow with the same columns and data as
     * the source row.
     * @param tableName the tableName of the unqualified row
     * @param source the data row to use
     */
    public static QualifiedNameRow fromRow(final String tableName,
                                           final Row source) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName == null");
        }

        if (source == null) {
            throw new IllegalArgumentException();
        }

        Set<QualifiedColumnName> columnNames =
            new HashSet<QualifiedColumnName>();
        for (String cn : source.columns()) { // columnname
            columnNames.add(new QualifiedColumnName(tableName, cn));
        }
        QualifiedNameRow r = new QualifiedNameRow(columnNames); // result
        for (String cn : source.columns()) {
            r.setData(new QualifiedColumnName(tableName, cn),
                      source.getData(cn));
        }
        return r;
    }

    /**
     * turns a qualifiedNameRow into a regular row.
     * requires that all table names are identical.
     * @param source the sorce row
     */
    public static Row toRow(QualifiedNameRow source) {
        Set<String> tns = new HashSet<String>(); // table names
        Set<String> cns = new HashSet<String>(); // column names
        for(QualifiedColumnName cn : source.columns()) {
            tns.add(cn.getTable());
            cns.add(cn.getColumn());
        }

        if (tns.size() > 1) {
            throw new TableAmbiguous(tns);
        }

        Row r = new Row(cns.iterator());

        for (QualifiedColumnName cn : source.columns()) {
            r.setData(cn.getColumn(), source.getData(cn));
        }
        return r;
    }
}
