package conf.middleware.models.dtos;

import org.jboss.resteasy.reactive.RestForm;

public class GuruDTO {
    public int id;
    @RestForm
    public String firstName;
    @RestForm
    public String lastName;
    @RestForm
    public String status;
    @RestForm
    public String region;
    @RestForm
    public String temple;
    @RestForm
    public Integer age;
    @RestForm
    public String country;

    public GuruDTO() {

    }

    public GuruDTO(int id, String firstName, String lastName, String status, String region, String temple, Integer age, String country) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.region = region;
        this.temple = temple;
        this.age = age;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTemple() {
        return temple;
    }

    public void setTemple(String temple) {
        this.temple = temple;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
// constructor, getters, setters (optional for record-like use)
}