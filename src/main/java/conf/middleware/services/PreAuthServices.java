package conf.middleware.services;

import io.agroal.api.AgroalDataSource;
import io.agroal.pool.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class PreAuthServices {
    private static final Logger log = LoggerFactory.getLogger(PreAuthServices.class);


    public Optional<Session> portalAuthenticate(String username, String password) {

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            subject.getSession();
            return Optional.of(subject.getSession());
        } catch (UnknownAccountException e) {
            log.info("unknown account", e);
            return Optional.empty();
        } catch (IncorrectCredentialsException ice) {
            log.info("incorrect credentials", ice);
            return Optional.empty();
        } catch (LockedAccountException lae) {
            log.info("Locked  accounts", lae);
            return Optional.empty();
        } catch (ExcessiveAttemptsException eae) {
            log.info("Excessive  accounts  attempts", eae);
            return Optional.empty();
        }
     catch (AuthenticationException efa) {
        log.info("Excessive  login failed", efa);
        return Optional.empty();
    }

    }

    public Optional<Boolean> portalLogout(String session){
try {
    Session ses =SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(session));
    ses.stop();
} catch (Exception e) {
    log.info("Error getting the session", e);
}        return   Optional.of(true);
        }







    }

