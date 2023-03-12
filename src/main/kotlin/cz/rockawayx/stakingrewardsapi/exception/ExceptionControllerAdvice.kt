package cz.rockawayx.stakingrewardsapi.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.PrintWriter
import java.io.StringWriter

@ControllerAdvice
class ExceptionControllerAdvice {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleDefaultException(e: Exception): ResponseEntity<ErrorResponseModel> {
        return createResponseEntity(e)
    }

    private fun createResponseEntity(e: Exception): ResponseEntity<ErrorResponseModel> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        logger.error(getStackTrace(e))
        val response = ErrorResponseModel(status.value(), status.name, e.message)
        return ResponseEntity.internalServerError().body(response)
    }

    private fun getStackTrace(e: Exception): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)

        return stringWriter.toString()
    }
}

data class ErrorResponseModel(
    val code: Int,
    val status: String,
    val message: String?
)
