package com.FYP.Fleet.Whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WhatsAppPayload {

    @JsonProperty("messaging_product")
    private final String messagingProduct = "whatsapp";

    @JsonProperty("recipient_type")
    private final String recipientType = "individual";

    private String to;
    private final String type = "template";
    private Template template;

    public WhatsAppPayload(String recipientPhone, String templateName, String languageCode, List<String> bodyValues) {
        // Strip out non-numeric characters to prevent Meta delivery failures
        String cleanPhone = recipientPhone.replaceAll("[^0-9]", "");

        if (cleanPhone.length() == 10) {
            this.to = "91" + cleanPhone;
        } else {
            this.to = cleanPhone;
        }

        // Transform the raw strings array into separate sequential positional parameter wrappers
        List<Parameter> parameters = bodyValues.stream()
                .map(val -> new Parameter("text", val))
                .toList();

        // Create the standard "body" type section structure
        Component bodyComponent = new Component("body", parameters);

        // Bind the inner template block
        this.template = new Template(templateName, new Language(languageCode), List.of(bodyComponent));
    }

    @Data
    public static class Template{
        private final String name;
        private final Language language;
        private final List<Component> components;

        public Template(String templateName, Language language, List<Component> bodyComponent) {
            this.name = templateName;
            this.language = language;
            this.components = bodyComponent;
        }
    }

    @Data
    public static class Component{
        private final String type; // expected value: "body"
        private final List<Parameter> parameters;
    }

    @Data
    public static class Language{
        private final String code;
    }

    @Data
    public static class Parameter{
        private final String type;
        private final String text;

        public Parameter(String type, String val) {
            this.type = type;
            this.text = val;
        }
    }
}


