package br.com.fiap.feedback_api.function;

import br.com.fiap.feedback_api.FeedbackApiApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public final class SpringContextProvider {

    private static volatile ConfigurableApplicationContext context;

    private SpringContextProvider() {
    }

    public static ConfigurableApplicationContext getContext() {

        if (context == null) {
            synchronized (SpringContextProvider.class) {
                if (context == null) {
                    context = new SpringApplication(
                            FeedbackApiApplication.class
                    ).run("--spring.main.web-application-type=none");
                }
            }
        }

        return context;
    }
}