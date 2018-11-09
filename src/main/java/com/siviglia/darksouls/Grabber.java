package com.siviglia.darksouls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Grabber{

    private final static String BASE_URL = "https://darksouls.wiki.fextralife.com/";
    private final static String CATEGORY_TABLE_NAME = "wiki_table sortable";
    private final static String ITEM_TABLE_NAME = "wiki_table";

    public final static String[] CATEGORYS = 
                            {"Helms", "Chest Armor", "Gauntlets", "Leg Armor"};

    private Grabber(){}

    public static List<String> listItems(String page){

        List<String> items = new ArrayList<>();

        Document base_doc = getItemDoc(page);
        List< List < List<String> > > base_table = 
            grabTablesFromDoc(base_doc, CATEGORY_TABLE_NAME);

        for(List<String> row : base_table.get(0)){
            items.add(row.get(0));
        }

        return items;
    }

    public static List < List<String> > getItem(String item){
        
        Document doc = getItemDoc(item);

        List< List < List<String> > > tables = null;
        tables = grabTablesFromDoc(doc, ITEM_TABLE_NAME);

        List< List<String>> formattedTable = null;
        formattedTable = formatTable(item, tables);

        return formattedTable;
    }

    private static List< List<String>> formatTable(String itemName, List< List< List<String>>> tables){
    
        List< List<String> > formattedTable = new ArrayList<>();
        if(tables.size() < 3){

            formattedTable.add( formatInfoTable(itemName, tables.get(0)) );

        }else{
            
            for(List<String> row : tables.get(1)){
                formattedTable.add( formatUpgradeTable(itemName, 
                    tables.get(0).get(4).get(1), tables.get(0).get(5).get(1), row));
            }
        }

        return formattedTable;
    }

    private static List<String> formatInfoTable(String itemName, List< List<String> > infoTable){

        List<String> outputTable = new ArrayList<>(); 
        try{
        
            outputTable.add(itemName);
            outputTable.add("0");
            outputTable.add( infoTable.get(0).get(0));
            outputTable.add( infoTable.get(1).get(0));
            outputTable.add( infoTable.get(2).get(0));
            outputTable.add( infoTable.get(3).get(0));
            outputTable.add( infoTable.get(4).get(0));
            outputTable.add( infoTable.get(5).get(0));
            outputTable.add( infoTable.get(6).get(0));
            outputTable.add( infoTable.get(1).get(1));
            outputTable.add( infoTable.get(2).get(1));
            outputTable.add( infoTable.get(3).get(1));
            outputTable.add( infoTable.get(0).get(1));
            outputTable.add( infoTable.get(4).get(1));
            outputTable.add( infoTable.get(5).get(1));
        } 
        catch(IndexOutOfBoundsException ex){

            System.out.println(infoTable);
            System.out.println(ex);
            System.out.println(outputTable);
            throw ex;
        }

        return outputTable;
    }

    private static List<String> formatUpgradeTable(String itemName, String dur, String weight, List<String> updateTable){
    
        List<String> outputTable = new ArrayList<>(); 
        try{

            outputTable.add(itemName);
            outputTable.add( updateTable.get(0).replace("+", " ").trim());
            outputTable.add( updateTable.get(1));
            outputTable.add( updateTable.get(2));
            outputTable.add( updateTable.get(3));
            outputTable.add( updateTable.get(4));
            outputTable.add( updateTable.get(5));
            outputTable.add( updateTable.get(6));
            outputTable.add( updateTable.get(7));
            outputTable.add( updateTable.get(8));
            outputTable.add( updateTable.get(9));
            outputTable.add( updateTable.get(10));
            outputTable.add( updateTable.get(11));
            outputTable.add(dur);
            outputTable.add(weight);
        }
        catch(IndexOutOfBoundsException ex){

            System.out.println(updateTable);
            System.out.println(ex);
            System.out.println(outputTable);
            throw ex;
        }
    
        return outputTable;
    }

   private static Document getItemDoc(String item){

        String url = BASE_URL + item.replace(" ", "+");

        Document doc = grabUrl(url);

        return doc;
    }

    //Grabs the page of the supplied url.
    private static Document grabUrl(String url){

        Document doc = null;
        try{
            doc = Jsoup.connect(url).get();
        }
        catch(IOException ex){
            System.out.println(ex); 
            System.exit(-1);
        }

        return doc;
    }

    //This will grab all the tables from the page.
    private static List< List < List<String> > > grabTablesFromDoc(Document doc, String tableName){

        List< List< List<String> > > bucket = new ArrayList<>();
        Elements tables = doc.getElementsByClass(tableName);
        for(Element table : tables){
            
            List< List<String> > items = new ArrayList<>();
            Elements rows = table.getElementsByTag("tr");
            for(Element row : rows){

                List<String> item = new ArrayList<>(); 
                Elements columns = row.getElementsByTag("td"); 
                for(Element column : columns){
                    
                    String text = column.text()
                        .replaceAll("[/]", "")
                        .replace(",", ".")
                        .trim();
                    if(text.length() > 0 && !text.endsWith("-")){
                        item.add(text);
                    }
                    else if( !column.hasAttr("colspan") || text.endsWith("-") ){
                        item.add("0");
                    } 
                }
                
                if(item.size() > 0){
                    items.add(item);
                }
            }

            if(items.size() > 0){
                bucket.add(items);
            }
        }

        return bucket;
    }

}
