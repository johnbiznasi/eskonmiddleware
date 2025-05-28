package conf.middleware.models;

public class Gurus {
    String firstName;
    String lastName;
    int  age;
    String id;
    Temple temple;
    String region;
    String  country;

    public Gurus(String firstName,
                 String lastName,
                 String id, String region, String country, int age, Temple temple) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.id=id;
        this.temple=temple;
        this.age=age;
        this.region=region;
        this.country=country;
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
