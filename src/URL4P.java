import java.io.IOException;
import java.net.*;
import java.io.*;
import java.security.spec.ECField;
import java.util.*;
import java.time.LocalDate;
import java.io.File;

public class URL4P {

    public static void main(String[] args){

        //Checking if config file exist
        File userSettingsFile = new File("C:\\Program Files (x86)\\URL4P\\user_settings.txt");
        if(!userSettingsFile.exists()){
            System.out.println("This is the first time you run the program");
        }
        ArrayList<String> listOfMutualFunds = new ArrayList<>();

        try{

            Scanner scanner = new Scanner(new File("D:\\portfolio\\mutualfundsSE\\fulllist.txt"));
            while(scanner.hasNext()){
                listOfMutualFunds.add(scanner.nextLine());
            }
        }
        catch (Exception e){
            System.out.println("Reading of file failed. Errorcode #1");
        }

        //Create map for storing mutual funds and their latest price
        HashMap<String, String> latestMutualFundPrices = new HashMap<>();

        int i = 0;
        while(listOfMutualFunds.size() > i){
            latestMutualFundPrices.put(listOfMutualFunds.get(i),Fetcher.fetcher(listOfMutualFunds.get(i)));
            i++;
        }

        //Create new file per key in hashmap
        try{
            for(Map.Entry<String, String> entry : latestMutualFundPrices.entrySet()){
                Writer fileWriter = new FileWriter("D:\\portfolio\\mutualfundsSE\\"+entry.getKey()+".html", false);
                fileWriter.write(
                        "<h3>ISIN: "+entry.getKey() + "</h3>\n" +
                                ""+"<table class=\"pure-table pure-table-striped\">\n" +
                                "<thead>\n" +
                                "<tr>\n" +
                                "<th>Date</th>\n" +
                                "<th>Price</th>\n" +
                                "</tr>\n" +
                                "</thead>" +
                                "\n\n<tbody>" +
                                "\n<tr>\n" +
                                "<td>"+java.time.LocalDate.now()+"</td>\n" +
                                "<td>"+entry.getValue()+"</td>\n" +
                                "</tr>\n" +
                                "</tbody>\n" +
                                "</table>"
                );
                fileWriter.close();
            }

        }
        catch (Exception e){

        }
        System.out.println(latestMutualFundPrices);


}}