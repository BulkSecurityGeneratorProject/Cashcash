package com.gagnepain.cashcash.config;

import java.util.Arrays;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.gagnepain.cashcash.config.liquibase.AsyncSpringLiquibase;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;

@Configuration
@EnableJpaRepositories("com.gagnepain.cashcash.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {
	private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

	@Inject
	private Environment env;

	@Autowired(required = false)
	private MetricRegistry metricRegistry;

	@Bean(destroyMethod = "close")
	@ConditionalOnExpression("#{!environment.acceptsProfiles('" + Constants.SPRING_PROFILE_CLOUD + "') && !environment.acceptsProfiles('" +
			Constants.SPRING_PROFILE_HEROKU + "')}")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource(final DataSourceProperties dataSourceProperties) {
		log.debug("Configuring Datasource");
		if (dataSourceProperties.determineUrl() == null) {
			log.error("Your database connection pool configuration is incorrect! The application" +
					" cannot start. Please check your Spring profile, current profiles are: {}", Arrays.toString(env.getActiveProfiles()));

			throw new ApplicationContextException("Database connection pool is not configured correctly");
		}
		final HikariDataSource hikariDataSource = (HikariDataSource) DataSourceBuilder.create(dataSourceProperties.getClassLoader())
				.type(HikariDataSource.class)
				.driverClassName(dataSourceProperties.determineDriverClassName())
				.url(dataSourceProperties.determineUrl())
				.username(dataSourceProperties.determineUsername())
				.password(dataSourceProperties.determineUsername())
				.build();

		if (metricRegistry != null) {
			hikariDataSource.setMetricRegistry(metricRegistry);
		}
		return hikariDataSource;
	}

	@Bean
	public SpringLiquibase liquibase(final DataSource dataSource, final DataSourceProperties dataSourceProperties,
			final LiquibaseProperties liquibaseProperties) {

		// Use liquibase.integration.spring.SpringLiquibase if you don't want Liquibase to start asynchronously
		final SpringLiquibase liquibase = new AsyncSpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:config/liquibase/master.xml");
		liquibase.setContexts(liquibaseProperties.getContexts());
		liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
		liquibase.setDropFirst(liquibaseProperties.isDropFirst());
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_NO_LIQUIBASE)) {
			liquibase.setShouldRun(false);
		} else {
			liquibase.setShouldRun(liquibaseProperties.isEnabled());
			log.debug("Configuring Liquibase");
		}

		return liquibase;
	}

	@Bean
	public Hibernate4Module hibernate4Module() {
		return new Hibernate4Module();
	}
}
