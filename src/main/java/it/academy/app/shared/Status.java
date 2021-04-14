package it.academy.app.shared;

public enum Status {
    INPROGRESS ("PERŽIŪRIMA"),
    ACCEPTED ("PATVIRTINTA"),
    DECLINED ("ATMESTA");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatusInLithuanian() {
        return this.status;
    }
}
