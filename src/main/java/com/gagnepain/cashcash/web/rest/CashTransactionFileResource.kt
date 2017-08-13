package com.gagnepain.cashcash.web.rest

import com.codahale.metrics.annotation.Timed
import com.gagnepain.cashcash.service.fileservice.CsvFileService
import com.gagnepain.cashcash.service.fileservice.JsonFileService
import com.gagnepain.cashcash.service.fileservice.OfxFileService
import com.gagnepain.cashcash.service.fileservice.mapper.CashTransactionToImport
import com.gagnepain.cashcash.web.rest.util.HeaderUtil
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.apache.http.entity.ContentType
import org.apache.tika.exception.TikaException
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

/**
 * REST controller for managing Files.
 */
@Controller
@RequestMapping("/api/cash-transactions")
class CashTransactionFileResource {
    private val log = LoggerFactory.getLogger(CashTransactionFileResource::class.java)

    @Inject
    private lateinit var ofxFileService: OfxFileService

    @Inject
    private lateinit var jsonFileService: JsonFileService

    @Inject
    private lateinit var csvFileService: CsvFileService

    @RequestMapping(value = "/upload", method = arrayOf(RequestMethod.POST))
    @Timed
    @Throws(TikaException::class, IOException::class)
    fun handleFileUpload(@RequestParam("file") file: MultipartFile,
                         @RequestParam("accountId") accountId: Long?): ResponseEntity<List<CashTransactionToImport>> {
        log.debug("REST request to handle uploaded file : {}", file)

        if (!file.isEmpty) {
            val originalFilename = file.originalFilename
            val contentType = file.contentType
            val inputStream = file.inputStream
            try {
                val cashTransactionToImportList: List<CashTransactionToImport>
                if (StringUtils.endsWith(originalFilename, ".json") && ContentType.APPLICATION_JSON.mimeType == contentType) {
                    cashTransactionToImportList = jsonFileService.extractTransaction(inputStream)
                } else if (StringUtils.endsWith(originalFilename, ".ofx") && ContentType.APPLICATION_OCTET_STREAM.mimeType == contentType) {
                    cashTransactionToImportList = ofxFileService.extractTransaction(inputStream, accountId)
                } else if (StringUtils.endsWith(originalFilename, ".csv") && ContentType.create("text/csv")
                        .mimeType == contentType) {
                    cashTransactionToImportList = csvFileService.extractTransaction(inputStream, accountId)
                } else {
                    throw IllegalArgumentException()
                }
                return ResponseEntity.ok()
                        .body(cashTransactionToImportList)
            } catch (e: Exception) {
                return ResponseEntity.badRequest()
                        .headers(HeaderUtil.createFailureAlert("uploadCashTransaction", "Error occur when uploading " + originalFilename,
                                e.message))
                        .body<List<CashTransactionToImport>>(null)
            }

        } else {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("uploadCashTransaction", "Impossible to upload the file because it's empty.", null))
                    .body<List<CashTransactionToImport>>(null)
        }
    }

    @RequestMapping(value = "/download", method = arrayOf(RequestMethod.GET))
    @Timed
    @Throws(IOException::class)
    fun handleFileDownload(): ResponseEntity<ByteArray> {
        log.debug("REST request to download all transactions in zip file")
        if (true) {
            throw UnsupportedOperationException("This opération is not ready yet")
        }
        //creating byteArray stream, make it bufforable and passing this buffor to ZipOutputStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bufferedOutputStream = BufferedOutputStream(byteArrayOutputStream)
        val zipOutputStream = ZipOutputStream(bufferedOutputStream)

        //simple file list, just for tests
        val files = ArrayList<File>(2)
        files.add(File("README.md"))

        //packing files
        for (file in files) {
            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
            zipOutputStream.putNextEntry(ZipEntry(file.name))
            val fileInputStream = FileInputStream(file)

            IOUtils.copy(fileInputStream, zipOutputStream)

            fileInputStream.close()
            zipOutputStream.closeEntry()
        }

        zipOutputStream.finish()
        zipOutputStream.flush()
        IOUtils.closeQuietly(zipOutputStream)

        IOUtils.closeQuietly(bufferedOutputStream)
        IOUtils.closeQuietly(byteArrayOutputStream)

        val body = byteArrayOutputStream.toByteArray()
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=toto.zip")
                .contentType(MediaType("application", "zip"))
                .contentLength(body.size.toLong())
                .body(body)
    }
}
