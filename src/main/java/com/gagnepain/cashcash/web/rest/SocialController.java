package com.gagnepain.cashcash.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import com.gagnepain.cashcash.service.SocialService;

@RestController
@RequestMapping("/social")
public class SocialController {
	private final Logger log = LoggerFactory.getLogger(SocialController.class);

	@Inject
	private SocialService socialService;

	@Inject
	private ProviderSignInUtils providerSignInUtils;

	@RequestMapping(value = "/signup",
			method = RequestMethod.GET)
	public RedirectView signUp(final WebRequest webRequest, @CookieValue(name = "NG_TRANSLATE_LANG_KEY",
			required = false,
			defaultValue = "\"en\"") final String langKey) {
		try {
			final Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
			socialService.createSocialUser(connection, langKey.replace("\"", ""));
			return new RedirectView(URIBuilder.fromUri("/#/social-register/" + connection.getKey()
					.getProviderId())
					.queryParam("success", "true")
					.build()
					.toString(), true);
		} catch (final Exception e) {
			log.error("Exception creating social user: ", e);
			return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider")
					.queryParam("success", "false")
					.build()
					.toString(), true);
		}
	}
}
