package it.academy.app.configs;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public class DisableUrlSessionFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

            if (!(request instanceof HttpServletRequest)) {
                chain.doFilter(request, response);
                return;
            }

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            if (httpRequest.isRequestedSessionIdFromURL()) {
                HttpSession session = httpRequest.getSession();
                if (session != null)
                    session.invalidate();
            }

            HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {

                @Override
                public String encodeRedirectUrl(String url) {
                    return url;
                }

                public String encodeRedirectURL(String url) {
                    return url;
                }

                public String encodeUrl(String url) {
                    return url;
                }

                public String encodeURL(String url) {
                    return url;
                }
            };

            chain.doFilter(request, wrappedResponse);

        }

        @Override
        public void destroy() {
        }

    }