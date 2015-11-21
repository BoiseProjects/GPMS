package gpms.dao;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import gpms.model.AuditLog;
import gpms.model.GPMSCommonInfo;
import gpms.model.Proposal;
import gpms.model.UserProfile;

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

	public Proposal findProposalByProposalID(ObjectId id)
			throws UnknownHostException {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class).field("_id").equal(id).get();
	}
	
	public void deleteProposal(Proposal proposal, UserProfile authorProfile,
			GPMSCommonInfo gpmsCommonObj) {
		Datastore ds = getDatastore();
		proposal.setProposalStatus(Status.DELETED);
		AuditLog entry = new AuditLog(authorProfile, "Deleted Proposal for "
				+ proposal.getProjectInfo().getProjectTitle(), new Date());
		proposal.addEntryToAuditLog(entry);
		ds.save(proposal);
	}
	
}
