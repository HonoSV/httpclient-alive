package cn.th.alive.utils;

import org.apache.http.Header;

import java.io.Serializable;

public class HttpClientResult implements Serializable {
    private int code;
    private Header[] header;
    private String content;

    public HttpClientResult (int code) {
        this.code = code;
    }

    public HttpClientResult (int code, Header[] header, String content) {
        this.code = code;
        this.header = header;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Header[] getHeader() {
        return header;
    }

    public void setHeader(Header[] header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
