import java.io.IOException;
import java.net.*;
import java.io.*;
import java.security.spec.ECField;
import java.util.*;
import java.time.LocalDate;
import java.io.File;
import java.util.zip.InflaterInputStream;

public class URL4P {

    //Do not want to throw IOExceptions but will do for now.
    public static void main(String[] args) throws IOException{

        //Checking if config file exist
        File userSettingsFile = new File("C:\\URL4P\\user_isin.txt");
        if(!userSettingsFile.exists()){
            System.out.println("This is the first time you run the program.");
            System.out.println("Would you like to add some ISIN numbers to track?");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String answer = reader.readLine();

            if(answer.equals("yes")){
                userSettingsFile = new File("C:\\URL4P\\user_isin.txt");
                userSettingsFile.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(userSettingsFile, true);
                System.out.println("Add ISIN numbers by typing or pasting, pressing enter and then repeat the process until done.");
                System.out.println("(Pro tip! If you have many ISIN numbers you can paste them one per line into the user settings file\n");
                System.out.println("located at C:\\URL4P\\user_isin.txt)");

                String tempISIN = "";
                ISIN_ADD:
                while (true){
                    String lastISIN = tempISIN;
                    System.out.print("Add ISIN: ");
                    tempISIN = tempISIN + reader.readLine() + "\n";
                    //Cutting off last line break to be able to compare because line break is the way to tell program to stop inputting ISIN numbers
                    String compare = tempISIN.substring(0,tempISIN.length()-1);
                    if(lastISIN.equals(compare)){
                        writer.write(tempISIN);
                        writer.flush();
                        writer.close();
                        break ISIN_ADD;
                    }
                }
            }
        }



        ArrayList<String> listOfMutualFunds = new ArrayList<>();

        try{

            Scanner scanner = new Scanner(new File("C:\\URL4P\\user_isin.txt"));
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
                Writer fileWriter = new FileWriter("C:\\URL4P\\"+entry.getKey()+".html", false);
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