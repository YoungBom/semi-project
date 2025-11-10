package filter;

import dao.UserDAO;
import dto.UserDTO;
import util.SessionKeys;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class RememberMeFilter implements Filter {
    private final UserDAO userDao = new UserDAO();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse p = (HttpServletResponse) res;

        HttpSession s = r.getSession(false);
        boolean loggedIn = (s != null && s.getAttribute(SessionKeys.LOGIN_UID) != null);

        if (!loggedIn) {
            Cookie[] cookies = r.getCookies();
            if (cookies != null) {
                Cookie remember = Arrays.stream(cookies)
                        .filter(c -> "REMEMBER".equals(c.getName()))
                        .findFirst().orElse(null);
                if (remember != null && remember.getValue() != null && !remember.getValue().isBlank()) {
                    Optional<UserDTO> uopt = userDao.findByRememberToken(remember.getValue());
                    if (uopt.isPresent()) {
                        HttpSession ns = r.getSession(true);
                        UserDTO u = uopt.get();
                        ns.setAttribute(SessionKeys.LOGIN_UID, u.getId());
                        ns.setAttribute(SessionKeys.LOGIN_USERID, u.getUserId());
                        ns.setAttribute(SessionKeys.LOGIN_NAME, u.getName());
                    } else {
                        // 만료/무효 토큰 쿠키 정리
                        remember.setMaxAge(0);
                        remember.setPath(r.getContextPath().isEmpty() ? "/" : r.getContextPath());
                        p.addCookie(remember);
                    }
                }
            }
        }
        chain.doFilter(req, res);
    }
}
