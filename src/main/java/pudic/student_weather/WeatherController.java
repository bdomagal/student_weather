package pudic.student_weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pudic.student_weather.current.OpenWeatherObject;
import pudic.student_weather.forecast.OpenWeatherForecastObject;

@RestController
public class WeatherController {

    @Value("${openWeather.AppID}")
    private String appID;
    @Value("${openWeather.baseAddress}")
    private String baseAddress;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String hello(){
        return "POGOSTUDENT TEŻ DZIAŁA";
    }

    @GetMapping("/getWeather")
    public ResponseEntity<OpenWeatherObject> getWeather(@RequestParam(value = "q", required = false)String city,
                                                        @RequestParam(value = "lat", required = false) Double latitude,
                                                        @RequestParam(value = "lon", required = false) Double longitude
                             ){
        String uri;
        if(latitude!=null && longitude!=null){
            uri = String.format("%s weather?lat=%f&lon=%f&appid=%s", baseAddress, latitude, longitude, appID);
        }else {
            if (city != null) {
                uri = String.format("%s weather?q=%s&appid=%s", baseAddress, city, appID);
                System.out.println(uri);
            } else {
                return new ResponseEntity<OpenWeatherObject>(HttpStatus.BAD_REQUEST);
            }
        }
        ResponseEntity<OpenWeatherObject> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<OpenWeatherObject>() {
        });
        OpenWeatherObject owo = responseEntity.getBody();

        return new ResponseEntity<OpenWeatherObject>(owo, HttpStatus.OK);

    }

    @GetMapping("/getForecast")
    public ResponseEntity<OpenWeatherForecastObject> getForecast(@RequestParam(value = "q", required = false)String city,
                                                                 @RequestParam(value = "lat", required = false) Double latitude,
                                                                 @RequestParam(value = "lon", required = false) Double longitude
    ){
        String uri;
        if(latitude!=null && longitude!=null){
            uri = String.format("%s forecast?lat=%f&lon=%f&appid=%s", baseAddress, latitude, longitude, appID);
        }else {
            if (city != null) {
                uri = String.format("%s forecast?q=%s&appid=%s", baseAddress, city, appID);
                System.out.println(uri);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        ResponseEntity<OpenWeatherForecastObject> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<OpenWeatherForecastObject>() {
        });
        OpenWeatherForecastObject owfo = responseEntity.getBody();

        return new ResponseEntity<>(owfo, HttpStatus.OK);

    }
}
