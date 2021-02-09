package com.astro.sott.usermanagment.modelClasses;

import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse;
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse;
import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse;
import com.astro.sott.usermanagment.modelClasses.login.LoginResponse;
import com.astro.sott.usermanagment.modelClasses.resetPassword.ResetPasswordResponse;
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response;

public class EvergentCommonResponse {

    private String errorMessage;
    private String errorCode;

    private boolean status;
    private SearchAccountv2Response searchAccountv2Response;

    private CreateOtpResponse createOtpResponse;
    private ConfirmOtpResponse confirmOtpResponse;

    private ResetPasswordResponse resetPasswordResponse;
    private CreateUserResponse createUserResponse;
    private LoginResponse loginResponse;

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public void setCreateUserResponse(CreateUserResponse createUserResponse) {
        this.createUserResponse = createUserResponse;
    }

    public CreateUserResponse getCreateUserResponse() {
        return createUserResponse;
    }

    public void setResetPasswordResponse(ResetPasswordResponse resetPasswordResponse) {
        this.resetPasswordResponse = resetPasswordResponse;
    }

    public ResetPasswordResponse getResetPasswordResponse() {
        return resetPasswordResponse;
    }

    public void setConfirmOtpResponse(ConfirmOtpResponse confirmOtpResponse) {
        this.confirmOtpResponse = confirmOtpResponse;
    }

    public ConfirmOtpResponse getConfirmOtpResponse() {
        return confirmOtpResponse;
    }

    public void setCreateOtpResponse(CreateOtpResponse createOtpResponse) {
        this.createOtpResponse = createOtpResponse;
    }

    public CreateOtpResponse getCreateOtpResponse() {
        return createOtpResponse;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setSearchAccountv2Response(SearchAccountv2Response searchAccountv2Response) {
        this.searchAccountv2Response = searchAccountv2Response;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public SearchAccountv2Response getSearchAccountv2Response() {
        return searchAccountv2Response;
    }
}
