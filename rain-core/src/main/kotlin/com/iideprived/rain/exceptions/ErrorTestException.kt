package com.iideprived.rain.exceptions

internal class ErrorTestException : RuntimeException("Error Exception"), ErrorCodeException {
    override val errorCode: String
        get() = "TEST_ERROR"
}

internal class ErrorStatusTestException : RuntimeException("Full Exception"), ErrorCodeException, StatusCodeException {
    override val errorCode: String
        get() = "TEST_ERROR_STATUS"
    override val statusCode: Int
        get() = 500
}

internal class StatusTestException : RuntimeException("Status Exception"), ErrorCodeException, StatusCodeException {
    override val errorCode: String
        get() = "STATUS_ERROR"
    override val statusCode: Int
        get() = 403
}

internal class GenericException : RuntimeException("Generic Exception")