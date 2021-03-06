package com.gagnepain.cashcash.web.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.gagnepain.cashcash.config.JHipsterProperties;

/**
 * This filter is used in production, to put HTTP cache headers with a long (1 month) expiration time.
 */
public class CachingHttpHeadersFilter implements Filter {
	// We consider the last modified date is the start up time of the server
	private final static long LAST_MODIFIED = System.currentTimeMillis();

	private long CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(1461L);

	private final JHipsterProperties jHipsterProperties;

	public CachingHttpHeadersFilter(final JHipsterProperties jHipsterProperties) {
		this.jHipsterProperties = jHipsterProperties;
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		CACHE_TIME_TO_LIVE = TimeUnit.DAYS.toMillis(jHipsterProperties.getHttp()
				.getCache()
				.getTimeToLiveInDays());
	}

	@Override
	public void destroy() {
		// Nothing to destroy
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		final HttpServletResponse httpResponse = (HttpServletResponse) response;

		httpResponse.setHeader("Cache-Control", "max-age=" + CACHE_TIME_TO_LIVE + ", public");
		httpResponse.setHeader("Pragma", "cache");

		// Setting Expires header, for proxy caching
		httpResponse.setDateHeader("Expires", CACHE_TIME_TO_LIVE + System.currentTimeMillis());

		// Setting the Last-Modified header, for browser caching
		httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);

		chain.doFilter(request, response);
	}
}
