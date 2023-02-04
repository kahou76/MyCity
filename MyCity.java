import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCity {
    public static void main(String[] args) throws IOException {
        // Get city name from argument 
        String city = args[0];
        System.out.println("Status in " + city + ": ");
        weatherAPI(city, 1);
        String cityOfCountry = populationAPI(city, 1);
        flagAPI(cityOfCountry, 1);
        //test(1);
    }

    public static void weatherAPI(String city, long sleepTime) throws IOException{
         // Build URL for API call
        try{
            String apiKey = "aae6aab80d76545db32b0b01f4caf807";
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
            URL url = new URL(urlString);
   
            // Make API call and get JSON response
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();            

            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //retry if 500 response code
            int responseCode = connection.getResponseCode();
            if(responseCode == 500){
                System.out.println("Response Code is 500");
                System.out.println("Waiting: " + (sleepTime));
                try {
                    //waiting exponentially
                    Thread.sleep((long)Math.exp(sleepTime));
                } catch(InterruptedException e) {
                    System.out.println("got interrupted!");
                }
                weatherAPI(city, sleepTime+1);
            }

            JSONObject json = new JSONObject(response.toString());
            
            //Print current weather in city
            String currWeather = json.getJSONArray("weather").getJSONObject(0).getString("description");
            System.out.printf("Current weather: %s\n", currWeather);

            // Print current temperature in city
            double tempKelvin = json.getJSONObject("main").getDouble("temp");
            double tempFahrenheit = (tempKelvin - 273.15) * 9/5 + 32;
            System.out.printf("Current temperature: %.2f °F\n", tempFahrenheit);


        }
        catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());
        }
    }

    public static String populationAPI(String city, long sleepTime) throws IOException{
        try{
            // Build URL for API call
            String apiKey = "U8MpyyPb7XvJMHAVG30wxQ==we55e0eVfwEnTx2D";
            String urlString = "https://api.api-ninjas.com/v1/city?name=" + city ;
            URL url = new URL(urlString);

            // Make API call and get JSON response
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            //setting headers from the api instruction
            connection.setRequestProperty("X-Api-Key", apiKey);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //retry if 500 response code
            int responseCode = connection.getResponseCode();
            if(responseCode == 500){
                System.out.println("Response Code is 500");
                System.out.println("Waiting: " + (sleepTime));
                try {
                    //waiting exponentially
                    Thread.sleep((long)Math.exp(sleepTime));
                } catch(InterruptedException e) {
                    System.out.println("got interrupted!");
                }
                populationAPI(city, sleepTime+1);
            }

            JSONArray json = new JSONArray(response.toString());

            //Print population in city
            int population = json.getJSONObject(0).getInt("population");
            System.out.println("Population: " + population);

            //Get the country of the City
            String country = json.getJSONObject(0).getString("country");
            //call the flagAPI for printing the flag of the city of country
            return country;
           

        }
        catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());
            return "error";
        }
    }

    public static void flagAPI(String country, long sleepTime) throws IOException{
        String url = "https://countriesnow.space/api/v0.1/countries/flag/images";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Content-Type", "application/json");
        
        // "{\"iso2\":\"NG\"}";
        /*{
            iso2: NG
        } */
        String postJsonData = "{\"iso2\":\"" + country + "\"}";
 
        // Send post request
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(postJsonData);
        wr.flush();
        wr.close();
 
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        
        //retry if 500 response code
        int responseCode = connection.getResponseCode();
        if(responseCode == 500){
            System.out.println("Response Code is 500");
            System.out.println("Waiting: " + (sleepTime));
            try {
                //waiting exponentially
                Thread.sleep((long)Math.exp(sleepTime));
            } catch(InterruptedException e) {
                System.out.println("got interrupted!");
            }
            flagAPI(country, sleepTime+1);
        }

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        try{
        // print result
        JSONObject json = new JSONObject(response.toString());
        String flag = json.getJSONObject("data").getString("flag");
        System.out.println("The flag URL of the city of country: " + flag);
        }
        catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());
        }
    }

    //test method for retries
    // public static void test(long sleepTime) throws IOException{
    //     String urlString = "http://71.197.205.90:8080/ten?id=1";
    //     URL url = new URL(urlString);

    //     // Make API call and get JSON response
    //     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //     int responseCode = connection.getResponseCode();
    //     System.out.println(responseCode);
        
    //     if(responseCode == 500){
    //         System.out.println("Sleeping sec:" + (sleepTime));
    //         try {
    //             Thread.sleep((long)Math.exp(sleepTime));
    //         } catch(InterruptedException e) {
    //             System.out.println("got interrupted!");
    //         }
    //         sleepTime++;
    //         test(sleepTime);
    //     }
    // }
}
