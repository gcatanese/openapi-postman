package com.github.openapilab.openapipostman.model;

public class OpenApiSpec {
    private String url;
    private String raw;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    @Override
    public String toString() {
        return "OpenApiSpec[" +
                "url: " + this.url +
                " ,raw: " + this.raw +
                "]";
    }
}
