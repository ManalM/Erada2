package com.example.android.erada2;

public class User {
    private String name;
    private String address;
    private String description;
    private String phone;
    private String location;
    private String email;
    private String website;
    private String mImageResourceId;
    private Double lag;
    private Double lat;

    public User(){

    }

    public User(String name , String address, String description,String phone,String location,String email,String website, String mImageResourceId ,Double lag,Double lat){
        name=name;
        address=address;
        description=description;
        website=website;
        location=location;
        phone=phone;
        email=email;
        mImageResourceId = mImageResourceId;
        lat =lat;
        lag=lag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLag() {
        return lag;
    }

    public void setLag(Double lag) {
        this.lag = lag;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getmImageResourceId() {
        return mImageResourceId;
    }

    public void setmImageResourceId(String mImageResourceId) {
        this.mImageResourceId = mImageResourceId;
    }


}
