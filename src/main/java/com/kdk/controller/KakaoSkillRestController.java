package com.kdk.controller;

import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class KakaoSkillRestController {
  
  static final Logger logger = LoggerFactory.getLogger(KakaoSkillRestController.class);
  
    @RequestMapping(value = "/kakao", method = RequestMethod.GET)
    public JSONObject kakaoSkillTest() {
        JSONObject jsonObject = new JSONObject();
        logger.info("접속요청");
        try {

            JSONParser jsonParser = new JSONParser();
            ClassPathResource resource = new ClassPathResource("static/json/test.json");
            Object obj = jsonParser.parse(new FileReader(resource.getFile()));
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            System.out.println("Error :: " + e.getMessage());
        }

        return jsonObject;
    }
}