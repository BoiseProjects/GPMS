package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.Delegation;
import gpms.model.SignatureInfo;
import gpms.model.UserAccount;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

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

	public List<SignatureInfo> findDelegatedUsersForAUser(ObjectId userId,
			String positionTitle, String proposalId) {
		Datastore ds = getDatastore();
		List<SignatureInfo> signatures = new ArrayList<SignatureInfo>();

		Query<Delegation> delegationQuery = ds.createQuery(Delegation.class);

		Query<Delegation> q = ds.createQuery(Delegation.class)
				.field("user profile id").equal(userId).field("from")
				.greaterThanOrEq(new Date()).field("to")
				.lessThanOrEq(new Date());
		List<Delegation> delegations = q.asList();

		for (Delegation delegation : delegations) {
			if (delegation.getProposalId() != "") {
				delegationQuery.criteria("proposal id").containsIgnoreCase(
						proposalId);
			}
		}

		// delegationQuery.and(
		// delegationQuery.criteria("user profile id").equal(userId),
		// delegationQuery.criteria("proposal id").containsIgnoreCase(
		// proposalId));

		List<Delegation> delegates = delegationQuery.asList();

		for (Delegation delegation : delegates) {
			SignatureInfo signature = new SignatureInfo();
			// Adding PI
			switch (positionTitle) {
			case "PI":
				signature.setUserProfileId(delegation.getUserProfile().getId()
						.toString());
				signature
						.setFullName(delegation.getUserProfile().getFullName());

				signature.setDelegated(true);
				signature.setDelegatedAs("PI");
				signatures.add(signature);
				break;

			case "Co-PI":
				signature.setUserProfileId(delegation.getUserProfile().getId()
						.toString());
				signature
						.setFullName(delegation.getUserProfile().getFullName());

				signature.setDelegated(true);
				signature.setDelegatedAs("Co-PI");
				signatures.add(signature);
				break;

			case "Senior Personnel":
				signature.setUserProfileId(delegation.getUserProfile().getId()
						.toString());
				signature
						.setFullName(delegation.getUserProfile().getFullName());

				signature.setDelegated(true);
				signature.setDelegatedAs("Senior Personnel");
				signatures.add(signature);
				break;

			case "Department Chair":
				signature.setUserProfileId(delegation.getUserProfile().getId()
						.toString());
				signature
						.setFullName(delegation.getUserProfile().getFullName());

				signature.setDelegated(true);
				signature.setDelegatedAs("Department Chair");
				signatures.add(signature);
				break;

			case "University Research Director":
				signature.setUserProfileId(delegation.getUserProfile().getId()
						.toString());
				signature
						.setFullName(delegation.getUserProfile().getFullName());

				signature.setDelegated(true);
				signature.setDelegatedAs("University Research Director");
				signatures.add(signature);
				break;

			case "Dean":
				signature.setUserProfileId(delegation.getUserProfile().getId()
						.toString());
				signature
						.setFullName(delegation.getUserProfile().getFullName());

				signature.setDelegated(true);
				signature.setDelegatedAs("Dean");
				signatures.add(signature);
				break;

			case "Business Manager":
				signature.setUserProfileId(delegation.getUserProfile().getId()
						.toString());
				signature
						.setFullName(delegation.getUserProfile().getFullName());

				signature.setDelegated(true);
				signature.setDelegatedAs("Business Manager");
				signatures.add(signature);

			default:
				break;
			}
		}
		return signatures;

	}
}
