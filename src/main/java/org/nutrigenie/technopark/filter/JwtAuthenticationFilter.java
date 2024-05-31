package org.nutrigenie.technopark.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nutrigenie.technopark.auth.JwtTokenProvider;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@WebFilter(urlPatterns = "/api/*")
public class JwtAuthenticationFilter implements Filter{

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void init(FilterConfig filterConfig){
        log.info("Filter 적용");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //request 에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(req);
        if(token != null){

            String tokenType = jwtTokenProvider.tokenTypeCheck(token);
            if(tokenType.equals("reT")){
                res.setStatus(HttpStatus.FORBIDDEN.value());
                res.getWriter().write("Check token value");
                return;
            }

            switch (jwtTokenProvider.validateToken(token)){
                case "Expired":
                    res.setStatus(HttpStatus.FORBIDDEN.value());
                    res.getWriter().write("A Expired");
                    break;
                case "Invalid":
                    res.setStatus(HttpStatus.FORBIDDEN.value());
                    res.getWriter().write("A Invalid");
                    break;
                default:
                    chain.doFilter(request, response);
                    break;
            }

        }else{
            res.setStatus(403);
            res.getWriter().write("ERR_T_500"); //token is null
        }



    }

}
