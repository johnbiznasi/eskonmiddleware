package conf.middleware.models;

public class LocationInfo {
    double latitude;
    double longitude;
    String  town;
    String  country;
    String road;
    String region;

    public LocationInfo(double latitude, double longitude, String town, String country, String road,String region) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.town = town;
        this.country = country;
        this.road = road;
        this.region=region;
    }

    public LocationInfo() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTown() {
        return town;
    }

    public String getCountry() {
        return country;
    }

    public String getRoad() {
        return road;
    }
}
