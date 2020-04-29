package com.delta.kylintask.exception;

import com.delta.kylintask.dto.KylinError;

public class KylinException extends RuntimeException {
    private static final long serialVersionUID = -8102677845935010551L;

    public KylinException() {
        super();
    }

    public KylinException(String message) {
        super(message);
    }

    public KylinException(KylinError kylinError) {
        super(String.format("Kylin  exception: %s", kylinError.toString()));
    }
}

