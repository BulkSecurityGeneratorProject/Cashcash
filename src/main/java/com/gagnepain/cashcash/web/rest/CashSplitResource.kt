package com.gagnepain.cashcash.web.rest

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashSplit
import com.gagnepain.cashcash.domain.FilterParams
import com.gagnepain.cashcash.domain.exception.BusinessException
import com.gagnepain.cashcash.service.CashSplitService
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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.net.URISyntaxException
import javax.inject.Inject

/**
 * REST controller for managing CashSplit.
 */
@RestController
@RequestMapping("/api")
class CashSplitResource {
    private val log = LoggerFactory.getLogger(CashSplitResource::class.java)

    @Inject
    private lateinit var cashSplitService: CashSplitService

    /**
     * GET  /cash-splits : get all the cashSplits.

     * @param pageable
     * * 		the pagination informationCashSplit
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and the list of cashSplits in body
     * *
     * *
     * @throws URISyntaxException
     * * 		if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cash-splits", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun findAll(pageable: Pageable, filterParams: FilterParams): ResponseEntity<List<CashSplit>> {
        log.debug("REST request to get a page of CashSplits")

        val page = cashSplitService.findAll(pageable, filterParams)
        val headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cash-splits")
        return ResponseEntity(page.content, headers, HttpStatus.OK)
    }

    /**
     * GET  /cash-splits/:id : get the "id" cashSplit.

     * @param id
     * * 		the id of the cashSplit to retrieve
     * *
     * *
     * @return the ResponseEntity with status 200 (OK) and with body the cashSplit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cash-splits/{id}", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    fun findOne(@PathVariable id: Long?): ResponseEntity<CashSplit> {
        log.debug("REST request to get CashSplit : {}", id)

        try {
            val result = cashSplitService.findOne(id)
            if (result == null) {
                return ResponseEntity<CashSplit>(HttpStatus.NOT_FOUND)
            } else {
                return ResponseEntity(result, HttpStatus.OK)
            }
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashSplit", cashError.errorKey, cashError.message))
                    .body<CashSplit>(null)
        }

    }
}
