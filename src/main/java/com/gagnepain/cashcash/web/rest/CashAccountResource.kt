package com.gagnepain.cashcash.web.rest

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashAccount
import com.gagnepain.cashcash.domain.exception.BusinessException
import com.gagnepain.cashcash.service.CashAccountService
import com.gagnepain.cashcash.web.rest.errors.CashError
import com.gagnepain.cashcash.web.rest.util.HeaderUtil
import org.slf4j.LoggerFactory
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
 * REST controller for managing CashAccount.
 */
@RestController
@RequestMapping("/api")
class CashAccountResource {
    private val log = LoggerFactory.getLogger(CashAccountResource::class.java)

    @Inject
    private lateinit var cashAccountService: CashAccountService

    /**
     * POST  /cash-accounts : Create a new cashAccount.

     * @param cashAccountList
     * * 		the cashAccounts to create
     * *
     * *
     * @return the ResponseEntity with status 201 (Created) and with body the new cashAccount, or with status 400 (Bad Request) if the cashAccount has already an ID
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-accounts", method = arrayOf(RequestMethod.POST), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun create(@Valid @RequestBody cashAccountList: List<CashAccount>): ResponseEntity<Void> {
        log.debug("REST request to create CashAccountList : {}", cashAccountList)

        try {
            cashAccountService.create(cashAccountList)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityListUpdateAlert("cashAccount", null))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashAccount", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }

    /**
     * PUT  /cash-accounts : Updates an existing cashAccount.

     * @param cashAccountList
     * * 		the list of cashAccounts to update
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the updated cashAccount,
     * * or with status 400 (Bad Request) if the cashAccount is not valid,
     * * or with status 500 (Internal Server Error) if the cashAccount couldnt be updated
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-accounts", method = arrayOf(RequestMethod.PUT), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun update(@Valid @RequestBody cashAccountList: List<CashAccount>): ResponseEntity<Void> {
        log.debug("REST request to update CashAccountList : {}", cashAccountList)

        try {
            cashAccountService.update(cashAccountList)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityListUpdateAlert("cashAccount", null))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashAccount", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }

    /**
     * GET  /cash-accounts : get all the cashAccounts.

     * @return the ResponseEntity with status 200 (OK) and the list of cashAccounts in body
     * *
     * *
     * @throws URISyntaxException
     * * 		if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cash-accounts", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun findAll(): ResponseEntity<List<CashAccount>> {
        log.debug("REST request to get a page of CashAccounts")
        var result = cashAccountService.findAll()

        if (result.isEmpty()) {
            // First time coming here, we create the root account
            try {
                result = cashAccountService.createInitialCashAccount()
            } catch (ex: BusinessException) {
                val cashError = CashError(ex.errorKey, ex.message)
                return ResponseEntity.badRequest()
                        .headers(HeaderUtil.createFailureAlert("cashAccountForNewUser", cashError.errorKey, cashError.message))
                        .body<List<CashAccount>>(null)
            }

        }

        return ResponseEntity(result, HttpStatus.OK)
    }

    /**
     * GET  /cash-accounts/:id : get the "id" cashAccount.

     * @param id
     * * 		the id of the cashAccount to retrieve
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the cashAccount, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cash-accounts/{id}", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun findOne(@PathVariable id: Long): ResponseEntity<CashAccount> {
        log.debug("REST request to get CashAccount : {}", id)

        try {
            val result = cashAccountService.findOne(id)
            if (result == null) {
                return ResponseEntity<CashAccount>(HttpStatus.NOT_FOUND)
            } else {
                return ResponseEntity(result, HttpStatus.OK)
            }
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashAccount", cashError.errorKey, cashError.message))
                    .body<CashAccount>(null)
        }

    }

    /**
     * DELETE  /cash-accounts/:id : delete the "id" cashAccount.

     * @param id
     * * 		the id of the cashAccount to delete
     * *
     * *
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cash-accounts/{id}", method = arrayOf(RequestMethod.DELETE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun delete(@PathVariable id: Long?): ResponseEntity<Void> {
        log.debug("REST request to delete CashAccount : {}", id)
        try {
            cashAccountService.delete(id)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityDeletionAlert("cashAccount", id!!.toString()))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashAccount", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }
}
