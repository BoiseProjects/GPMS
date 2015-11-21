package gpms.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import gpms.model.AuditLog;
import gpms.model.Proposal;

public class ProposalDAO  extends BasicDAO<Proposal, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "proposal";

	private static Morphia morphia;
	private static Datastore ds;
	private AuditLog audit = new AuditLog();
//	DelegationDAO delegationDAO = null;
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public ProposalDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

}
