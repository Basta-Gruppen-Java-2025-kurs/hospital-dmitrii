package se.dimage.hospital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
@PropertySource("classpath:application.yaml")
public class ConfigProperties {
    @Autowired
    private Environment env;

    public List<String> getEnvList() {
        List<String> ls = new ArrayList<>();
        if (env instanceof ConfigurableEnvironment cfgE) {
            for (org.springframework.core.env.PropertySource<?> ps : cfgE.getPropertySources()) {
                if (ps instanceof EnumerablePropertySource<?> eps) {
                    for(String key: eps.getPropertyNames()) {
                        ls.add(key + ": " + env.getProperty(key));
                    }
                }
            }
        }
        return ls;
    }

    public String getProperty(String key) {
        return env.getProperty(key);
    }

    public List<String> getEnvKeys() {
        if (env instanceof AbstractEnvironment absE) {
            return StreamSupport.stream(absE.getPropertySources().spliterator(), false)
                    .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                    .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
                    .flatMap(Arrays::<String>stream).toList();
        } else {
            return List.of();
        }
    }
}
