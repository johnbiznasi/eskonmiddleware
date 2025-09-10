package conf.middleware.models;

import java.util.Date;

public class Devotee {
    private String id;
    private String   publicKey;
    private String sharedKey;
    private String  teacherPublicKey;
private String mentorId;
    private String createdOn;
    private AccountStatus  status;

    public Devotee() {
    }

    public String getMentorId() {
        return mentorId;
    }

    public void setMentorId(String mentorId) {
        this.mentorId = mentorId;
    }

    public Devotee(String id, String createdOn, AccountStatus status, String publickey) {
        this.id = id;
        this.createdOn = createdOn;
        this.status = status;
        this.publicKey=publickey;

    }
    public Devotee(String mentorId,String id, String createdOn, AccountStatus status,String publickey,String shared,
                   String teacherPublicKey) {
        this.id = id;
        this.createdOn = createdOn;
        this.status = status;
        this.publicKey=publickey;
        this.sharedKey=shared;
        this.teacherPublicKey=teacherPublicKey;
        this.mentorId=mentorId;
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    public String getTeacherPublicKey() {
        return teacherPublicKey;
    }

    public void setTeacherPublicKey(String teacherPublicKey) {
        this.teacherPublicKey = teacherPublicKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
