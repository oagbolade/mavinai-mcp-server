package com.example.chataiserver;

import com.example.chataiserver.tools.AccountTools;
import com.example.chataiserver.tools.CustomerTools;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MarvinaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarvinaiApplication.class, args);
	}

    @Bean
    public List<ToolCallback[]> toolCallbacks(
            CustomerTools customerTools,
            AccountTools accountTools) {

        return List.of(
                ToolCallbacks.from(customerTools),
                ToolCallbacks.from(accountTools)
        );
    }
}
