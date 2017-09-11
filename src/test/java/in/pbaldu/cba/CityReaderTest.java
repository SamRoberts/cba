package in.pbaldu.cba;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class CityReaderTest {

  @Test
  public void should_not_return_an_empty_list() throws IOException {
    CityReader reader = new CityReader("data/worldcities-basic.csv");

    List<City> list = reader.readAll();

    assertFalse(list.isEmpty());
  }
}