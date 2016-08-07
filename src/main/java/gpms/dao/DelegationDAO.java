package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.AuditLogInfo;
import gpms.model.Delegation;
import gpms.model.DelegationInfo;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.net.UnknownHostException;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class DelegationDAO extends BasicDAO<Delegation, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "delegation";

	private static Morphia morphia;
	private static Datastore ds;

	private static Morphia getMorphia() throws UnknownHostException,
			MongoException {
		if (morphia == null) {
			morphia = new Morphia().map(Delegation.class)
					.map(UserAccount.class);
		}
		return morphia;
	}

	@Override
	public Datastore getDatastore() {
		if (ds == null) {
			try {
				ds = getMorphia().createDatastore(MongoDBConnector.getMongo(),
						DBNAME);
			} catch (UnknownHostException | MongoException e) {
				e.printStackTrace();
			}
		}
		ds.ensureIndexes();
		return ds;
	}

	public DelegationDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

	public boolean revokeDelegation(Delegation existingDelegation,
			UserProfile authorProfile) {
		return false;
	}

	public List<DelegationInfo> findAllForUserDelegationGrid(int offset,
			int limit, String delegatee, String delegatedFrom,
			String delegatedTo, String delegatedAction, String isRevoked,
			String userProfileID, String userCollege, String userDepartment,
			String userPositionType, String userPositionTitle) {
		return null;
	}

	public List<DelegationInfo> findAllUserDelegations(String delegatee,
			String delegatedFrom, String delegatedTo, String delegatedAction,
			String isRevoked, String userProfileID, String userCollege,
			String userDepartment, String userPositionType,
			String userPositionTitle) {
		// TODO Auto-generated method stub
		return null;
	}

	public Delegation findDelegationByDelegationID(ObjectId id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AuditLogInfo> findAllForDelegationAuditLogGrid(int offset,
			int limit, ObjectId id, String action, String auditedBy,
			String activityOnFrom, String activityOnTo) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AuditLogInfo> findAllUserDelegationAuditLogs(ObjectId id,
			String action, String auditedBy, String activityOnFrom,
			String activityOnTo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveDelegation(Delegation existingDelegation,
			UserProfile authorProfile) {
		// TODO Auto-generated method stub

	}

	public void updateDelegation(Delegation existingDelegation,
			UserProfile authorProfile) {
		// TODO Auto-generated method stub

	}

}
