package ps.demo.commonlibx.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ps.demo.commonlibx.common.ProjConstant;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Configuration
public class DateFormatConfig {

//    @Bean
//    @Primary
//    public ObjectMapper primaryObjectMapper() {
//        return JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                .addModule(new JavaTimeModule()).build();
//    }

    @Bean
    public ObjectMapper getObjectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Date.class, new MyJsonDeserializer());
        simpleModule.addSerializer(Date.class, new MyJsonSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);
        objectMapper.registerModule(new JavaTimeModule()); //JDK8 Date Time
        return objectMapper;
    }

    public static class MyJsonDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String source = p.getText().trim();
            try {
                return ProjConstant.parseToDate(source);
            } catch (Exception e) {
                log.error("MyJsonDeserializer deserialize error {}", e.getMessage(), e);
            }
            return null;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyJsonSerializer extends JsonSerializer<Date> implements ContextualSerializer {
        private JsonFormat jsonFormat;

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
                return;
            }
            String pattern = jsonFormat == null ? ProjConstant.DATE_FORMAT_STR_ISO8601 : jsonFormat.pattern();
            gen.writeString(ProjConstant.formatToString(value, pattern));
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
            if (property != null) {
                JsonFormat ann = property.getAnnotation(JsonFormat.class);
                if (ann != null) {
                    return new MyJsonSerializer(ann);
                }
            }
            return this;
        }
    }

}
