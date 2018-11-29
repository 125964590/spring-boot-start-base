package con.ht.base.start.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zhengyi
 * @date 11/29/18 4:29 PM
 **/
public class RedisConfig {

    @Bean
    public StringRedisTemplate niubilityRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        StringRedisTemplate niubilityRedisTemplate = new StringRedisTemplate();
        niubilityRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        return niubilityRedisTemplate;
    }
}