package com.gagnepain.cashcash.web.rest

import java.net.URISyntaxException
import javax.inject.Inject

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashSplitSum
import com.gagnepain.cashcash.domain.FilterParams
import com.gagnepain.cashcash.service.CashSplitSumService

/**
 * REST controller for managing CashSplitSum.
 */
@RestController
@RequestMapping("/api")
class CashSplitSumResource {
    private val log = LoggerFactory.getLogger(CashSplitSumResource::class.java)

    @Inject
    private lateinit var cashSplitSumService: CashSplitSumService

    /**
     * GET  /cash-account-sums : get all the cashSplitSum.

     * @return the ResponseEntity with status 200 (OK) and the list of cashSplitSum in body
     * *
     * *
     * @throws URISyntaxException
     * * 		if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cash-split-sums", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun findAll(filterParams: FilterParams): ResponseEntity<List<CashSplitSum>> {
        log.debug("REST request to get CashSplitSum")
        val cashSplitSumList = cashSplitSumService.findAll(filterParams)
        return ResponseEntity(cashSplitSumList, HttpStatus.OK)
    }
}
