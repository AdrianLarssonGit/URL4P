import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Fetcher {

    public static String fetcher(String ISIN){
    String output = getURLContents("https://markets.ft.com/data/funds/tearsheet/summary?s="+ISIN+":sek");
    output = output.substring(output.indexOf("span class=\"mod-ui-data-list__value\">"));

    //Step through string and look for first nr, make that the start position of substring
    int i = 0;
    int startCharPos = 0;
        while (output.length() >= i){
        char c = output.charAt(i);

        if (Character.isDigit(c)){

            startCharPos = i;
            break;
        }

        i++;
    }
    String finalOutput = "";
    finalOutput = output.substring(startCharPos,output.indexOf("<"));



        //System.out.println("Price of Avanza Zero " + java.time.LocalDate.now() + " is:");


    return finalOutput;
}

    private static String getURLContents(String theUrl){
        StringBuilder content = new StringBuilder();

        try{
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch (Exception e){
            System.out.println("Something broke boss!");
        }

        return content.toString();
    }
}
