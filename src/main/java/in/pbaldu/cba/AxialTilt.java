package in.pbaldu.cba;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class AxialTilt {

  public static final double EARTH_YEAR = 365.25;
  public static final double EARTH_INCLINATION = 23.4;
  public static final long   EQUINOX = 172;

  public static double onDate(LocalDate date) {
    int doy = date.getDayOfYear();
    double grad = ((doy - EQUINOX) / EARTH_YEAR) * 2 * Math.PI;

    return Math.cos(grad) * EARTH_INCLINATION;
  }
}
