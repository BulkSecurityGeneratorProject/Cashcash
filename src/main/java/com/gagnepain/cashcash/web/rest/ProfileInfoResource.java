package com.gagnepain.cashcash.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gagnepain.cashcash.config.JHipsterProperties;

@RestController
@RequestMapping("/api")
public class ProfileInfoResource {
	@Inject
	Environment env;

	@Inject
	private JHipsterProperties jHipsterProperties;

	@RequestMapping(value = "/profile-info",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfileInfoResponse getActiveProfiles() {
		return new ProfileInfoResponse(env.getActiveProfiles(), getRibbonEnv());
	}

	private String getRibbonEnv() {
		final String[] activeProfiles = env.getActiveProfiles();
		final String[] displayOnActiveProfiles = jHipsterProperties.getRibbon()
				.getDisplayOnActiveProfiles();

		if (displayOnActiveProfiles == null) {
			return null;
		}

		final List<String> ribbonProfiles = new ArrayList<>(Arrays.asList(displayOnActiveProfiles));
		final List<String> springBootProfiles = Arrays.asList(activeProfiles);
		ribbonProfiles.retainAll(springBootProfiles);

		if (ribbonProfiles.size() > 0) {
			return ribbonProfiles.get(0);
		}
		return null;
	}

	class ProfileInfoResponse {
		public String[] activeProfiles;

		public String ribbonEnv;

		ProfileInfoResponse(final String[] activeProfiles, final String ribbonEnv) {
			this.activeProfiles = activeProfiles;
			this.ribbonEnv = ribbonEnv;
		}
	}
}
