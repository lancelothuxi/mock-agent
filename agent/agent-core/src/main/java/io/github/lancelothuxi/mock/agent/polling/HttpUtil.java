package io.github.lancelothuxi.mock.agent.polling;

import com.alibaba.fastjson.JSON;
import io.github.lancelothuxi.mock.agent.CharStreams;
import io.github.lancelothuxi.mock.agent.LogUtil;
import io.github.lancelothuxi.mock.agent.config.GlobalConfig;
import io.github.lancelothuxi.mock.agent.util.ConfigUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class HttpUtil {

    private ConfigUtil m_configUtil;

    /**
     * Constructor.
     */
    public HttpUtil() {
        m_configUtil = new ConfigUtil();
    }

    public static String doPost(Object data, String method, int readTimeOut) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(GlobalConfig.mockServerURL + method);
            // 通过远程url连接对象打开连接、
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (Exception ex) {
                System.out.println("mock agent http open connection failed = " + ex.getMessage());
            }

            // 设置连接请求方式
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(5000);
            if (readTimeOut != 0) {
                connection.setReadTimeout(readTimeOut);
            }
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            connection.connect();

            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(JSON.toJSONBytes(data));
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("http请求URL格式错误 message=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.log("long polling service error", e);
            throw new RuntimeException("http请求异常 message=" + e.getMessage());
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    public String doGet(HttpRequest httpRequest) {

        InputStreamReader isr = null;
        InputStreamReader esr = null;
        int statusCode;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(httpRequest.getUrl()).openConnection();

            conn.setRequestMethod("GET");

            int connectTimeout = httpRequest.getConnectTimeout();
            if (connectTimeout < 0) {
                connectTimeout = m_configUtil.getConnectTimeout();
            }

            int readTimeout = httpRequest.getReadTimeout();
            if (readTimeout < 0) {
                readTimeout = m_configUtil.getReadTimeout();
            }

            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            conn.connect();

            statusCode = conn.getResponseCode();
            String response;

            try {

                isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                response = CharStreams.toString(isr);

            } catch (IOException ex) {
                /**
                 * according to https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html,
                 * we should clean up the connection by reading the response body so that the connection
                 * could be reused.
                 */
                InputStream errorStream = conn.getErrorStream();

                if (errorStream != null) {
                    esr = new InputStreamReader(errorStream, StandardCharsets.UTF_8);
                    try {
                        CharStreams.toString(esr);
                    } catch (IOException ioe) {
                        //ignore
                    }
                }

                // 200 and 304 should not trigger IOException, thus we must throw the original exception out
                if (statusCode == 200 || statusCode == 304) {
                    throw ex;
                } else {
                    // for status codes like 404, IOException is expected when calling conn.getInputStream()
                    throw new LongPollingStatusCodeException(statusCode, ex);
                }
            }

            if (statusCode == 200) {
                return response;
            }

            if (statusCode == 304) {
                return "";
            }
        } catch (Throwable ex) {
            return "";
        } finally {

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    // ignore
                }
            }

            if (esr != null) {
                try {
                    esr.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }

        return "";
    }

    public String doPost(HttpRequest httpRequest) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpRequest.getUrl());
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");

            int connectTimeout = httpRequest.getConnectTimeout();
            if (connectTimeout < 0) {
                connectTimeout = m_configUtil.getConnectTimeout();
            }

            int readTimeout = httpRequest.getReadTimeout();
            if (readTimeout < 0) {
                readTimeout = m_configUtil.getReadTimeout();
            }

            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            connection.connect();

            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(JSON.toJSONBytes(httpRequest.getBody()));
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            LogUtil.log("long polling service error", e);
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }


}
