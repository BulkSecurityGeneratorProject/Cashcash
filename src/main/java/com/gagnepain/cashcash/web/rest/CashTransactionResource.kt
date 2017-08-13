package com.gagnepain.cashcash.web.rest

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashTransaction
import com.gagnepain.cashcash.domain.FilterParams
import com.gagnepain.cashcash.domain.exception.BusinessException
import com.gagnepain.cashcash.service.CashTransactionService
import com.gagnepain.cashcash.web.rest.errors.CashError
import com.gagnepain.cashcash.web.rest.util.HeaderUtil
import com.gagnepain.cashcash.web.rest.util.PaginationUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.net.URISyntaxException
import javax.inject.Inject
import javax.validation.Valid

/**
 * REST controller for managing CashTransaction.
 */
@RestController
@RequestMapping("/api")
class CashTransactionResource {
    private val log = LoggerFactory.getLogger(CashTransactionResource::class.java)

    @Inject
    private lateinit var cashTransactionService: CashTransactionService

    @Value("\${cashcash.maxPageSize}")
    private var MAX_PAGE_SIZE: Int = 0

    /**
     * POST  /cash-transactions : Create a new cashTransaction.

     * @param cashTransactionList
     * * 		the cashTransactionList to create
     * *
     * *
     * @return the ResponseEntity with status 201 (Created) and with body the new cashTransaction, or with status 400 (Bad Request) if the cashTransaction has already an ID
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-transactions", method = arrayOf(RequestMethod.POST), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun create(@Valid @RequestBody cashTransactionList: List<CashTransaction>): ResponseEntity<Void> {
        log.debug("REST request to create CashTransactionList : {}", cashTransactionList)
        try {
            cashTransactionService.create(cashTransactionList)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityListCreationAlert("cashTransaction", null))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashTransaction", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }

    /**
     * PUT  /cash-transactions : Updates an existing cashTransaction.

     * @param cashTransaction
     * * 		the cashTransaction to update
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the updated cashTransaction,
     * * or with status 400 (Bad Request) if the cashTransaction is not valid,
     * * or with status 500 (Internal Server Error) if the cashTransaction couldnt be updated
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-transactions", method = arrayOf(RequestMethod.PUT), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun update(@Valid @RequestBody cashTransaction: CashTransaction): ResponseEntity<Void> {
        log.debug("REST request to update cashTransaction : {}", cashTransaction)

        try {
            val result = cashTransactionService.update(cashTransaction)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("cashTransaction", result.id!!
                            .toString()))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashTransaction", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }

    /**
     * GET  /cash-transactions : get all the cashTransactions.

     * @param pageable
     * * 		the pagination information
     * *
     * @param filterParams
     * * 		the filter information
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and the list of cashTransactions in body
     * *
     * *
     * @throws URISyntaxException
     * * 		if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cash-transactions", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun findAll(pageable: Pageable, filterParams: FilterParams): ResponseEntity<List<CashTransaction>> {
        log.debug("REST request to get a page of CashTransactions")

        try {
            val page = cashTransactionService.findAll(pageable, filterParams)
            val headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cash-transactions")
            return ResponseEntity(page.content, headers, HttpStatus.OK)
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashTransaction", cashError.errorKey, cashError.message))
                    .body<List<CashTransaction>>(null)
        }

    }

    /**
     * GET  /cash-transactions/:id : get the "id" cashTransaction.

     * @param id
     * * 		the id of the cashTransaction to retrieve
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the cashTransaction, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cash-transactions/{id}", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun findOne(@PathVariable id: Long?): ResponseEntity<CashTransaction> {
        log.debug("REST request to get CashTransaction : {}", id)

        try {
            val result = cashTransactionService.findOne(id)
            if (result == null) {
                return ResponseEntity<CashTransaction>(HttpStatus.NOT_FOUND)
            } else {
                return ResponseEntity(result, HttpStatus.OK)
            }
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashTransaction", cashError.errorKey, cashError.message))
                    .body<CashTransaction>(null)
        }

    }

    /**
     * DELETE  /cash-transactions/:id : delete the "id" cashTransaction.

     * @param id
     * * 		the id of the cashTransaction to delete
     * *
     * *
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cash-transactions/{id}", method = arrayOf(RequestMethod.DELETE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun delete(@PathVariable id: Long?): ResponseEntity<Void> {
        log.debug("REST request to delete CashTransaction : {}", id)
        try {
            cashTransactionService.delete(id)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityDeletionAlert("cashTransaction", id!!.toString()))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashTransaction", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }
}
