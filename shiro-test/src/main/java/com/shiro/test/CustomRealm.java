package com.shiro.test;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {
    Map<String,String> userMap = new HashMap<String, String>(16);
    {
        userMap.put("Mark","73bea81c6c06bacab41a995495239545");
        super.setName("customRealm");
    }
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();
        //从数据库或者缓存中获取角色数据
        Set<String> roles = getRolesByUserName(userName);
        Set<String> perminssions = getPermissionsByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(perminssions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }
    private Set<String> getRolesByUserName(String userName){
        Set<String> sets = new HashSet<String>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }
    private Set<String> getPermissionsByUserName(String userName){
        Set<String> sets = new HashSet<String>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.从主体传过来的认证信息中获取用户名
        String userName = (String)token.getPrincipal();
        //2.通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("Mark",password,"customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("mark"));
        return simpleAuthenticationInfo;
    }

    /**
     * 模拟数据库查询返回数据
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName){

        return userMap.get(userName);
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","mark");
        System.out.println(md5Hash);
    }
}
