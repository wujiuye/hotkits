package com.wujiuye.hotkit.util.encrypt;

/**
 * 通用解码异常
 *
 * @author wujiuye 2021/03/08
 */
public class DecoderException extends Exception {

    private static final long serialVersionUID = 1L;

    public DecoderException() {
    }

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecoderException(Throwable cause) {
        super(cause);
    }

}
