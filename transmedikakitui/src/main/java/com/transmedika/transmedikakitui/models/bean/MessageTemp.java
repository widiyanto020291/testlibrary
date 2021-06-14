package com.transmedika.transmedikakitui.models.bean;

public class MessageTemp {
    private String msgId;
    private Long konsultasiId;
    private String url;

    public MessageTemp(String msgId, Long konsultasiId, String url) {
        this.msgId = msgId;
        this.konsultasiId = konsultasiId;
        this.url = url;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Long getKonsultasiId() {
        return konsultasiId;
    }

    public void setKonsultasiId(Long konsultasiId) {
        this.konsultasiId = konsultasiId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
