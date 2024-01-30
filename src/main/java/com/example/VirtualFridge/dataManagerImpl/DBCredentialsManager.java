package com.example.VirtualFridge.dataManagerImpl;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DBCredentialsManager {
    private String databaseURL = "";
    private String username = "";
    private String password = "";
    private BasicDataSource basicDataSource = new BasicDataSource();

    private static DBCredentialsManager dbCredentialsManager = null;

    private DBCredentialsManager() {
        basicDataSource = new BasicDataSource();
        this.getDBLoginData("src/main/resources/.dblogininfo");
        //basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl(databaseURL);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        //basicDataSource.setDriverClassName("org.postgresql.Driver");
    }

    private static DBCredentialsManager getDbCredentialsManager() {
        if (dbCredentialsManager == null){
            dbCredentialsManager = new DBCredentialsManager();
        }
        return dbCredentialsManager;
    }

    public static BasicDataSource getBasicDataSource() {
        return getDbCredentialsManager().basicDataSource;
    }

    private void getDBLoginData(String filename){
        //reading dbinfo from file
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            for(String line; (line = br.readLine()) != null;){
                String[] varval = line.split("=", 2);
                if(varval.length == 2){
                    String var = varval[0];
                    String val = varval[1];
                    if(var.equals("url")){
                        this.databaseURL = val;
                    }
                    else if(var.equals("user")){
                        this.username = val;
                    }
                    else if(var.equals("password")){
                        this.password = val;
                    }
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

}
