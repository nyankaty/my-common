package com.company.common.spring.config.resttemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

@Configuration
@SuppressWarnings("all")
public class UtilsRestTemplate {
    private static final Logger log = LoggerFactory.getLogger(UtilsRestTemplate.class);

    public UtilsRestTemplate() {
        // no arg constructor
    }

    public void trace(HttpRequest request, byte[] body, ClientHttpResponse response) {
        StringBuilder str = new StringBuilder();

        try {
            InputStream inputStreamBody = response.getBody();
            Throwable var6 = null;

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStreamBody, StandardCharsets.UTF_8);
                Throwable var8 = null;

                try {
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    Throwable var10 = null;

                    try {
                        str.append("\n Request = ").append(" \n").append("Request to : ").append(request.getURI().toString()).append(" \n").append("Method     : ").append(request.getMethodValue()).append(" \n").append("Header     : ").append(request.getHeaders()).append(" \n").append("Body       : ").append(new String(body, StandardCharsets.UTF_8)).append(" \n").append(" \n");
                        str.append("Response = ").append(" \n").append("Status code  : {}").append(response.getStatusCode()).append(" \n").append("Headers      : {}").append(response.getHeaders()).append(" \n");
                        StringBuilder inputStringBuilder = new StringBuilder();

                        for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                            inputStringBuilder.append(line);
                            inputStringBuilder.append('\n');
                        }

                        str.append("Response body: {}").append(inputStringBuilder.toString()).append(" \n").append(" \n");
                    } catch (Throwable var76) {
                        var10 = var76;
                        throw var76;
                    } finally {
                        if (bufferedReader != null) {
                            if (var10 != null) {
                                try {
                                    bufferedReader.close();
                                } catch (Throwable var75) {
                                    var10.addSuppressed(var75);
                                }
                            } else {
                                bufferedReader.close();
                            }
                        }

                    }
                } catch (Throwable var78) {
                    var8 = var78;
                    throw var78;
                } finally {
                    if (inputStreamReader != null) {
                        if (var8 != null) {
                            try {
                                inputStreamReader.close();
                            } catch (Throwable var74) {
                                var8.addSuppressed(var74);
                            }
                        } else {
                            inputStreamReader.close();
                        }
                    }

                }
            } catch (Throwable var80) {
                var6 = var80;
                throw var80;
            } finally {
                if (inputStreamBody != null) {
                    if (var6 != null) {
                        try {
                            inputStreamBody.close();
                        } catch (Throwable var73) {
                            var6.addSuppressed(var73);
                        }
                    } else {
                        inputStreamBody.close();
                    }
                }

            }
        } catch (Exception var82) {
            log.warn(var82.getMessage(), var82);
        } finally {
            log.info(str.toString());
        }
    }

    public void addHeader(HttpHeaders header, String headerName, String headerValue) {
        if (!header.containsKey(headerName)) {
            header.add(headerName, headerValue);
        }
    }
    
}

