package org.panacea.drmp.nag.domain.graph;

import java.util.Objects;


/*@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,include = JsonTypeInfo.As.PROPERTY,property = "type",defaultImpl = Void.class)
@JsonSubTypes({
		@JsonSubTypes.Type(name="NDNone",value = NDNone.class),
		@JsonSubTypes.Type(name="NDUser",value = NDUser.class),
		@JsonSubTypes.Type(name="NDRoot",value = NDRoot.class),
})*/

public class NDPrivilege {

	public String deviceId;
	public String uuid;
	public String privLevel;


	public NDPrivilege(String deviceId, String uuid, String privLevel) {
		this.deviceId = deviceId;
		this.uuid = uuid;
		this.privLevel = privLevel;
	}

	public NDPrivilege() {

	}

	@Override
	public String toString() {
		return "NDPrivilege{" +
				"deviceHostname='" + deviceId + '\'' +
				", uuid='" + uuid + '\'' +
				", privLevel='" + privLevel + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NDPrivilege that = (NDPrivilege) o;
		return uuid.equals(that.uuid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}
}
