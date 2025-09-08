package conf.middleware.models;

public class EmailNotification {
    private String message;
    private String receiver;
    private String senttime;
    private String subject;

    public EmailNotification(String message, String receiver, String senttime, String subject) {
        this.message = message;
        this.receiver = receiver;
        this.senttime = senttime;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSenttime() {
        return senttime;
    }

    public void setSenttime(String senttime) {
        this.senttime = senttime;
    }
}
