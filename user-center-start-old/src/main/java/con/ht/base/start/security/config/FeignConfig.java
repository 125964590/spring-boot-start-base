package con.ht.base.start.security.config;

import con.ht.base.start.security.module.properties.UserCenterProperties;
import con.ht.base.start.security.service.AuthServer;
import feign.Feign;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author zhengyi
 * @date 11/20/18 7:39 PM
 **/
public class FeignConfig {

    private UserCenterProperties userCenterProperties;

    public FeignConfig(UserCenterProperties userCenterProperties) {
        this.userCenterProperties = userCenterProperties;
    }

    public AuthServer authService() {
        return Feign.builder()
                .options(new Request.Options())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(AuthServer.class, userCenterProperties.getUrl());
    }
}