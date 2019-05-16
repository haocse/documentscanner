package com.haotran.documentscanner.widget.common;

/**
 * <p/>
 * Author : TRONG SON<br>
 * Create Date : 11/23/2018.<br>
 */
public class WheelViewException extends RuntimeException {
    public WheelViewException() {
        super();
    }

    public WheelViewException(String detailMessage) {
        super(detailMessage);
    }

    public WheelViewException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WheelViewException(Throwable throwable) {
        super(throwable);
    }
}
