package conf.middleware.models;

import java.util.Date;

public class Devotee {
    private String id;

    private Date createdOn;
    private AccountStatus  status;

    public Devotee() {
    }

    public Devotee(String id, Date createdOn, AccountStatus status) {
        this.id = id;
        this.createdOn = createdOn;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
