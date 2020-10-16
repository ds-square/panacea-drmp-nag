package org.panacea.drmp.nag.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NAGException extends RuntimeException {

    protected Throwable throwable;

    public NAGException(String message) {
        super(message);
    }

    public NAGException(String message, Throwable throwable) {
        super(message);
        this.throwable = throwable;
        log.error("ReachabilityNetworkException: ", message);
    }

    public Throwable getCause() {
        return throwable;
    }
}
