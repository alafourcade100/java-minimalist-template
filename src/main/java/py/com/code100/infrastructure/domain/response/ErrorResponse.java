package py.com.code100.infrastructure.domain.response;

public class ErrorResponse {
    public ErrorResponse(String message) {
        this.code = 1;
        this.message = message;
        this.details = message;
    }

    public ErrorResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.details = message;
    }

    public ErrorResponse(Integer code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    private Integer code;
    private String message;
    private String details;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
