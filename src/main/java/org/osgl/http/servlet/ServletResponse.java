package org.osgl.http.servlet;

import org.osgl.http.H;
import org.osgl.util.E;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;

public class ServletResponse extends H.Response<ServletResponse> {
    @Override
    protected Class<ServletResponse> _impl() {
        return ServletResponse.class;
    }

    private HttpServletResponse r;

    public ServletResponse(HttpServletResponse resp) {
        E.NPE(resp);
        r = resp;
    }

    @Override
    public String characterEncoding() {
        return r.getCharacterEncoding();
    }

    @Override
    public ServletResponse characterEncoding(String encoding) {
        r.setCharacterEncoding(encoding);
        return this;
    }

    @Override
    public ServletResponse contentLength(long len) {
        r.setContentLength((int) len);
        return this;
    }

    /**
     * This method is not supported in ServletResponse
     * @param buffer direct byte buffer
     * @return this response instance
     */
    @Override
    public ServletResponse writeContent(ByteBuffer buffer) {
        throw E.unsupport("Writing direct byte buffer is not supported by ServletResponse");
    }

    @Override
    protected OutputStream createOutputStream() {
        try {
            return r.getOutputStream();
        } catch (IOException e) {
            throw E.ioException(e);
        }
    }

    @Override
    protected void _setContentType(String type) {
        r.setContentType(type);
    }

    @Override
    protected void _setLocale(Locale loc) {
        r.setLocale(loc);
    }

    @Override
    public Locale locale() {
        return r.getLocale();
    }

    @Override
    public ServletResponse addCookie(H.Cookie cookie) {
        r.addCookie(ServletCookie.asServletCookie(cookie));
        return me();
    }

    @Override
    public boolean containsHeader(String name) {
        return r.containsHeader(name);
    }

    @Override
    public ServletResponse sendError(int sc, String msg) {
        try {
            r.sendError(sc, msg);
        } catch (IOException e) {
            throw E.ioException(e);
        }
        return this;
    }

    @Override
    public ServletResponse sendError(int sc) {
        try {
            r.sendError(sc);
        } catch (IOException e) {
            throw E.ioException(e);
        }
        return this;
    }

    @Override
    public ServletResponse sendRedirect(String location) {
        try {
            r.sendRedirect(location);
        } catch (IOException e) {
            throw E.ioException(e);
        }
        return this;
    }

    @Override
    public ServletResponse header(String name, String value) {
        r.setHeader(name, value);
        return this;
    }

    @Override
    public ServletResponse header(H.Header header) {
        List<String> values = header.values();
        int len = values.size();
        if (len > 0) {
            String name = header.name();
            r.setHeader(name, values.get(0));
            for (int i = 1; i < len; ++i) {
                r.addHeader(name, values.get(i));
            }
        }
        return this;
    }

    @Override
    public ServletResponse addHeaderValues(String name, String... values) {
        if (values.length < 1) {
            return this;
        }
        for (String value : values) {
            r.addHeader(name, value);
        }
        return this;
    }

    @Override
    public ServletResponse status(int sc) {
        r.setStatus(sc);
        return this;
    }

    @Override
    public ServletResponse addHeader(String name, String value) {
        r.addHeader(name, value);
        return this;
    }

    @Override
    public void commit() {
        try {
            r.flushBuffer();
        } catch (IOException e) {
            throw E.ioException(e);
        }
    }
}
