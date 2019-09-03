package com.ht.base.analysis.exception;

/**
 * @author zhengyi
 * @date 2018-12-25 18:29
 **/
public class NoInstanceException extends Exception {
    public NoInstanceException() {
        super("no event entity instance");
    }
}