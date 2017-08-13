package com.gagnepain.cashcash.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gagnepain.cashcash.web.rest.dto.ExternalCashRateDTO;

/**
 * Service to retrieve cash rate from external partner
 */
@Service
public class ExternalCashRateService {

	private final RestTemplate restTemplate;

	public ExternalCashRateService() {
		this.restTemplate = new RestTemplate();
	}

	public ExternalCashRateDTO getCashRate(String code) {
		Map<String, String> params = new HashMap<>();
		params.put("base", code);
		return this.restTemplate.getForObject("http://api.fixer.io/latest", ExternalCashRateDTO.class, params);
	}
}
