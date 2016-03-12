package gpms.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "id", "version", "auditLog" })
public abstract class BaseEntity {
	@Id
	@Property("id")
	@JsonProperty("id")
	protected ObjectId id;

	@Version
	@Property("version")
	@JsonProperty("version")
	private Long version;

	@Embedded("audit log")
	@JsonProperty("auditLog")
	private List<AuditLog> auditLog = new ArrayList<AuditLog>();

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

	public List<AuditLog> getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(ArrayList<AuditLog> auditLog) {
		this.auditLog = auditLog;
	}

}
