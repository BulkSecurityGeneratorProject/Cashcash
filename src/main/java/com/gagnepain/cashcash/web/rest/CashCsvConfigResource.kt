package com.gagnepain.cashcash.web.rest

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashCsvConfig
import com.gagnepain.cashcash.domain.exception.BusinessException
import com.gagnepain.cashcash.service.CashCsvConfigService
import com.gagnepain.cashcash.web.rest.errors.CashError
import com.gagnepain.cashcash.web.rest.util.HeaderUtil
import com.gagnepain.cashcash.web.rest.util.PaginationUtil
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.URISyntaxException
import javax.inject.Inject
import javax.validation.Valid

/**
 * REST controller for managing CashCsvConfig.
 */
@RestController
@RequestMapping("/api")
class CashCsvConfigResource {
    private val log = LoggerFactory.getLogger(CashCsvConfigResource::class.java)

    @Inject
    private lateinit var cashCsvConfigService: CashCsvConfigService

    /**
     * POST  /cash-csv-configs : Create a new cashCsvConfig.

     * @param cashCsvConfig
     * * 		the cashCsvConfig to create
     * *
     * *
     * @return the ResponseEntity with status 201 (Created) and with body the new cashCsvConfig, or with status 400 (Bad Request) if the cashCsvConfig has already an ID
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-csv-configs", method = arrayOf(RequestMethod.POST), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun create(@Valid @RequestBody cashCsvConfig: CashCsvConfig): ResponseEntity<CashCsvConfig> {
        log.debug("REST request to create cashCsvConfig : {}", cashCsvConfig)
        if (cashCsvConfig.id != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCsvConfig", "idexists", "A new cashCsvConfig cannot already have an ID"))
                    .body<CashCsvConfig>(null)
        }

        try {
            val result = cashCsvConfigService.create(cashCsvConfig)
            return ResponseEntity.created(URI("/api/cash-csv-configs/" + result.id!!))
                    .headers(HeaderUtil.createEntityCreationAlert("cashCsvConfig", result.id!!
                            .toString()))
                    .body(result)
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCsvConfig", cashError.errorKey, cashError.message))
                    .body<CashCsvConfig>(null)
        }

    }

    /**
     * PUT  /cash-csv-configs : Updates an existing cashCsvConfig.

     * @param cashCsvConfig
     * * 		the cashCsvConfig to update
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the updated cashCsvConfig,
     * * or with status 400 (Bad Request) if the cashCsvConfig is not valid,
     * * or with status 500 (Internal Server Error) if the cashCsvConfig couldnt be updated
     * *
     * *
     * @throws URISyntaxException
     * * 		if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cash-csv-configs", method = arrayOf(RequestMethod.PUT), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun update(@Valid @RequestBody cashCsvConfig: CashCsvConfig): ResponseEntity<CashCsvConfig> {
        log.debug("REST request to update CashCsvConfig : {}", cashCsvConfig)
        if (cashCsvConfig.id == null) {
            return create(cashCsvConfig)
        }

        try {
            val result = cashCsvConfigService.update(cashCsvConfig)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("cashCsvConfig", cashCsvConfig.id!!
                            .toString()))
                    .body(result)
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCsvConfig", cashError.errorKey, cashError.message))
                    .body<CashCsvConfig>(null)
        }

    }

    /**
     * GET  /cash-csv-configs : get all the cashCsvConfigs.

     * @return the ResponseEntity with status 200 (OK) and the list of cashCsvConfigs in body
     */
    @RequestMapping(value = "/cash-csv-configs", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun findAll(pageable: Pageable): ResponseEntity<List<CashCsvConfig>> {
        log.debug("REST request to get all CashCsvConfigs")
        val page = cashCsvConfigService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cash-csv-configs")
        return ResponseEntity(page.content, headers, HttpStatus.OK)
    }

    /**
     * GET  /cash-csv-configs/:id : get the "id" cashCsvConfig.

     * @param id
     * * 		the id of the cashCsvConfig to retrieve
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the cashCsvConfig, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cash-csv-configs/{id}", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun findOne(@PathVariable id: Long?): ResponseEntity<CashCsvConfig> {
        log.debug("REST request to get CashCsvConfig : {}", id)
        try {
            val result = cashCsvConfigService.findOne(id)
            if (result == null) {
                return ResponseEntity<CashCsvConfig>(HttpStatus.NOT_FOUND)
            } else {
                return ResponseEntity(result, HttpStatus.OK)
            }
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCsvConfig", cashError.errorKey, cashError.message))
                    .body<CashCsvConfig>(null)
        }

    }

    /**
     * DELETE  /cash-csv-configs/:id : delete the "id" cashCsvConfig.

     * @param id
     * * 		the id of the cashCsvConfig to delete
     * *
     * *
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cash-csv-configs/{id}", method = arrayOf(RequestMethod.DELETE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun delete(@PathVariable id: Long?): ResponseEntity<Void> {
        log.debug("REST request to delete cashCsvConfig : {}", id)
        try {
            cashCsvConfigService.delete(id)
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityDeletionAlert("cashCsvConfig", id!!.toString()))
                    .build<Void>()
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashCsvConfig", cashError.errorKey, cashError.message))
                    .body<Void>(null)
        }

    }
}
