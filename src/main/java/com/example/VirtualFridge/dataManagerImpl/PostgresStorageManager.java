package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.model.User;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostgresStorageManager {
    String databaseURL =
            "jdbc:postgres://pgd55e1b-psql-master-alias-1.data.eu01.onstackit.cloud:49925/pgd55e1b";
            //"jdbc:postgres://a9sb9cd30650ab08772fe19373da4af3b624112d57c:a9sf9fe1b1806254e4915c158f32195f3bd18a57da2@pgd55e1b-psql-master-alias-1.data.eu01.onstackit.cloud:49925/pgd55e1b";
            //"jdbc:postgresql://127.0.0.1:5432/virtualfridge";
    //"jdbc:postgresql://ec2-3-81-240-17.compute-1.amazonaws.com:5432/d9l0o5gfhlc5co";
    String username = "a9sb9cd30650ab08772fe19373da4af3b624112d57c"; //"myfgccvkkfndup";
    String password = "a9sf9fe1b1806254e4915c158f32195f3bd18a57da2"; //"c385301dd22562f1359213916017a689bfd17eb6f44db28aa5eb0ab85c34b436";

    BasicDataSource basicDataSource;

    static PostgresUserManager postgresUserManager = null;

    private PostgresStorageManager() {
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(databaseURL);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
    }

        public void createTableStorage() {

        // Be carefull: It deletes data if table already exists.
        //
        System.out.println("Starting to create new storage Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            System.out.println("getting datasource");

            String dropTable = "DROP TABLE IF EXISTS storages";
            stmt.executeUpdate(dropTable);

            System.out.println("creating storage Table");

            String createTable = "CREATE TABLE storages (" +
                    "StorageId SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "Owner int NOT NULL, " +
                    "FOREIGN KEY (Owner) REFERENCES users(id))";
            stmt.executeUpdate(createTable);
            System.out.println("storage Table created");

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        try{
            stmt.close();
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }


    }




}
