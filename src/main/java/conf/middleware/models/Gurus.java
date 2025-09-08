package conf.middleware.models;

public class Gurus {
    private final String email;
    private final String cellphone;
    String firstName;
    String lastName;
    int  age;
    String id;
    Temple temple;
    String region;
    String  country;
String    publicKey;
boolean conversationStarted;
    public Gurus( boolean converstaionStarted,String firstName,
                 String lastName,
                 String id, String region, String country, int age, Temple temple,String publicKey,String email, String cellphone) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.id=id;
        this.temple=temple;
        this.age=age;
        this.region=region;
        this.country=country;
        this.publicKey=publicKey;
        this.email=email;
        this.cellphone=cellphone;
        this.conversationStarted =converstaionStarted;
    }

    public boolean isConversationStarted() {
        return conversationStarted;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getAge() {
        return age;
    }

    public Temple getTemple() {
        return temple;
    }

    public void setTemple(Temple temple) {
        this.temple = temple;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }
}
