package conf.middleware.models.forms;

import org.jboss.resteasy.reactive.RestForm;

public class TempleForm {
    @RestForm
    private String  name;
    @RestForm
    private String  region;
    @RestForm
    private String  location;

    public TempleForm() {
    }

    public TempleForm(String name, String region, String location) {
        this.name = name;
        this.region = region;
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
