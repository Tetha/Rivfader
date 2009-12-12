package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import java.util.Iterator;
import java.util.List;

public interface ITable extends IRelAlgExpr {
    List<IQualifiedColumnName> getColumnNames();
    void setDatabase(Database context);
    void openForWriting();
    Iterator<IQualifiedNameRow> load();
    Iterator<IQualifiedNameRow> evaluate(Database context);
    void storeRow(IQualifiedNameRow newRow);
    void appendRow(IQualifiedNameRow newRow);
    void close();
}
