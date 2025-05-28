package conf.middleware.models;



public class Temple {
    private   String name;
    private  String   id;
    private  LocationInfo location;

    public Temple(String name, String id, LocationInfo location) {
        this.name = name;
        this.id = id;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public LocationInfo getLocation() {
        return location;
    }
}
