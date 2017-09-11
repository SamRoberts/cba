# CBA

Author: Plínio Balduino

## Run

To compile and test the application:

`mvn clean install`

To run the application:

`mvn exec:java`

## Requirements

Create a toy simulation of the environment (taking into account things like atmosphere, topography, geography, oceanography, or similar) that evolves over time. Then take measurements at various locations and times, and have your program emit that data, as in the following:

| Location  | Position         | Local time          | Condition | Temperature | Pressure | Humidity |
|-----------|------------------|---------------------|-----------|------------:|---------:|---------:|
| Sydney    | -33.86,151.21,39 | 2015-12-23 16:02:12 | Rain      | +12.5       | 1010.3   | 97       |
| Melbourne | -37.83,144.98,7  | 2015-12-25 02:30:55 | Snow      | -5.3        | 998.4    | 55       |
| Adelaide  | -34.92,138.62,48 | 2016-01-04 23:05:37 | Sunny     | +39.4       | 1114.1   | 12       |

Obviously you can’t give it to us as a table (ok, yes, you could feed us markdown, but let’s not do that?) so instead submit your data to us in the following format:

```
    Sydney|-33.86,151.21,39|2015-12-23T05:02:12Z|Rain|+12.5|1004.3|97
    Melbourne|-37.83,144.98,7|2015-12-24T15:30:55Z|Snow|-5.3|998.4|55
    Adelaide|-34.92,138.62,48|2016-01-03T12:35:37Z|Sunny|+39.4|1114.1|12
```

where

* Location is an optional label describing one or more positions,

* Position is a comma-separated triple containing latitude, longitude, and elevation in metres above sea level,
* Local time is an ISO8601 date time,
* Conditions is either Snow, Rain, Sunny,
* Temperature is in °C,
* Pressure is in hPa, and
* Relative humidity is a %.

Your toy weather simulation should report data from a reasonable number of positions; 10±. The weather simulation will be used for games and does not need to be meteorogically accurate, it just needs to be emit weather data that looks plausible to a layperson.

So far we have assumed that our game takes place on Earth, leading to the use of latitude and longitude for co-ordinates and earth-like conditions. If you choose to assume that the game takes place elsewhere, please document any corresponding changes to the output format.

## Astronomic metrics

### Axial tilt

The Equator is not perfectly aligned with the Earth orbital plan. This inclination, that varies from +23.4°[1.1] in the North's Summer to -23.4° in the North's Winter.

It means that, in the North's Summer, the sun rays hits the surface more directly. The horizontal line that it happens in the North is called Tropic of Cancer. The South's counterpart is called Tropic of Capricorn.

In the Winter, the sun hits the hemisphere less directly, increasing the mean temperature.

We're going to use a sine wave to calculate the axial tilt on a specific date.

### Latitude

Latitude is the angular position in the North-South axis of Earth's surface. The closer you are to a Tropic in the Summer, the greater is the temperature. The closer you to a Pole, the colder is the weather.

It goes from 90° at the North Pole, to -90° at the South Pole, being the Equator the 0° mark.

### Solar time

Solar time is the time based on Solar Azimuth. When the Sun is in the apex, relative to who's observing, it's Noon. It's not always related with timezones and standard times.

As a rule of thumb, the closer we are from Solar Noon, the greater is the temperature.

## Atmospheric metrics

### Altitude

The Earth is not a flat rocky sphere roaming through the Universe. It has lower and higher points on its surface and we called this set of different heights as 'Relief'.

This relief impacts directly on the temperature and on the atmospheric pressure calculation.

We call _Altitude_ as the difference between a point height and the sea level.

As a rule of thumb we can say that temperature drops 9.8°C per km, ignoring materials, humidity and greenhouse effect[2.1]. It's called Lapse Rate. A moist atmosphere has an average temperature lapse rate of 6.49°C per km[2.2]. That's our standard lapse rate when humidity is greater than 30%.

From 11km up to 20km, we assume a constant temperature of -56.6°C. As the highest point on Earth is the Mt Everest, with 8848 meters, we won't implement a checking to this altitude.

We're going to use two forms of altitude calculation: an API query, with quite accurate results, and a NASA high quality image as fallback with poorer accuracy.

### Humidity

For the sake of our explanation, let's limit ourselves to two humidity measures.

The first one is called _Absolute humidity_ and is the total mass of water vapor in a given volume of air. It doesn't depends on the temperature. We're going to consider it to be between 0 g/m<sup>3</sup> and 30 g/m<sup>3</sup>[2.5].

The measure we're going to use in our calculations is called _Relative humidity_, expressed as percentage of current absolute humidity over the maximum value. It's temperature dependent.

As a rule of thumb, ignoring the existence of oceans, rain forests, deserts, winds and specific weather conditions, the humidity is greater close to Equator line.

Absolute humidity is independent of air amount, so we won't consider altitude for this calculation.

### Air pressure

_Air pressure_ is the weight of atmosphere on a specific point and we will use hPa (hectopascals) as unit. One Pascal is equivalent to one Newton over a square meter[2.3].

The standard air pressure at sea level is 1013.25 hPa[2.2] and we'e going to use this value as reference in our calculations.

As the air pressure is directly affected by altitude and humidity, we're going to use a simplified form of the _barometric formula_[2.4] to calculate it.

## Calculation

### Temperature

tl = range of temperatures related to latitude [max, min]
sh = solar hour. max at noon, min at midnight.
alt = altitude in meters

`temp = tl * sh * alt`

### Humidity

hl = humidity related to latitude

If rain forest:

`humidity = hl * 1.5`

If desert:

`humidity = hl * 0.2`

### Air pressure

Uses the _Barometric Formula_[2.4]

## References:

[1.1] Astronomical Almanac 2010, p. B52

[2.1] Danielson, Levin, and Abrams, Meteorology, McGraw Hill, 2003

[2.2] Manual of the ICAO Standard Atmosphere (extended to 80 kilometres (262 500 feet)) (Third ed.). International Civil Aviation Organization. 1993. ISBN 92-9194-004-6. Doc 7488-CD.

[2.3] International Bureau of Weights and Measures (2006), The International System of Units (SI) (8th ed.), p. 118, ISBN 92-822-2213-6.

[2.4] Barometric formula - https://en.wikipedia.org/wiki/Barometric_formula

[2.5] Lowe, P.R. 1977. An approximating polynomial for the computation of saturation vapor pressure. J. Applied Meteorology 16: 100-104. https://doi.org/10.1175/1520-0450(1977)016
