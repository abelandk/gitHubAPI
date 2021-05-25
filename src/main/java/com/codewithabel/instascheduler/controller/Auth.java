package com.codewithabel.instascheduler.controller;

import com.codewithabel.instascheduler.controller.model.AuthModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class Auth {

    static String clientId;
    static String clientSecret;

    @Value("${auth.redirectUri}")
    private String redirectUri;

    @Value("${api.accessTokenUrl}")
    private String accessTokenUrl;

    @Value("${api.authorizeUrl}")
    private String authorizeUrl;

    @Value("${api.authorizationCode}")
    private String grantTypeAuthCode;


    @GetMapping("/auth")
    public String authenticated(@RequestParam(value = "code") String authCode, Model model) {

        if(clientId == null || clientSecret == null)
        {
            model.addAttribute("error", "Client Id:" + clientId + " ,Client Secret:" + clientSecret);
            return "index";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        try {
            URI uri = new URI(accessTokenUrl);
            RestTemplate restTemplate = new RestTemplate();

            String str = "client_id=" + clientId + "&";
            str += "client_secret=" + clientSecret + "&";
            str += "grant_type=" + grantTypeAuthCode + "&";
            str += "redirect_uri=" + redirectUri + "&";
            str += "code=" + authCode;

            HttpEntity<String> httpEntity = new HttpEntity<>(str, headers);
            AuthModel authModel = restTemplate.postForObject(uri, httpEntity, AuthModel.class);

            if(authModel == null)
            {
                model.addAttribute("error", "authorization_code post request returned null");
            }
            else
            {
                model.addAttribute("authCode", authCode);
                model.addAttribute("access_token", authModel.getAccess_token());
                model.addAttribute("user_id", authModel.getUser_id());
            }
        }
        catch (URISyntaxException exception)
        {
            model.addAttribute("error", exception.getReason());
        }

        return "index";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "clientId") String _clientId,
                        @RequestParam(value = "clientSecret") String _clientSecret,
                        Model model) {

        clientId = _clientId;
        clientSecret = _clientSecret;

        String authCodeRequest = authorizeUrl + "?client_id=" + clientId + "&";
        authCodeRequest += "redirect_uri=" + redirectUri + "&";
        authCodeRequest += "scope=user_profile,user_media&";
        authCodeRequest += "response_type=code";


        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getForObject(authCodeRequest, String.class);
        return "index";
    }
}
