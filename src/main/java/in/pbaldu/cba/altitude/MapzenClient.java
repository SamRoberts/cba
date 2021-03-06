package in.pbaldu.cba.altitude;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapzenClient {

  private static final String HOST_URL = "http://elevation.mapzen.com";
  private static final String SERVICE_NAME = "/height?json=$JSON$&api_key=$APIKEY$";
  private final String apiKey;

  public MapzenClient(String apiKey) {
    this.apiKey = apiKey;
  }

  public int fetchAltitude(double latitude, double longitude) throws IOException {
    final Pattern REGEX = Pattern.compile("height\":\\[([-]?\\d+)");

    String decodedUrl = decodeUrl(latitude, longitude);

    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet request = new HttpGet(decodedUrl);
    CloseableHttpResponse response = httpclient.execute(request);

    int altitude = 0;

    try {
      HttpEntity entity = response.getEntity();

      BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
      String result = br.readLine();

      Matcher m = REGEX.matcher(result);

      while(m.find()) {
        altitude = Integer.parseInt(m.group(1));
      }
    } finally {
      response.close();
    }

    return altitude;
  }

  String decodeUrl(double latitude, double longitude) {
    StringBuffer result = new StringBuffer();
    StringBuffer json = new StringBuffer();

    json.append("%7B%22range%22:false,%22shape%22:%5B%7B%22lat%22:")
        .append(latitude)
        .append(",%22lon%22:")
        .append(longitude)
        .append("%7D%5D%7D");

    result.append(HOST_URL);
    result.append(SERVICE_NAME.replaceAll("\\$APIKEY\\$", apiKey)
                              .replaceAll("\\$JSON\\$", json.toString()));

    return result.toString();
  }

}
