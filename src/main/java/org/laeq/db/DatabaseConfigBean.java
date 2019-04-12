package org.laeq.db;

import javax.annotation.Nonnull;

public class DatabaseConfigBean implements DatabaseConfigInterface{
    @Nonnull private final String url;
    @Nonnull private final String user;
    @Nonnull private final String password;

    public DatabaseConfigBean(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public String getUrl() {
        return this.url + "?serverTimezone=EST";
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
