package com.kdk.service;

import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kdk.util.CommonConstant;

@Service
public class RealTimeService {

  static final Logger logger = LoggerFactory.getLogger(RealTimeService.class);

  @Value("${open-api.key}")
  private String apiKey;

  private static String getTagValue(String tag, Element eElement) {
    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
    Node nValue = (Node) nlList.item(0);
    if (nValue == null)
      return null;
    return nValue.getNodeValue();
  }

  public List<Map<String, Object>> airportAPiCall(String airportCode) throws Exception {
    String url = CommonConstant.OPEN_API_DOMAIN + CommonConstant.OPEN_API_TYPE 
        + "?serviceKey=" + apiKey + "&schAirportCode=" + airportCode;

    DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
    Document doc = dBuilder.parse(url);

    NodeList nList = doc.getElementsByTagName("item");

    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    for (int temp = 0; temp < nList.getLength(); temp++) {
      Node nNode = nList.item(temp);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();

        Element eElement = (Element) nNode;
        dataMap.put("parkingAirportCodeName", getTagValue("parkingAirportCodeName", eElement)); // 주차장명
        dataMap.put("parkingTotalSpace", getTagValue("parkingTotalSpace", eElement)); // 전체 주차면 수
        dataMap.put("parkingOccupiedSpace", getTagValue("parkingOccupiedSpace", eElement)); // 입고된 차량 수

        dataList.add(dataMap);
      }
    }

    return dataList;
  }

  public int openTSDataInsert(String metric, long timestamp, int value, JSONObject tags) throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    URI url = URI.create(CommonConstant.TSDB_DOMAIN + CommonConstant.TSDB_TYPE_PUT + "?details");

    JSONObject jsonReq = new JSONObject();
    jsonReq.put("metric", metric);
    jsonReq.put("timestamp", timestamp);
    jsonReq.put("value", value);
    jsonReq.put("tags", tags);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "*/*");

    HttpEntity<String> entity = new HttpEntity<String>(jsonReq.toString(), headers);
    ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

    return (res.getStatusCode() == HttpStatus.OK) ? 1 : 0;
  }

  public JSONArray getAirportJsonArrayDatas() throws Exception {
    JSONParser jsonParser = new JSONParser();
    ClassPathResource resource = new ClassPathResource("static/json/airport_parking_data.json");
    
    Object obj = jsonParser.parse(new FileReader(resource.getFile()));
    JSONObject jsonObject = (JSONObject) obj;
    JSONArray datas = (JSONArray) jsonObject.get("datas");

    return datas;
  }

  public int minuteScheduleF(long timestamp) {
    int result = 0;

    try {

      JSONObject tags = new JSONObject();
      tags.put("host", "kdk");

      JSONArray datas = getAirportJsonArrayDatas();

      for (int i = 0; i < datas.size(); i++) {
        JSONObject data = (JSONObject) ((JSONObject) datas.get(i));
        List<Map<String, Object>> apiDatas = airportAPiCall(data.get("airportCode").toString());

        for (Map<String, Object> apiData : apiDatas) {
          String parkingName = data.get("parkingAirportCodeName").toString();
          String metric = "kdk." + data.get("airportCode") + ".";
          result =+ openTSDataInsert(metric, timestamp, Integer.parseInt(apiData.get("parkingOccupiedSpace").toString()), tags);
        }
      }

    } catch(Exception e) {
      result = 0;
    }

    return result;
  }
}