package com.solutions.todolist.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelConfiguration {

    @Bean
    OtlpHttpSpanExporter otlpHttpSpanExporter() {
        return OtlpHttpSpanExporter.builder()
                .setEndpoint("http://localhost:4318/v1/traces")
                .build();
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") final String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
