package com.kdk.util;

import org.springframework.stereotype.Component;

@Component
abstract public class CommonConstant {

  public static final String TSDB_DOMAIN = "http://localhost:7777";
  public static final String TSDB_TYPE_PUT = "/api/put";
  public static final String TSDB_TYPE_QUERY = "/api/query";

  public static final String OPEN_API_DOMAIN = "http://openapi.airport.co.kr";
  public static final String OPEN_API_TYPE = "/service/rest/AirportParkingCongestion/airportParkingCongestionRT";
}
