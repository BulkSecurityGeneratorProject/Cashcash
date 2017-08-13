package com.gagnepain.cashcash.repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converter used for cashRate object
 */
public class CashRateMapConverter implements AttributeConverter<Map<String, BigDecimal>, String> {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public String convertToDatabaseColumn(final Map<String, BigDecimal> stringDoubleMap) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(stringDoubleMap);
		} catch (JsonProcessingException e) {
			log.error("Error when converting cashRate map to Json", e);
		}
		return "";
	}

	@Override
	public Map<String, BigDecimal> convertToEntityAttribute(final String jsonInString) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(jsonInString, Map.class);
		} catch (IOException e) {
			log.error("Error when converting cashRate map to Json", e);
		}
		return new HashMap<>();
	}
}
