package com.huce.hma.presentation.guest.forgotpassword.dto;

public class ForgotPasswordResponseDTO {
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
