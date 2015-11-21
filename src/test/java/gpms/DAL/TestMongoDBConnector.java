package gpms.DAL;

import gpms.DAL.MongoDBConnector;
import junit.framework.TestCase;

public class TestMongoDBConnector extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetMongoDBInstance() {
		MongoDBConnector mdb = MongoDBConnector.getMongoDBInstance();
		assertNotNull(mdb);
	}
}
