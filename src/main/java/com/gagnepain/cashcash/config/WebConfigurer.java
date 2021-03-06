package com.gagnepain.cashcash.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import com.gagnepain.cashcash.web.filter.CachingHttpHeadersFilter;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {
	private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

	@Inject
	private Environment env;

	@Inject
	private JHipsterProperties jHipsterProperties;

	@Autowired(required = false)
	private MetricRegistry metricRegistry;

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		if (env.getActiveProfiles().length != 0) {
			log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
		}
		final EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
		initMetrics(servletContext, disps);
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
			initCachingHttpHeadersFilter(servletContext, disps);
		}

		log.info("Web application fully configured");
	}

	/**
	 * Set up Mime types and, if needed, set the document root.
	 */
	@Override
	public void customize(final ConfigurableEmbeddedServletContainer container) {
		final MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		// IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
		mappings.add("html", "text/html;charset=utf-8");
		// CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
		mappings.add("json", "text/html;charset=utf-8");
		container.setMimeMappings(mappings);

		// When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
		setLocationForStaticAssets(container);
	}

	private void setLocationForStaticAssets(final ConfigurableEmbeddedServletContainer container) {
		final File root;
		final String prefixPath = resolvePathPrefix();
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
			root = new File(prefixPath + "target/www/");
		} else {
			root = new File(prefixPath + "target/www/");
		}
		if (root.exists() && root.isDirectory()) {
			container.setDocumentRoot(root);
		}
	}

	/**
	 * Resolve path prefix to static resources.
	 */
	private String resolvePathPrefix() {
		final String fullExecutablePath = this.getClass()
				.getResource("")
				.getPath();
		final String rootPath = Paths.get(".")
				.toUri()
				.normalize()
				.getPath();
		final String extractedPath = fullExecutablePath.replace(rootPath, "");
		final int extractionEndIndex = extractedPath.indexOf("target/");
		if (extractionEndIndex <= 0) {
			return "";
		}
		return extractedPath.substring(0, extractionEndIndex);
	}

	/**
	 * Initializes the caching HTTP Headers Filter.
	 */
	private void initCachingHttpHeadersFilter(final ServletContext servletContext, final EnumSet<DispatcherType> disps) {
		log.debug("Registering Caching HTTP Headers Filter");
		final FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext.addFilter("cachingHttpHeadersFilter",
				new CachingHttpHeadersFilter(jHipsterProperties));

		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/content/*");
		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/app/*");
		cachingHttpHeadersFilter.setAsyncSupported(true);
	}

	/**
	 * Initializes Metrics.
	 */
	private void initMetrics(final ServletContext servletContext, final EnumSet<DispatcherType> disps) {
		log.debug("Initializing Metrics registries");
		servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
		servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);

		log.debug("Registering Metrics Filter");
		final FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());

		metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
		metricsFilter.setAsyncSupported(true);

		log.debug("Registering Metrics Servlet");
		final ServletRegistration.Dynamic metricsAdminServlet = servletContext.addServlet("metricsServlet", new MetricsServlet());

		metricsAdminServlet.addMapping("/management/jhipster/metrics/*");
		metricsAdminServlet.setAsyncSupported(true);
		metricsAdminServlet.setLoadOnStartup(2);
	}

//	@Bean
//	public PageableHandlerMethodArgumentResolver pageableResolver() {
//		PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
//		pageableResolver.setMaxPageSize(MAX_PAGE_SIZE);
//		PageRequest defaultPageable = new PageRequest(0, MAX_PAGE_SIZE);
//		pageableResolver.setFallbackPageable(defaultPageable);
//		return pageableResolver;
//	}

	@Bean
	@ConditionalOnProperty(name = "jhipster.cors.allowed-origins")
	public CorsFilter corsFilter() {
		log.debug("Registering CORS filter");
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = jHipsterProperties.getCors();
		source.registerCorsConfiguration("/api/**", config);
		source.registerCorsConfiguration("/v2/api-docs", config);
		source.registerCorsConfiguration("/oauth/**", config);
		return new CorsFilter(source);
	}
}
