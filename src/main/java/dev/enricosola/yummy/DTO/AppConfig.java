package dev.enricosola.yummy.DTO;

public class AppConfig {
    private final String adminUsername;
    private final String adminPassword;
    private final String storage;

    public AppConfig(String storage, String adminUsername, String adminPassword){
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.storage = storage;
    }

    public String getAdminUsername(){
        return this.adminUsername;
    }

    public String getAdminPassword(){
        return this.adminPassword;
    }

    public String getStorage(){
        return this.storage;
    }
}
