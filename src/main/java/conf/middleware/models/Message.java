package conf.middleware.models;

public class Message {
     int senderid;
     String messageid;
     String message;
     String time;

    public Message(int senderid, String messageid, String message, String time) {
        this.senderid = senderid;
        this.messageid = messageid;
        this.message = message;
        this.time = time;
    }

    public int getSenderid() {
        return senderid;
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}