package com.gagnepain.cashcash.web.rest

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.domain.CashSplitCumulative
import com.gagnepain.cashcash.domain.FilterParams
import com.gagnepain.cashcash.domain.exception.BusinessException
import com.gagnepain.cashcash.service.CashSplitCumulativeService
import com.gagnepain.cashcash.web.rest.errors.CashError
import com.gagnepain.cashcash.web.rest.util.HeaderUtil
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.net.URISyntaxException
import javax.inject.Inject

/**
 * REST controller for managing CashSplitCumulative.
 */
@RestController
@RequestMapping("/api")
class CashSplitCumulativeResource {
    private val log = LoggerFactory.getLogger(CashSplitSumResource::class.java)

    @Inject
    private lateinit var cashSplitCumulativeService: CashSplitCumulativeService

    /**
     * GET  /cash-account-sums : get all the cashSplitSum.

     * @return the ResponseEntity with status 200 (OK) and the list of cashSplitSum in body
     * *
     * *
     * @throws URISyntaxException
     * * 		if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cash-split-cumulative", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @Timed
    @Throws(URISyntaxException::class)
    fun findAll(filterParams: FilterParams): ResponseEntity<List<CashSplitCumulative>> {
        log.debug("REST request to get CashSplitCumulative")

        try {
            val cashSplitCumulativeList = cashSplitCumulativeService.findAll(filterParams)
            return ResponseEntity(cashSplitCumulativeList, HttpStatus.OK)
        } catch (ex: BusinessException) {
            val cashError = CashError(ex.errorKey, ex.message)
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("cashSplitCumulative", cashError.errorKey, cashError.message))
                    .body<List<CashSplitCumulative>>(null)
        }
    }
}
