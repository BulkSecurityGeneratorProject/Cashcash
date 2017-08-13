package com.gagnepain.cashcash.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public class HeaderUtil {
	private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

	public static HttpHeaders createAlert(final String message, final String param) {
		final HttpHeaders headers = new HttpHeaders();
		headers.add("X-cashcashApp-alert", message);
		headers.add("X-cashcashApp-params", param);
		return headers;
	}

	public static HttpHeaders createEntityCreationAlert(final String entityName, final String param) {
		return createAlert("A new " + entityName + " is created with identifier " + param, param);
	}

	public static HttpHeaders createEntityListCreationAlert(final String entityName, final String param) {
		return createAlert("New " + entityName + "s are created", param);
	}

	public static HttpHeaders createEntityUpdateAlert(final String entityName, final String param) {
		return createAlert("A " + entityName + " is updated with identifier " + param, param);
	}

	public static HttpHeaders createEntityListUpdateAlert(final String entityName, final String param) {
		return createAlert("Some " + entityName + "s are updated", param);
	}

	public static HttpHeaders createEntityDeletionAlert(final String entityName, final String param) {
		return createAlert("A " + entityName + " is deleted with identifier " + param, param);
	}

	public static HttpHeaders createFailureAlert(final String entityName, final String errorKey, final String defaultMessage) {
		log.error("Entity creation failed, {}", defaultMessage);
		final HttpHeaders headers = new HttpHeaders();
		headers.add("X-cashcashApp-error", defaultMessage);
		headers.add("X-cashcashApp-params", entityName);
		return headers;
	}
}
