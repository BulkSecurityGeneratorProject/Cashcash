package com.gagnepain.cashcash.config.audit;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.gagnepain.cashcash.domain.PersistentAuditEvent;

@Component
public class AuditEventConverter {
	/**
	 * Convert a list of PersistentAuditEvent to a list of AuditEvent
	 *
	 * @param persistentAuditEvents
	 * 		the list to convert
	 *
	 * @return the converted list.
	 */
	public List<AuditEvent> convertToAuditEvent(final Iterable<PersistentAuditEvent> persistentAuditEvents) {
		if (persistentAuditEvents == null) {
			return Collections.emptyList();
		}
		final List<AuditEvent> auditEvents = new ArrayList<>();
		for (final PersistentAuditEvent persistentAuditEvent : persistentAuditEvents) {
			auditEvents.add(convertToAuditEvent(persistentAuditEvent));
		}
		return auditEvents;
	}

	/**
	 * Convert a PersistentAuditEvent to an AuditEvent
	 *
	 * @param persistentAuditEvent
	 * 		the event to convert
	 *
	 * @return the converted list.
	 */
	public AuditEvent convertToAuditEvent(final PersistentAuditEvent persistentAuditEvent) {
		final Instant instant = persistentAuditEvent.getAuditEventDate()
				.atZone(ZoneId.systemDefault())
				.toInstant();
		return new AuditEvent(Date.from(instant), persistentAuditEvent.getPrincipal(), persistentAuditEvent.getAuditEventType(),
				convertDataToObjects(persistentAuditEvent.getData()));
	}

	/**
	 * Internal conversion. This is needed to support the current SpringBoot actuator AuditEventRepository interface
	 *
	 * @param data
	 * 		the data to convert
	 *
	 * @return a map of String, Object
	 */
	public Map<String, Object> convertDataToObjects(final Map<String, String> data) {
		final Map<String, Object> results = new HashMap<>();

		if (data != null) {
			for (final String key : data.keySet()) {
				results.put(key, data.get(key));
			}
		}
		return results;
	}

	/**
	 * Internal conversion. This method will allow to create additional data.
	 * By default, it will create the object as string
	 *
	 * @param data
	 * 		the data to convert
	 *
	 * @return a map of String, String
	 */
	public Map<String, String> convertDataToStrings(final Map<String, Object> data) {
		final Map<String, String> results = new HashMap<>();

		if (data != null) {
			for (final String key : data.keySet()) {
				final Object object = data.get(key);

				// Extract the data that will be saved.
				if (object instanceof WebAuthenticationDetails) {
					final WebAuthenticationDetails authenticationDetails = (WebAuthenticationDetails) object;
					results.put("remoteAddress", authenticationDetails.getRemoteAddress());
					results.put("sessionId", authenticationDetails.getSessionId());
				} else if (object != null) {
					results.put(key, object.toString());
				} else {
					results.put(key, "null");
				}
			}
		}

		return results;
	}
}
