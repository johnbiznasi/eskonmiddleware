package conf.middleware.models;

public class Message {
     String senderid;
     String messageid;
     String message;
     String time;
     String status;
 String mac;
 String ounce;
    public Message(String senderid, String messageid, String message, String time,String status,String ounce,String mac) {
        this.senderid = senderid;
        this.messageid = messageid;
        this.message = message;
        this.time = time;
        this.status=status;
        this.ounce=ounce;
        this.mac=mac;
    }
    public Message(String senderid, String messageid, String message, String time,String status) {
        this.senderid = senderid;
        this.messageid = messageid;
        this.message = message;
        this.time = time;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOunce() {
        return ounce;
    }

    public void setOunce(String ounce) {
        this.ounce = ounce;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
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