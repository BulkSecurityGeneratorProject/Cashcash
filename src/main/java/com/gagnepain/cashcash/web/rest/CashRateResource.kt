package com.gagnepain.cashcash.web.rest

import java.util.Optional
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashCurrency
import com.gagnepain.cashcash.domain.CashRate
import com.gagnepain.cashcash.domain.exception.BusinessException
import com.gagnepain.cashcash.service.CashRateService
import com.gagnepain.cashcash.web.rest.errors.CashError
import com.gagnepain.cashcash.web.rest.util.HeaderUtil

/**
 * REST controller for managing CashRate.
 */
@RestController
@RequestMapping("/api")
class CashRateResource {
    private val log = LoggerFactory.getLogger(CashRateResource::class.java)

    @Inject
    private lateinit var cashRateService: CashRateService

    /**
     * GET  /cash-rates/:code.

     * @param code
     * * 		the code of the base cashCurrency for the rate to retrieve
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the cashrate, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cash-rates/{code}", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun findOne(@PathVariable code: String): ResponseEntity<CashRate> {
        log.debug("REST request to get CashRate : {}", code)
        try {
            var result: CashRate? = cashRateService.findOne(code)
            if (result == null) {
                result = cashRateService.updateOrCreate(code)
            }
            if (result == null) {
                return ResponseEntity<CashRate>(HttpStatus.NOT_FOUND)
            } else {
                return ResponseEntity(result, HttpStatus.OK)
            }
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashRate", cashError.errorKey, cashError.message))
                    .body<CashRate>(null)
        }

    }
}
