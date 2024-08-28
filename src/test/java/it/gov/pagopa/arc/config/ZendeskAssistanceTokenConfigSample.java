package it.gov.pagopa.arc.config;

public class ZendeskAssistanceTokenConfigSample {

    private static final String PRIVATE_KEY= "qznupla7kr75ljz2nn5p23npxhj16zm3jfrjfggcn2wficgolf";

    public ZendeskAssistanceTokenConfig getCorrectConfiguration(){
        return new ZendeskAssistanceTokenConfig("JWT","help-center-url.com", new ZendeskAssistanceTokenConfig.AssistanceToken("prod-arc", "_hc_arc", PRIVATE_KEY));
    }
}
