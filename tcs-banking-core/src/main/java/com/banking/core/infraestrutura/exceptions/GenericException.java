package com.banking.core.infraestrutura.exceptions;

public class GenericException extends Throwable {

    private String code;
    private String technicalMessage;
    private String shortMessage;

    private String dataAux;


    public GenericException() {
        this.code = "000";
        this.technicalMessage = "";
        this.shortMessage = "";
        this.dataAux = "";
    }

    public GenericException(String code) {
        this.code = code;
        this.technicalMessage = "";
        this.shortMessage = "";
        this.dataAux = "";
    }

    public GenericException(String code, String technicalMessage) {
        this.code = code;
        this.technicalMessage = technicalMessage;
        this.shortMessage = "";
        this.dataAux = "";
    }

    public GenericException(String code, String technicalMessage, String shortMessage) {
        this.code = code;
        this.technicalMessage = technicalMessage;
        this.shortMessage = shortMessage;
        this.dataAux = "";
    }

    public GenericException(String code, String technicalMessage, String shortMessage, String dataAux) {
        this.code = code;
        this.technicalMessage = technicalMessage;
        this.shortMessage = shortMessage;
        this.dataAux = dataAux;
    }

    public String getCode() {
        return code;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public String getDataAux() {
        return dataAux;
    }
}
