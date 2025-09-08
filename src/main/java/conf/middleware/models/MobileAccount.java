package conf.middleware.models;

public class MobileAccount {
     String token;
     String profileId;

    public MobileAccount( String token, String profileId) {
        this.token=token;
        this.profileId=profileId;
    }

    public String getToken() {
        return token;
    }

    public String getProfileId() {
        return profileId;
    }
}
