package in.pbaldu.cba;

import in.pbaldu.cba.altitude.MapzenClient;

import java.io.IOException;

public class Altitude {

  public static final double TEMPERATURE_LAPSE = 0.0065;

  private final String apiKey;

  public Altitude(String apiKey) {
    this.apiKey = apiKey;
  }

  public int calculate(double latitude, double longitude) throws IOException {
    MapzenClient client = new MapzenClient(apiKey);

    int altitude = client.fetchAltitude(latitude, longitude);

    return altitude > 0 ? altitude : 0;
  }
}
