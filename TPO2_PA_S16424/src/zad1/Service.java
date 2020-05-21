/**
 *
 *  @author Podolska Anna S16424
 *
 */


package zad1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {

    private String country;
    private String currency;
    private final Map<String, String> countriesCurr = createMapOfCountries();
    private final String USER_ID = "&appid=d9f15e950ba427044d1bd82cbfed315f";
    private final String WEATHER = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final String CURR = "https://api.exchangeratesapi.io/latest?base=";
    private final String[] NBPRATES = {"http://www.nbp.pl/kursy/kursya.html",
            "http://www.nbp.pl/kursy/kursyb.html"};
    public Service(String country){
        this.country = country;
        this.currency = countriesCurr.get(country);
        //System.out.println(this.currency);
    }
    public String getCountry(){
        return country;
    }

    public String getWeather(String miasto) {
        String result = "";
        JsonObject jo = null;
        try{
            String line;
            URL url = new URL(WEATHER + miasto + "&units=metric"+ USER_ID);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(url.openStream(), "UTF-8"));
            while((line = reader.readLine()) != null){
                result += line;
            }
            reader.close();
            //System.out.println(result);
             jo = JsonParser.parseString(result).getAsJsonObject();
//            System.out.println("Pogoda " + jo.get("weather") );

        } catch (IOException ex){
            ex.printStackTrace();
        }
                return jo.get("main").toString();

    }
// Double getRateFor(String kod_waluty) - zwraca kurs waluty danego kraju wobec waluty podanej jako argument,
    public Double getRateFor(String kodWaluty){
        Double output=-1.0;
        String result = "";
        JsonObject ob = null;
        try{
            String line;
            URL url = new URL(CURR + kodWaluty);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            while((line = br.readLine()) != null){
                result += line;
            }
            br.close();
            ob = JsonParser.parseString(result).getAsJsonObject();
            String tofix = ob.get("rates").toString().replaceAll("[\\{\\}\"]", "");
            String[] tokens = tofix.split(",");
            for(String s: tokens) {
                if(s.startsWith(currency)) {
                    output = Double.parseDouble(s.split("\\:")[1]);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return output;
    }
//Double getNBPRate() - zwraca kurs złotego wobec waluty danego kraju
    public Double getNBPRate(){
        Double currencyValue = -1.0;
        int multiplyValue = 1;
        URL url = null;
        JsonObject ob = null;
        if (currency.equals("PLN")){
            return 1.0;
        }
        for(int i = 0; i < NBPRATES.length; i++){
            try {
                url = new URL(NBPRATES[i]);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),
                        "UTF-8"));

                String line ="";
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }

                Document html = Jsoup.parse(result);
                Elements trs = html.select("table.pad5 tr");
                trs.remove(0);
                for(Element tr : trs){
                    Elements tds = tr.getElementsByTag("td");
                    if(tds.get(1).text().contains(currency)){
                        multiplyValue = Integer.valueOf(tds.get(1).text().split(" ")[0]);
                        currencyValue = Double.parseDouble(tds.get(2).text().replaceAll(",", "."))/multiplyValue;
                    }
                   }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return currencyValue;
    }
    public String getCurrency(){
        return currency;
    }

    public Map<String, String> createMapOfCountries(){
        Map<String, String> resultMap = new HashMap<>();
        try{
            String line;
            File file = new File("panstwawaluty.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((line = br.readLine()) != null){
                String[] tokens = line.split(";");
                resultMap.put(tokens[0], tokens[1]);
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return resultMap;
    }


}