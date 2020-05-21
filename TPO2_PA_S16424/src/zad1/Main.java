/**
 *
 *  @author Podolska Anna S16424
 *
 */

package zad1;


public class Main {
  public static void main(String[] args)  {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    System.out.println("Pogoda dla podanego miasta: " + weatherJson);
    Double rate1 = s.getRateFor("EUR");
    Double rate2 = s.getNBPRate();

    System.out.println("Kurs waluty podanej jako parametr - wobec waluty kraju " + s.getCountry() + " = " + rate1 + " " + s.getCurrency());
    System.out.println("Kurs złotego wobec waluty kraju o nazwie: " + s.getCountry() + " = " + rate2);

  }
}
