package in.pbaldu.cba;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AltitudeTest {
  private static final String API_KEY = "mapzen-DU7HoT7";
  private Altitude altitude;

  @Before
  public void setUp() throws Exception {
    altitude = new Altitude(API_KEY);
  }

  @Test
  public void altitude_of_Sao_Paulo_should_be_greater_than_Rio_de_Janeiro() throws IOException {
    double[] saoPaulo = {-23.55, -46.633333};
    double[] rioDeJaneiro = {-22.908333, -43.196389};

    int saoPauloElevation = altitude.calculate(saoPaulo[0], saoPaulo[1]);
    int rioDeJaneiroElevation = altitude.calculate(rioDeJaneiro[0], rioDeJaneiro[1]);

    assertTrue(saoPauloElevation > rioDeJaneiroElevation);
  }

  @Test
  public void altitude_of_Rio_de_Janeiro_should_be_greater_than_ocean() throws IOException {
    double[] guineaGulf = {0, 0};
    double[] rioDeJaneiro = {-22.908333, -43.196389};

    assertTrue(altitude.calculate(guineaGulf[0], guineaGulf[1]) < altitude.calculate(rioDeJaneiro[0], rioDeJaneiro[1]));
  }

}