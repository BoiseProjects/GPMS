package gpms.model;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;

public abstract class BaseEntity {
	@Id
	@Property("id")
	protected ObjectId id;

	@Version
	@Property("version")
	private Long version;

	@Embedded("audit log")
	private ArrayList<AuditLog> auditLog = new ArrayList<AuditLog>();

	public BaseEntity() {
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public ArrayList<AuditLog> getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(ArrayList<AuditLog> auditLog) {
		this.auditLog = auditLog;
	}

}
