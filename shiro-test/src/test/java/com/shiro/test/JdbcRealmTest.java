package com.shiro.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {
    DruidDataSource datasource = new DruidDataSource();
    {
        datasource.setUrl("jdbc:mysql://localhost:3306/springboot");
        datasource.setUsername("root");
        datasource.setPassword("123456");
    }
    @Test
    public void testAuthentication(){
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(datasource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql = "select password from test_user where user_name = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql = "select .....(用户名和角色表的查询)";
        jdbcRealm.setUserRolesQuery(roleSql);
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        //2.主体提交认真请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token =  new UsernamePasswordToken("xiaoming","123456");
        subject.login(token);

        System.out.println("isAuthenticated 1 : "+subject.isAuthenticated());
        subject.isAuthenticated();




    }
}
