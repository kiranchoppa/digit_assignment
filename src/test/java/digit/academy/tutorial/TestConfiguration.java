package digit.academy.tutorial;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.egov.tracer.kafka.CustomKafkaTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfiguration {
    @Bean
    @SuppressWarnings("unchecked")
    public CustomKafkaTemplate<String, Object> kafkaTemplate() {
        return mock(CustomKafkaTemplate.class);
    }
}
