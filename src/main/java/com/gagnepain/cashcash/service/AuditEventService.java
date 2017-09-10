package com.gagnepain.cashcash.service;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.inject.Inject;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.config.audit.AuditEventConverter;
import com.gagnepain.cashcash.repository.PersistenceAuditEventRepository;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 * </p>
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AuditEventService {
	private final PersistenceAuditEventRepository persistenceAuditEventRepository;

	private final AuditEventConverter auditEventConverter;

	@Inject
	public AuditEventService(final PersistenceAuditEventRepository persistenceAuditEventRepository,
			final AuditEventConverter auditEventConverter) {

		this.persistenceAuditEventRepository = persistenceAuditEventRepository;
		this.auditEventConverter = auditEventConverter;
	}

	public Page<AuditEvent> findAll(final Pageable pageable) {
		return persistenceAuditEventRepository.findAll(pageable)
				.map(persistentAuditEvents -> auditEventConverter.convertToAuditEvent(persistentAuditEvents));
	}

	public Page<AuditEvent> findByDates(final LocalDateTime fromDate, final LocalDateTime toDate, final Pageable pageable) {
		return persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable)
				.map(persistentAuditEvents -> auditEventConverter.convertToAuditEvent(persistentAuditEvents));
	}

	public Optional<AuditEvent> find(final Long id) {
		return Optional.ofNullable(persistenceAuditEventRepository.findOne(id))
				.map(auditEventConverter::convertToAuditEvent);
	}
}
