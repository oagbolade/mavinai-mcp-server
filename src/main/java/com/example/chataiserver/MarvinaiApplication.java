package com.example.chataiserver;

import com.example.chataiserver.tools.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MarvinaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarvinaiApplication.class, args);
	}

    @Bean
    ToolCallbackProvider toolCallbackProvider(
            CustomerTools customerTools,
            AccountTools accountTools,
            TransactionTools transactionTools,
            LoanTools loanTools,
            AccountRestrictionTools accountRestrictionTools,
            TransferTools transferTools,
            CardTools cardTools,
            KycTools kycTools,
            ReferenceTools referenceTools) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        customerTools,
                        accountTools,
                        transactionTools,
                        loanTools,
                        accountRestrictionTools,
                        transferTools,
                        cardTools,
                        kycTools,
                        referenceTools
                )
                .build();
    }
}
