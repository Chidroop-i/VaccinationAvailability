import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.net.URISyntaxException;

public class PhoneCalltw {
    public static void  makephoneCall(String number) throws URISyntaxException {
        //fill twillio Account details
        String ACCOUNT_SID = "";
        String AUTH_TOKEN = "";
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);

        String from = "Your twillio number";
        String to = number;

        Call call = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                new URI("http://demo.twilio.com/docs/voice.xml")).create();

        System.out.println(call.getSid());
    }
}
