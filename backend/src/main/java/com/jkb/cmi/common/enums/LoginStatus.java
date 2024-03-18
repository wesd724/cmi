package com.jkb.cmi.common.enums;

public enum LoginStatus {
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

    private String status;

    LoginStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }



}
