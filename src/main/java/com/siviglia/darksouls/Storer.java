package com.siviglia.darksouls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

class Storer{

    private Connection connection = null;
    private Statement statement = null;

    public Storer(String filePath){
        
        try{

            Class.forName("org.sqlite.JDBC"); 
        }
        catch(ClassNotFoundException ex){

            System.out.println(ex);
            System.exit(-1);
        }

        if(Paths.get(filePath).toFile().exists()){
            Paths.get(filePath).toFile().delete();
        }

        try{

            this.connection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            this.statement = this.connection.createStatement();
        }
        catch(SQLException ex){

            System.out.println(ex);
            System.exit(-1);
        }
    }

    public void createTable(String tableName){

        tableName = tableName.replace(" ", "+");
        String sql = null;
        try{

            sql = String.format("create table \"%s\" ("
                +"ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"NAME CHAR(255) NOT NULL,"
                +"LEVEL INTEGER NOT NULL,"
                +"PHYSICAL REAL,"
                +"STRIKE REAL,"
                +"SLASH REAL,"
                +"THRUST REAL,"
                +"MAGIC REAL,"
                +"FIRE REAL,"
                +"LIGHTNING REAL,"
                +"BLEED REAL,"
                +"POISON REAL,"
                +"CURSE REAL,"
                +"POISE REAL,"
                +"DURABILITY REAL,"
                +"WEIGHT REAL"
                +")"
                , tableName
            );

            this.statement.executeUpdate(sql);
        }
        catch(SQLException ex){

            System.out.println(sql);
            System.out.println(ex);
            this.close();
            System.exit(-1);
        }
    
    }

    public void insert(String tableName, List< List<String>> item){
        
        tableName = tableName.replace(" ", "+");
        for(List<String> row : item){

            String sql = null;

            try{
                sql = String.format("INSERT INTO \"%s\" (NAME, LEVEL," 
                    +"PHYSICAL, STRIKE, SLASH, THRUST, MAGIC, FIRE,"
                    +"LIGHTNING, BLEED, POISON, CURSE, POISE, DURABILITY," 
                    +"WEIGHT) VALUES ("
                    +"\"%s\", %s, %s,"
                    +"%s, %s, %s,"
                    +"%s, %s, %s,"
                    +"%s, %s, %s,"
                    +"%s, %s, %s)"
                    , tableName, row.get(0), row.get(1), row.get(2)
                    , row.get(3), row.get(4), row.get(5), row.get(6)
                    , row.get(7), row.get(8), row.get(9), row.get(10)
                    , row.get(11), row.get(12), row.get(13), row.get(14)
                );
                this.statement.executeUpdate(sql);
            }
            catch(SQLException ex){

                System.out.println(sql);
                System.out.println(ex);
                this.close();
                System.exit(-1);
            }
        }
    }

    public void close(){
        
        try{
            if(connection != null){
                connection.close();
            }
        }
        catch(SQLException ex){
            System.out.println(ex); 
        }
    }
}
