package com.redhat.engineering.plugins.exceptions;

/**
 * @author vdedik@redhat.com
 */
public class UserNotFoundException extends RuntimeException {
    private String userName;

    public UserNotFoundException(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
