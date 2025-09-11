package conf.middleware.services;

import io.agroal.api.AgroalDataSource;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.slf4j.Logger;

import javax.sql.DataSource;

import static org.apache.shiro.SecurityUtils.*;

@ApplicationScoped
public class ServiceSetUp {
    @Inject
    AgroalDataSource db;
    ServiceSetUp(){

    }
@Startup
    public void setupSecurity(){
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setAuthenticationQuery("SELECT password FROM  public.platform_users WHERE username = ?");
        jdbcRealm.setDataSource(this.db);

        jdbcRealm.setUserRolesQuery("SELECT role_name FROM platform_roles WHERE username = ?");
        jdbcRealm.setPermissionsQuery("SELECT permission FROM roles_permissions WHERE role_name = ?");
        SecurityUtils.setSecurityManager(new DefaultSecurityManager(jdbcRealm));
        Log.info("Service is staring ********************************************************SECURITY BEING SET IUP***************************************");
    }








}
