package in.pbaldu.cba;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class WeatherGenerator {
  private static final String API_KEY = System.getProperty("ELEVATION_API_KEY");

  private enum Condition {
    SUNNY("Sunny"),
    RAINY("Rainy"),
    SNOW("Snow");

    private String name;

    Condition(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  };

  public static void main(String... args) throws IOException {
    new WeatherGenerator().start();
  }

  /*
   read file
   apply axial tilt to city latitude

   get altitude
   calculate a random datetime

   calculate temperature
   calculate humidity
   calculate pressure
   calculate weather condition
   output to console
  */
  private void start() throws IOException {
    CityReader reader = new CityReader("data/worldcities-basic.csv");
    List<City> cities = reader.readAll();
    Altitude altitude = new Altitude(API_KEY);

    for (City city: cities) {
      int elevation = altitude.calculate(city.getLatitude(), city.getLongitude());
      LocalDateTime dateTime = randomDateTime();

      double relativeLatitude = city.getLatitude() - AxialTilt.onDate(dateTime.toLocalDate());

      double temperature = Temperature.at(relativeLatitude, elevation, dateTime.toLocalTime());

      double humidity = new Humidity().at(relativeLatitude, city.getLongitude());

      double pressure = Pressure.calculate(elevation, temperature);

      Condition condition = calculateCondition(temperature, humidity);

      output(System.out,
          city,
          elevation,
          dateTime,
          temperature,
          humidity,
          pressure,
          condition);
    }
  }

  private void output(PrintStream out, City city, double elevation, LocalDateTime dateTime, double temperature, double humidity, double pressure, Condition condition) {
    StringBuffer line = new StringBuffer();

    long timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timestamp);

    line.append(city.getName())
        .append("|")
        .append(city.getLatitude())
        .append(",")
        .append(city.getLongitude())
        .append(",")
        .append(elevation)
        .append("|")
        .append(DateTimeFormatter.ISO_INSTANT.format(dateTime.toInstant(ZoneOffset.UTC)))
        .append("|")
        .append(condition)
        .append("|")
        .append(Math.round(temperature * 10) / 10.0)
        .append("|")
        .append(Math.round(pressure * 10) / 10.0)
        .append("|")
        .append(humidity);

    out.println(line.toString());
  }

  private Condition calculateCondition(double temperature, double humidity) {
    if(temperature < -5 && humidity > 50) {
      return Condition.SNOW;
    } else if (humidity > 70) {
      return Condition.RAINY;
    }

    return Condition.SUNNY;
  }

  private LocalDateTime randomDateTime() {
    LocalDateTime now = LocalDateTime.now();
    long beginTime = now.minus(1, ChronoUnit.YEARS).toEpochSecond(ZoneOffset.UTC);
    long endTime = now.toEpochSecond(ZoneOffset.UTC);

    long diff = endTime - beginTime + 1;
    long randomTimestamp = beginTime + (long) (Math.random() * diff);

    LocalDateTime randomDate = LocalDateTime.ofEpochSecond(randomTimestamp, 0, ZoneOffset.UTC);

    return randomDate;
  }
}
