package ewubd.edu.lab_final;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.URLEncodedUtils;
import java.io.*;
import java.util.List;
import java.net.*;
@SuppressWarnings("ALL")
public class RemoteAccess {

    private String TAG = "RemoteAccess";
    private static RemoteAccess instance = new RemoteAccess();
    private RemoteAccess() {}
    public static RemoteAccess getInstance() {
        return instance;
    }

    public String makeHttpRequest(String url, String method, List<NameValuePair> params) {

        HttpURLConnection http = null;
        InputStream is = null;
        String data = "";
        // Making HTTP request
        try {
            // check for request method
            if (method.equals("POST")) {
                //httpClient = new DefaultHttpClient();
                if(params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
            }
            System.out.println("@RemoteAccess-"+": "+ url);
            URL urlc = new URL(url);
            http = (HttpURLConnection) urlc.openConnection();
            //System.out.println("Here 2");
            http = (HttpURLConnection) urlc.openConnection();
            //System.out.println("Here 3");
            http.connect();
            is = http.getInputStream();
            //System.out.println("Here 4");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            http.disconnect();
        } catch (Exception e) {
        }
        return null;
    }
}