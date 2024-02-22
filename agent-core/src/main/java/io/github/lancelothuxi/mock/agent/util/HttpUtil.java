package io.github.lancelothuxi.mock.agent.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.config.registry.MockConfigRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(MockConfigRegistry.class);


    public static String sendPostRequest(String url, Object jsonData) {
       return sendPostRequest(url,jsonData,10000);
    }
    public static String sendPostRequest(String url, Object jsonData,int readTimeout) {
        HttpURLConnection con = null;
        BufferedReader in = null;

        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.setReadTimeout(readTimeout);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(JSON.toJSONString(jsonData));
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                return response.toString();
            } else {
                throw new Exception("POST request failed with response code: " + responseCode);
            }
        }catch (Exception ex){
            logger.error("http请求出错",ex);
            throw new RuntimeException("http请求出错000",ex);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
