package com.kdk.util;

import org.springframework.stereotype.Component;

@Component
abstract public class CommonConstant {

  public static final String TSDB_DOMAIN = "http://127.0.0.1:6379";

  public static final String OPEN_API_DOMAIN = "http://openapi.airport.co.kr";
  public static final String OPEN_API_TYPE = "/service/rest/AirportParkingCongestion/airportParkingCongestionRT";
  
  public static final String REDIS_PREFIX_INFO = "INFO_";
  public static final String REDIS_PREFIX_FLAG = "FLAG_";
  public static final String REDIS_PREFIX_TIME = "TIME_";

  public static final String REDIS_SUFFIX_ALERT = "_ALERT";
  public static final String REDIS_SUFFIX_PM10 = "_PM10";
  public static final String REDIS_SUFFIX_PM25 = "_PM25";
  public static final String REDIS_SUFFIX_CO2 = "_CO2";
  public static final String REDIS_SUFFIX_VOC = "_VOC";
  public static final String REDIS_SUFFIX_TEMP = "_TEMP";
  public static final String REDIS_SUFFIX_HUMI = "_HUMI";

  public static final String REDIS_NO_DATA = "NO_DATA";
}
