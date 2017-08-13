package com.gagnepain.cashcash.web.rest

import java.net.URI
import java.net.URISyntaxException
import java.util.Optional
import javax.inject.Inject
import javax.validation.Valid

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashCsvConfig
import com.gagnepain.cashcash.domain.CashCurrency
import com.gagnepain.cashcash.domain.exception.BusinessException
import com.gagnepain.cashcash.service.CashCurrencyService
import com.gagnepain.cashcash.web.rest.errors.CashError
import com.gagnepain.cashcash.web.rest.util.HeaderUtil

/**
 * REST controller for managing CashCurrency.
 */
@RestController
@RequestMapping("/api")
class CashCurrencyResource {
    private val log = LoggerFactory.getLogger(CashCurrencyResource::class.java)

    @Inject
    private lateinit var cashCurrencyService: CashCurrencyService

    /**
     * POST  /cash-currencies : Create a new cashCurrency.

     * @param cashCurrency
     * * 		the cashCurrency to create
     * *
     * *
     * @return the ResponseEntity with status 201 (Created) and with body the new cashCurrency, or with status 400 (Bad Request) if the cashCurrency has already an ID
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-currencies", method = arrayOf(RequestMethod.POST), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun create(@Valid @RequestBody cashCurrency: CashCurrency): ResponseEntity<CashCurrency> {
        log.debug("REST request to create CashCurrency : {}", cashCurrency)
        if (cashCurrency.id != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCurrency", "idexists", "A new cashCurrency cannot already have an ID"))
                    .body<CashCurrency>(null)
        }

        try {
            val result = cashCurrencyService.create(cashCurrency)
            return ResponseEntity.created(URI("/api/cash-currencies/" + result.id!!))
                    .headers(HeaderUtil.createEntityCreationAlert("cashCurrency", result.id!!
                            .toString()))
                    .body(result)
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCsvConfig", cashError.errorKey, cashError.message))
                    .body<CashCurrency>(null)
        }

    }

    /**
     * PUT  /cash-currencies : Updates an existing cashCurrency.

     * @param cashCurrency
     * * 		the cashCurrency to update
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the updated cashCurrency,
     * * or with status 400 (Bad Request) if the cashCurrency is not valid,
     * * or with status 500 (Internal Server Error) if the cashCurrency couldnt be updated
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-currencies", method = arrayOf(RequestMethod.PUT), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun update(@Valid @RequestBody cashCurrency: CashCurrency): ResponseEntity<CashCurrency> {
        log.debug("REST request to update CashCurrency : {}", cashCurrency)
        if (cashCurrency.id == null) {
            return create(cashCurrency)
        }

        try {
            val result = cashCurrencyService.update(cashCurrency)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("cashCurrency", cashCurrency.id!!
                            .toString()))
                    .body(result)
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCurrency", cashError.errorKey, cashError.message))
                    .body<CashCurrency>(null)
        }

    }

    /**
     * GET  /cash-currencies : get all the cashCurrencies.

     * @return the ResponseEntity with status 200 (OK) and the list of cashCurrencies in body
     */
    @RequestMapping(value = "/cash-currencies", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun findAll(): List<CashCurrency> {
        log.debug("REST request to get all CashCurrencies")
        return cashCurrencyService.findAll()
    }

    /**
     * GET  /cash-currencies/:id : get the "id" cashCurrency.

     * @param id
     * * 		the id of the cashCurrency to retrieve
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the cashCurrency, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cash-currencies/{id}", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun findOne(@PathVariable id: Long?): ResponseEntity<CashCurrency> {
        log.debug("REST request to get CashCurrency : {}", id)
        try {
            val result = cashCurrencyService.findOne(id)
            if (result == null) {
                return ResponseEntity<CashCurrency>(HttpStatus.NOT_FOUND)
            } else {
                return ResponseEntity(result, HttpStatus.OK)
            }
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCurrency", cashError.errorKey, cashError.message))
                    .body<CashCurrency>(null)
        }

    }

    /**
     * DELETE  /cash-currencies/:id : delete the "id" cashCurrency.

     * @param id
     * * 		the id of the cashCurrency to delete
     * *
     * *
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cash-currencies/{id}", method = arrayOf(RequestMethod.DELETE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun delete(@PathVariable id: Long?): ResponseEntity<Void> {
        log.debug("REST request to delete CashCurrency : {}", id)
        try {
            cashCurrencyService.delete(id)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityDeletionAlert("cashCurrency", id!!.toString()))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCurrency", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }
}
