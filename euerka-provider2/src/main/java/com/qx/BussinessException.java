package com.qx;

/**
 * @author qiux
 * @Date 2023/7/20
 * @since
 */
public class BussinessException extends RuntimeException {
    public BussinessException(String message, Throwable cause) {
        super(message, cause, false, false);
    }
}
