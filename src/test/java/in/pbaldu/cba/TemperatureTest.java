package in.pbaldu.cba;

import org.junit.Test;

import java.time.LocalTime;

import static in.pbaldu.cba.AxialTilt.EARTH_INCLINATION;
import static junit.framework.TestCase.assertTrue;

public class TemperatureTest {

  @Test
  public void base_temperature_should_be_hotter_in_equator_than_in_pole() {
    double tempEquator = Temperature.at(0, 0, LocalTime.NOON);
    double tempSouthPole = Temperature.at(-90, 0, LocalTime.NOON);
    double tempNorthPole = Temperature.at(90, 0, LocalTime.NOON);

    assertTrue(tempEquator > tempSouthPole);
    assertTrue(tempEquator > tempNorthPole);
  }

  @Test
  public void base_temperature_should_be_hotter_in_equator_than_in_tropics() {
    double tempEquator = Temperature.at(0, 0, LocalTime.NOON);
    double tempCapricorn = Temperature.at(-EARTH_INCLINATION, 0, LocalTime.NOON);
    double tempCancer = Temperature.at(EARTH_INCLINATION, 0, LocalTime.NOON);

    assertTrue(tempEquator > tempCapricorn);
    assertTrue(tempEquator > tempCancer);
  }

  @Test
  public void temperature_should_be_hotter_at_sea_level_than_on_a_mountain() {
    double seaLevel = Temperature.at(0, 0, LocalTime.NOON);
    double mountain = Temperature.at(0, 2000, LocalTime.NOON);

    assertTrue(seaLevel > mountain);
  }

  @Test
  public void temperature_at_equator_should_be_hotter_at_noon_than_at_midnight() {
    double noon = Temperature.at(0, 0, LocalTime.NOON);
    double midnight = Temperature.at(0, 0, LocalTime.MIDNIGHT);

    assertTrue(noon > midnight);
  }

  @Test
  public void temperature_at_south_pole_should_be_hotter_at_noon_than_at_midnight() {
    double noon = Temperature.at(-90, 0, LocalTime.NOON);
    double midnight = Temperature.at(-90, 0, LocalTime.MIDNIGHT);

    assertTrue(noon > midnight);
  }

  @Test
  public void temperature_at_north_pole_should_be_hotter_at_noon_than_at_midnight() {
    double noon = Temperature.at(90, 0, LocalTime.NOON);
    double midnight = Temperature.at(90, 0, LocalTime.MIDNIGHT);

    assertTrue(noon > midnight);
  }

}