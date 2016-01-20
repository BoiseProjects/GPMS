package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.NotificationLog;
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

		if (isUserAdmin) {
			return ds.createQuery(NotificationLog.class)
					.field("isViewedByAdmin").equal(false).countAll();
		} else {
			Query<NotificationLog> notificationQuery = ds
					.createQuery(NotificationLog.class);
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
							userPositionTitle));
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
				.createQuery(NotificationLog.class);
		if (isUserAdmin) {
			// int rowTotal = notificationQuery.asList().size();
			notifications = notificationQuery.offset(offset - 1).limit(limit)
					.asList();
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
							userPositionTitle));

			notifications = notificationQuery.offset(offset - 1).limit(limit)
					.asList();
		}
		return notifications;
	}
}
