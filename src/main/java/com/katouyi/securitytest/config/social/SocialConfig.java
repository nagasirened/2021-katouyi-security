package com.katouyi.securitytest.config.social;

import com.katouyi.securitytest.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * author: ZGF
 * context : 配置类
 */

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter{

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    @SuppressWarnings("all")
    private DataSource dataSource;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    /**
     * Spring-Social 加入到过滤器链中的配置，否则不生效
     * apply进去即可
     */
    @Bean
    public SpringSocialConfigurer katouyiSpringSocialConfigurer(){
        KatouyiSpringSocialconfigurer socialconfigurer = new KatouyiSpringSocialconfigurer();
        socialconfigurer.setFilterProcessUrl(securityProperties.getSocial().getFilterProcessUrl());
        return socialconfigurer;
    }

    /**
     * 主动重写此方法，定义结合数据库存储的Repository，默认是存在内存中
     *
     * SQL 如下，表名前面可以加前缀，如果加了，需要配置表名前缀是什么  connectionRepository.setTablePrefix()
     create table UserConnection (userId varchar(255) not null,
         providerId varchar(255) not null,
         providerUserId varchar(255),
         rank int not null,
         displayName varchar(255),
         profileUrl varchar(512),
         imageUrl varchar(512),
         accessToken varchar(512) not null,
         secret varchar(512),
         refreshToken varchar(512),
         expireTime bigint,
         primary key (userId, providerId, providerUserId));
     create unique index UserConnectionRank on UserConnection(userId, providerId, rank);
     *
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        /**
         * DataSource dataSource,                                   数据源
         * ConnectionFactoryLocator connectionFactoryLocator,       根据条件自定获取对应的ConnectionFactory
         * TextEncryptor textEncryptor                              加密解密的，下面的不加密
         */
        JdbcUsersConnectionRepository connectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        connectionRepository.setTablePrefix("i_");
        /**
         * 自定义的connectionSignUp，如果这个有的话，就不需要用户注册也能直接登录进去
         * 可以去这里面添加一个用户的系统用户信息，下次就可以不需要QQ登录了
         */
        if (connectionSignUp != null) {
            connectionRepository.setConnectionSignUp(connectionSignUp);
        }
        return connectionRepository;
    }


    /**
     * 此工具类的作用是，用户注册之后，绑定用户信息与social信息
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator){
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator));
    }

}
