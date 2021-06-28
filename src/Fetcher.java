import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Fetcher {

    public static String ISINfetcher(Security security){

    //RawUrl is for now hardcoded. A way to support multiple websites?
    String RawUrl = getURLContents("https://markets.ft.com/data/funds/tearsheet/summary?s="+security.getISIN()+":" + security.getFX());
    String PriceLocation = RawUrl.substring(RawUrl.indexOf("span class=\"mod-ui-data-list__value\">"));
    String NameLocation = RawUrl.substring(RawUrl.lastIndexOf("<title>")+7);

    //Set the name variable of the security object to be equal to the Mutual Fund name.
    NameLocation = NameLocation.substring(0,NameLocation.indexOf(","));
    security.setName(NameLocation);


    //Step through string and look for first nr, make that the start position of substring
    int i = 0;
    int startCharPos = 0;
        while (PriceLocation.length() >= i){
        char c = PriceLocation.charAt(i);

        if (Character.isDigit(c)){

            startCharPos = i;
            break;
        }

        i++;
    }
    String PriceOutput = "";
    PriceOutput = PriceLocation.substring(startCharPos,PriceLocation.indexOf("<"));


    return PriceOutput;
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
