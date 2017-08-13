package com.gagnepain.cashcash.repository.custom.annotation;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

public class CurrentZonedDateTimeGenerator implements ValueGenerator<ZonedDateTime> {
	@Override
	public ZonedDateTime generateValue(final Session session, final Object owner) {
		return ZonedDateTime.now(ZoneId.systemDefault());
	}
}
