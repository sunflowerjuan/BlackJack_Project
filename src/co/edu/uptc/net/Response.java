package co.edu.uptc.net;

import java.io.Serializable;

public class Response implements Serializable {
    private String request;
    private String responseBody;

    public Response(String request, String responseBody) {
        this.request = request;
        this.responseBody = responseBody;
    }

    public Response() {

    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "Response [request=" + request + ", responseBody=" + responseBody + "]";
    }
}
