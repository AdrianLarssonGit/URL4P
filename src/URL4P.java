import java.io.IOException;
import java.io.*;

import java.net.Socket;
import java.util.*;
import java.io.File;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;


public class URL4P {

    //Do not want to throw IOExceptions but will do for now.
    public static void main(String[] args) throws IOException{

        //Checking if config file exist
        File userSettingsFile = new File("C:\\URL4P\\user_isin.txt");
        if(!userSettingsFile.exists()){
            System.out.println("It looks like this is the first time you run the program.");
            System.out.println("Would you like to add some ISIN numbers to track?");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String answer = reader.readLine();

            if(answer.equals("yes")){
                userSettingsFile = new File("C:\\URL4P\\user_isin.txt");
                userSettingsFile.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(userSettingsFile, true);
                System.out.println("Add ISIN numbers by typing or pasting, pressing enter and then repeat the process until done.");
                System.out.println("(Pro tip! If you have many ISIN numbers you can paste them one per line into the user settings file");
                System.out.println("located at C:\\URL4P\\user_isin.txt)");

                String tempISIN = "";
                ISIN_ADD:
                while (true){
                    String lastISIN = tempISIN;
                    System.out.print("Add ISIN and FX with format: \"ISIN:FX\": ");
                    tempISIN = tempISIN + reader.readLine() + "\n";


                    //Cutting off last line break to be able to compare because line break is the way to tell the
                    // program to stop inputting ISIN numbers
                    String compare = tempISIN.substring(0,tempISIN.length()-1);

                    //If lastISIN string and compare string equals it means that we did not add anything on the line for
                    //ISIN numbers. Close the writer and move on to the web scraping.
                    if(lastISIN.equals(compare)){
                        writer.write(tempISIN);
                        writer.flush();
                        writer.close();
                        System.out.println("Now fetching prices. This might take a while!");
                        break ISIN_ADD;
                    }
                }
            }
        }



        HashMap<String,String> listOfMutualFunds = new HashMap<>();

        try{

            Scanner scanner = new Scanner(new File("C:\\URL4P\\user_isin.txt"));
            while(scanner.hasNext()){
                String currentFund = scanner.nextLine().toString();
                listOfMutualFunds.put(currentFund.substring(0,currentFund.indexOf(":")),currentFund.substring(currentFund.lastIndexOf(":")+1));
            }
        }
        catch (Exception e){
            System.out.println("Reading of file failed. Errorcode #1");
        }

        //Create list for storing security objects
        ArrayList<Security> objectsOfMutualFunds = new ArrayList<>();

        int i = 0;
        for(Map.Entry<String, String> entry : listOfMutualFunds.entrySet()){
            Security security = new Security();
            security.setISIN(entry.getKey());
            security.setFX(entry.getValue());
            security.setName(entry.getKey());

            objectsOfMutualFunds.add(security);
            i++;
        }

        //Create new file per security in list
        try{
            for (int j = 0; j < objectsOfMutualFunds.size(); j++) {
                String price = Fetcher.ISINfetcher(objectsOfMutualFunds.get(j));
                String fx = objectsOfMutualFunds.get(j).getFX();
                String SecurityName = objectsOfMutualFunds.get(j).getName();

                //Portfolio Performance do not accept space in files.
                SecurityName = SecurityName.replace(" ", "_");

                Writer fileWriter = new FileWriter("C:\\URL4P\\"+SecurityName+".html", false);


                fileWriter.write(
                        "<h3>Ticker name: "+SecurityName + "</h3>\n" +

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
                                "<td>"+price+"</td>\n" +
                                "</tr>\n" +
                                "</tbody>\n" +
                                "</table>"
                );
                fileWriter.close();
            }


        }
        catch (Exception e){

        }
        System.out.println("Spinning up local server, http://localhost:8000");

        //Calling the python script for running webserver
        Runtime rt = Runtime.getRuntime();
        rt.exec("cmd.exe /c start py pyserver.py", null,new File("C:\\URL4P\\"));




        System.out.println("Done! Now import them in Portfolio Performance!");


}}
