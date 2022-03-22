package com.ssq.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class MyRedisConfig {


    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(stringRedisSerializer);
        return template;
    }

    @Bean
    @Primary
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(){
        ObjectMapper om = new ObjectMapper();
        // 解决查询缓存转换异常的问题
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 支持 jdk 1.8 日期   ---- start ---
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new ParameterNamesModule());
        // --end --
        GenericJackson2JsonRedisSerializer  genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
        return genericJackson2JsonRedisSerializer;
    }



    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 此项必须配置，否则会报java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
        //指定序列化输入类型 转换为json的类型 这样就不会转换回来的时候 不知道什么类型。
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);


        //用于对LocalTime进行序列化和反序列化
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));


        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    //自定义redisCacheManager 改变缓存机制
    //默认存在 RedisConnectionFactory redisConnectionFactory
    @Bean
    public RedisCacheManager myRedisCacheManager(RedisConnectionFactory redisConnectionFactory)
    {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        //创建序列化机制
//        Jackson2JsonRedisSerializer<String> keySerializer = new Jackson2JsonRedisSerializer<String>(String.class);
//        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

//        //创建序列化机制
//        RedisSerializationContext.SerializationPair<String> keyPair = RedisSerializationContext.SerializationPair
//                .fromSerializer(keySerializer);
        RedisSerializationContext.SerializationPair<Object> valuePair = RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer());

        //创建序列化配置 那些usePrexif都是在这里配置的
        RedisCacheConfiguration redisCacheConfiguration =  RedisCacheConfiguration.defaultCacheConfig().
                serializeValuesWith(valuePair);

        //配置完成
        RedisCacheManager redisCacheManager=new  RedisCacheManager(redisCacheWriter,redisCacheConfiguration);
        return  redisCacheManager;
    }

}
