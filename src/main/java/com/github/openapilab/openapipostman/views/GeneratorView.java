package com.github.openapilab.openapipostman.views;

import com.github.openapilab.openapipostman.ApplicationProperty;
import com.github.openapilab.openapipostman.exception.BaseException;
import com.github.openapilab.openapipostman.model.OpenApiSpec;
import com.github.openapilab.openapipostman.service.FileService;
import com.github.openapilab.openapipostman.service.GeneratorService;
import com.github.openapilab.openapipostman.service.JsonService;
import com.github.openapilab.openapipostman.service.YamlService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GeneratorView extends VerticalLayout {
    // constructor injection
    protected ApplicationProperty applicationProperty;
    protected FileService fileService;
    // field injection
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private JsonService jsonService;
    @Autowired
    private YamlService yamlService;

    public GeneratorView(ApplicationProperty applicationProperty, FileService fileService) {
        this.applicationProperty = applicationProperty;
        this.fileService = fileService;
    }

    String process(OpenApiSpec openApiSpec) throws Exception {

        if (jsonService.isJson(openApiSpec.getRaw())) {
            if (!jsonService.isValidJson(openApiSpec.getRaw())) {
                throw new BaseException("Invalid JSON");
            }

            // force OpenAPI version
            openApiSpec.setRaw(jsonService.setOpenApiVersion(openApiSpec.getRaw()));
        }

        if (yamlService.isValidYaml(openApiSpec.getRaw())) {
            if (!yamlService.isValidYaml(openApiSpec.getRaw())) {
                throw new BaseException("Invalid YAML");
            }
        }

        String input = fileService.save(openApiSpec.getRaw());
        String postmanFile = generatorService.generate(input);

        return postmanFile;

    }

    Notification setNotification(String message) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(5000);

        Div text = new Div(new Text(message));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> {
            notification.close();
        });

        HorizontalLayout messageLayout = new HorizontalLayout(text, closeButton);
        messageLayout.setAlignItems(Alignment.CENTER);

        notification.add(messageLayout);
        notification.open();

        return notification;
    }

    protected ReCaptcha initReCaptcha() {
        return new ReCaptcha(
                applicationProperty.getRecaptchaSiteKey(),
                applicationProperty.getRecaptchaSecretKey()
        );
    }


    protected Component footer() {
        Div footer = new Div();
        footer.getElement().getStyle().set("font-size", "14px");
        footer.setText("Made with ❤️, OpenAPI Generator and Vaadin");

        return footer;
    }
}
