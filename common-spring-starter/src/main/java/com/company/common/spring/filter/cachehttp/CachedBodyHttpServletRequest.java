package com.company.common.spring.filter.cachehttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletInputStream;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class CachedBodyHttpServletRequest extends ContentCachingRequestWrapper {
    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(ContentCachingRequestWrapper request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }

    public byte[] getCachedBody() {
        return this.cachedBody;
    }

    public void setCachedBody(byte[] cachedBody) {
        this.cachedBody = cachedBody;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headersName = this.getHeaderNames();

        while(headersName.hasMoreElements()) {
            String name = headersName.nextElement();
            map.put(name, this.getHeader(name));
        }

        return map;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(this.cachedBody);
    }
}

