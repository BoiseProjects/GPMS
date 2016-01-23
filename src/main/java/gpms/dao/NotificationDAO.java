package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.NotificationLog;
import gpms.model.Proposal;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class NotificationDAO extends BasicDAO<NotificationLog, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "notification";

	private static Morphia morphia;
	private static Datastore ds;
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static Morphia getMorphia() throws UnknownHostException,
			MongoException {
		if (morphia == null) {
			morphia = new Morphia().map(UserProfile.class).map(
					UserAccount.class);
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

	public NotificationDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

	public long findAllNotificationCountAUser(String userProfileId,
			String userCollege, String userDepartment, String userPositionType,
			String userPositionTitle, boolean isUserAdmin)
			throws ParseException {
		Datastore ds = getDatastore();
		Query<NotificationLog> notificationQuery = ds
				.createQuery(NotificationLog.class);

		if (isUserAdmin) {
			notificationQuery.and(notificationQuery.criteria("for admin")
					.equal(true), notificationQuery.criteria("isViewedByAdmin")
					.equal(false));
			return notificationQuery.countAll();
		} else {
			notificationQuery.and(
					notificationQuery.criteria("isViewedByUser").equal(false),
					notificationQuery.criteria("user profile id").equal(
							userProfileId),
					notificationQuery.criteria("college").equal(userCollege),
					notificationQuery.criteria("department").equal(
							userDepartment),
					notificationQuery.criteria("position type").equal(
							userPositionType),
					notificationQuery.criteria("position title").equal(
							userPositionTitle),
					notificationQuery.criteria("for admin").equal(false));
			return notificationQuery.countAll();
		}
	}

	public List<NotificationLog> findAllNotificationForAUser(int offset,
			int limit, String userProfileId, String userCollege,
			String userDepartment, String userPositionType,
			String userPositionTitle, boolean isUserAdmin)
			throws ParseException {

		List<NotificationLog> notifications = new ArrayList<NotificationLog>();
		Query<NotificationLog> notificationQuery = ds
				.createQuery(NotificationLog.class)
				.retrievedFields(true, "_id", "type", "action", "proposal id",
						"proposal title", "user profile id", "user name",
						"college", "department", "position type",
						"position title", "is viewed by user",
						"is viewed by admin", "activity on", "for admin",
						"is critical").order("-activity on");

		if (isUserAdmin) {
			// int rowTotal = notificationQuery.asList().size();
			notifications = notificationQuery.offset(offset - 1).limit(limit)
					.asList();
		} else {
			notificationQuery.and(
					// notificationQuery.criteria("isViewedByUser").equal(false),
					notificationQuery.criteria("user profile id").equal(
							userProfileId),
					notificationQuery.criteria("college").equal(userCollege),
					notificationQuery.criteria("department").equal(
							userDepartment),
					notificationQuery.criteria("position type").equal(
							userPositionType),
					notificationQuery.criteria("position title").equal(
							userPositionTitle));

			notifications = notificationQuery.offset(offset - 1).limit(limit)
					.asList();
		}
		return notifications;
	}
}
