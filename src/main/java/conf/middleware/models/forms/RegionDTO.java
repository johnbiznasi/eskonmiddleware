package conf.middleware.models.forms;

import org.jboss.resteasy.reactive.RestForm;

public class RegionDTO {
    @RestForm
    public int id;
    @RestForm
    public String name;
    @RestForm
    public String country;


    public RegionDTO(int id, String county,String name) {
        this.id = id;
        this.country=county;
        this.name =name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}

