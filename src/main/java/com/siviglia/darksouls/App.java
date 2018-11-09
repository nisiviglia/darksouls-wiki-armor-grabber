package com.siviglia.darksouls;

import java.util.List;
import java.util.ArrayList;

public class App{

    public static void main( String[] args ){

        Storer storer = new Storer("data.db");

        for(String category : Grabber.CATEGORYS){

            System.out.println("category: " + category);
            storer.createTable(category);
            List<String> names = Grabber.listItems(category);
            for(String name : names){

                System.out.println(name);
                List< List<String>> item = Grabber.getItem(name);
                storer.insert(category, item);
            }
        }

        storer.close();
    }
}
