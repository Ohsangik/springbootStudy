package com.supercoding.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RestAPI {

    @GetMapping("/GetkobisData")
    public String callAPI() {

        HashMap<String, Object> result = new HashMap<String, Object>();

        String jsonInString = "";

        try {

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";

            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url+"?"+"key=430156241533f1d058c603178cc3ca0e&targetDt=20120101").build();

            //이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

            result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            LinkedHashMap lm = (LinkedHashMap)resultMap.getBody().get("boxOfficeResult");
            ArrayList<Map> dboxoffList = (ArrayList<Map>)lm.get("dailyBoxOfficeList");
            LinkedHashMap mnList = new LinkedHashMap<>();
            for (Map obj : dboxoffList) {
                mnList.put(obj.get("rnum"),obj.get("movieNm"));
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(mnList);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println("dfdfdfdf");
            System.out.println(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetion오류");
            System.out.println(e.toString());
        }

        return jsonInString;

    }

    @GetMapping("/cmapi")
    public String callCommonAPI() {

        HashMap<String, Object> result = new HashMap<String, Object>();

        String jsonInString = "";
        String authkey = "DQYqPdYO87JMLpXGo08vRxxJiINqQuS4u%2BVgvCjFeq53ox0z3kxHs%2BfLdE6VElzrSO0nT25zGtVtwlSMYh6YOg%3D%3D";
        try {
            String decodeServiceKey = URLDecoder.decode(authkey, "UTF-8");

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(10000); //타임아웃 설정 5초
            factory.setReadTimeout(10000);//타임아웃 설정 5초
            RestTemplate restTemplate = new RestTemplate(factory);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            //params.add("ServiceKey", authkey);
            params.add("type","json");
            params.add("pageNo","1");
            params.add("numOfRows","10");
            params.add("flag","Y");

            String url = "http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList?ServiceKey="+authkey;
            String url2 = "http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList?ServiceKey="+authkey+"&type=json&pageNo=1&numOfRows=10&flag=Y";
            //String url2 = "http://phototicket.cgv.co.kr/cgv/cinema.asmx/GetCinemaList";
            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(url)
                    .queryParams(params)
                    .build(false);

            URI i = URI.create(url2);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));    //Response Header to UTF-8
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> resultMap = restTemplate.exchange(uri.toUri(), HttpMethod.GET, requestEntity, String.class);
            //ResponseEntity<String> rex = restTemplate.getForEntity(i, String.class);
            //String rex = restTemplate.getForObject(i,String.class);

            //result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            //result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            //result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            ObjectMapper mapper = new ObjectMapper();
            //jsonInString = mapper.writeValueAsString(resultMap.getBody());
            //jsonInString = mapper.writeValueAsString(rex.getBody());;
            //jsonInString = rex;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println("dfdfdfdf");
            System.out.println(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetion오류");
            System.out.println(e.toString());
        }

        return jsonInString;

    }

    @GetMapping("/cmapi2")
    public String callCommonAPI2() {

        HashMap<String, Object> result = new HashMap<String, Object>();

        String jsonInString = "";
        String authkey = "DQYqPdYO87JMLpXGo08vRxxJiINqQuS4u%2BVgvCjFeq53ox0z3kxHs%2BfLdE6VElzrSO0nT25zGtVtwlSMYh6YOg%3D%3D";
        try {
            String decodeServiceKey = URLDecoder.decode(authkey, "UTF-8");

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(10000); //타임아웃 설정 5초
            factory.setReadTimeout(10000);//타임아웃 설정 5초
            RestTemplate restTemplate = new RestTemplate(factory);

            String url = "http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList";
            String url2 = "http://apis.data.go.kr/1741000/EarthquakeIndoors/getEarthquakeIndoorsList?ServiceKey="+authkey+"&type=json&pageNo=1&numOfRows=10&flag=Y";

            //서버 헤더설정
            HttpHeaders requestHeaders = new HttpHeaders();
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("ServiceKey", decodeServiceKey);
            params.add("type","json");
            params.add("pageNo","1");
            params.add("numOfRows","10");
            params.add("flag","Y");

            //서버 요청 파라미터
            HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<MultiValueMap<String, String>>(params, requestHeaders);

            URI i = URI.create(url2);

            //HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(new MediaType("application","json",Charset.forName("UTF-8")));    //Response Header to UTF-8
            //HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> resultMap = restTemplate.exchange(url, HttpMethod.GET, req, String.class);
            //ResponseEntity<String> rex = restTemplate.getForEntity(i, String.class);
            //String rex = restTemplate.getForObject(i,String.class);

            //result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
            //result.put("header", resultMap.getHeaders()); //헤더 정보 확인
            //result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());
            //jsonInString = mapper.writeValueAsString(rex.getBody());;
            //jsonInString = rex;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body"  , e.getStatusText());
            System.out.println("dfdfdfdf");
            System.out.println(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body"  , "excpetion오류");
            System.out.println(e.toString());
        }

        return jsonInString;

    }


}
