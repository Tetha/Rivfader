package edu.rivfader.test.relalg.representation;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.representation.QualifiedNameRow;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.rowselector.representation.IRowSelector;

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class SelectionTest {
    /*
    */
}
