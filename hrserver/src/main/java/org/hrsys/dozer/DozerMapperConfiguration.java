package org.hrsys.dozer;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class DozerMapperConfiguration {

    /**
     * Dozer 配置
     *
     * @param resources Dozer resources
     * @return DozerBeanMapperFactoryBean
     */
    @Bean
    public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean(@Value("classpath*:dozer/*.xml") Resource[] resources) {
        final DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        dozerBeanMapperFactoryBean.setMappingFiles(resources);
        return dozerBeanMapperFactoryBean;
    }

    /**
     * DozerConverter
     */
    @Bean
    public DozerConverter dozerConverter() {
        return new DefaultDozerConverter();
    }
}
