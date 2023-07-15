package com.company.common.spring.filter.cachehttp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedBodyServletInputStream extends ServletInputStream {
    private static final Logger log = LoggerFactory.getLogger(CachedBodyServletInputStream.class);
    private final InputStream cachedBodyInputStream;

    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    public boolean isFinished() {
        try {
            return this.cachedBodyInputStream.available() == 0;
        } catch (IOException var2) {
            log.error(var2.getMessage(), var2);
            return false;
        }
    }

    public boolean isReady() {
        return true;
    }

    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    public int read() throws IOException {
        return this.cachedBodyInputStream.read();
    }
}
