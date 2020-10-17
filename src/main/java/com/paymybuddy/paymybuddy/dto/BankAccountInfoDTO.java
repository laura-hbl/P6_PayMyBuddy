package com.paymybuddy.paymybuddy.dto;

public class BankAccountInfoDTO {

    private String email;

    private String iban;

    private String bic;

    public BankAccountInfoDTO() {
    }

    public BankAccountInfoDTO(String email, String iban, String bic) {
        this.email = email;
        this.iban = iban;
        this.bic = bic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }
}
