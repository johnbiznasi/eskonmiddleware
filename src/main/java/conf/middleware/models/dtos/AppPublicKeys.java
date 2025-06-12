package conf.middleware.models.dtos;

import org.jboss.resteasy.reactive.RestForm;

public class AppPublicKeys {
    @RestForm
    private   String  key;
    @RestForm
    private   int   account;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
}
