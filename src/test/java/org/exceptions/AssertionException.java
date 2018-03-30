package org.exceptions;


/**
 * Throwed when an assertion does not match
 */
public class AssertionException extends Exception {

    public AssertionException(String assertionMessage) {
        super(assertionMessage);
    }

}