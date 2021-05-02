import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException, URISyntaxException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        //chnage district id and date as required
        Request request = new Request.Builder()
                .url("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=294&date=02-05-2021")
                .method("GET", null)
                .addHeader("accept", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String reponseString = response.body().string();
        //System.out.println(reponseString);
        Object jo = new JSONParser().parse(reponseString);
        JSONObject obj = (JSONObject) jo;
        JSONArray centersobj = (JSONArray) obj.get("centers");
        // obj.get("centers").get(0).get("sessions").get(0).get("min_age_limit");
        String errors = "";
        boolean isVaccineAvailable=false;
        while (true) {

            for (int i = 0; i < centersobj.size(); i++) {
                JSONObject centerobj = (JSONObject) centersobj.get(i);
                JSONArray sessionsobj = (JSONArray) centerobj.get("sessions");
                for (int j = 0; j < sessionsobj.size(); j++) {
                    JSONObject sessionobj = (JSONObject) sessionsobj.get(j);
                    long agelimit = (long) sessionobj.get("min_age_limit");
                    String capString = String.valueOf(sessionobj.get("available_capacity"));
                    Double cap = 0d;
                    try {
                        cap = Double.parseDouble(capString);
                    } catch (Exception e) {
                        errors +=
                                "start of error\n" +
                                        centerobj.get("name") + "Available Capacity : " + capString + "\n" +
                                        e.getMessage() + "\n" +
                                        "end of error\n";
                    }
                    if (agelimit == 18)
                        System.out.println(centerobj.get("name") + " Available Capacity : " + capString +" Vaccine type : "+sessionobj.get("vaccine"));
                    if (agelimit == 18 && cap > 0)
                    {
                        System.out.println("Available at: " + centerobj.get("name") + "Available Capacity : " + capString);
                        isVaccineAvailable=true;
                    }

                }

            }
            System.out.println("***********ERRORS************\n" + errors);
            if(isVaccineAvailable)
            {
                System.out.println("Making Phone Calls");
                //add number to call
                PhoneCalltw.makephoneCall("+919999999999");
            }
            Thread.sleep(50000);

        }
    }
}
