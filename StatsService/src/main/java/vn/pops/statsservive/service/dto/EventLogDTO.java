package vn.pops.statsservive.service.dto;


import java.util.UUID;

import javax.validation.constraints.NotNull;
// TODO: Move to common lib
public class EventLogDTO {

	private UUID id;

    @NotNull
    private String userId;

    @NotNull
    private String entityType;

    private String entityId;

    private long time;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

    
}
