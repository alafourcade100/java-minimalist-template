package py.com.code100.infrastructure.domain.response;

public class Response<T> {
    private final Boolean isSuccess;
    private T value;
    private ErrorResponse errorResponse;

    public Response(T value) {
        this.isSuccess = true;
        this.value = value;
    }

    public Response(ErrorResponse errorResponse) {
        this.isSuccess = false;
        this.errorResponse = errorResponse;
    }

    public Boolean isSuccess() {
        return this.isSuccess;
    }

    public T getValue() {
        return value;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

}
