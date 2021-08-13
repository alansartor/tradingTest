package ar.com.signals.trading.util.spring;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class MyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        httpServletResponse.addHeader("X-Content-Type-Options", "nosniff");
        httpServletResponse.addHeader("X-Frame-Options", "SAMEORIGIN");
        httpServletResponse.addHeader("Strict-Transport-Security", "max-age=31536000");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
