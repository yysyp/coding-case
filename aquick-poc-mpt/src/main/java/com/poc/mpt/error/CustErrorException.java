package com.poc.mpt.error;





@ExceptionMapping(code = "Cust Error", httpStatus = 400, msg = "Poor network quality!")
public class CustErrorException extends RuntimeException {

    public CustErrorException() {
        super();
    }

    public CustErrorException(String message) {
        super(message);
    }
}
