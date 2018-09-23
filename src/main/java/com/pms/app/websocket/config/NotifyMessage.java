package com.pms.app.websocket.config;

public class NotifyMessage {

	private String content;

    public NotifyMessage() {
    }

    public NotifyMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
