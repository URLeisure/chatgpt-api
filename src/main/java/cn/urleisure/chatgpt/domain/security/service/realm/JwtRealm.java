package cn.urleisure.chatgpt.domain.security.service.realm;


import cn.urleisure.chatgpt.domain.security.model.vo.JwtToken;
import cn.urleisure.chatgpt.domain.security.service.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 扑腾的江鱼
 * @description: 自定义 Realm
 * @create: 2023/11/20 13:03
 **/

public class JwtRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(JwtRealm.class);

    private static JwtUtil jwtUtil= new JwtUtil();

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //暂不实现
        return null;
    }
    //实现授权逻辑
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String jwt = (String)authenticationToken.getPrincipal();
        if(jwt == null){
            throw new NullPointerException("jwtToken 不允许为空");
        }
        //判断
        if(!jwtUtil.isVerify(jwt)){
            throw new UnknownAccountException();
        }
        //可以获取 username 信息，并做一些处理
        String username = (String)jwtUtil.decode(jwt).get("username");
        logger.info("鉴权用户 username：{}",username);
        return new SimpleAuthenticationInfo(jwt,jwt,"JwtRealm");
    }
}
