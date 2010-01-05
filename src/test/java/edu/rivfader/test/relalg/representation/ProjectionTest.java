package edu.rivfader.test.relalg.representation;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.IColumnProjection;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.IQualifiedColumnName;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.powermock.api.easymock.annotation.Mock;
import static org.easymock.EasyMock.expect;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class ProjectionTest {
    private Projection subject;

}
