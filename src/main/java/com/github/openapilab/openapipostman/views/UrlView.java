package com.github.openapilab.openapipostman.views;

import com.github.openapilab.openapipostman.ApplicationProperty;
import com.github.openapilab.openapipostman.exception.BaseException;
import com.github.openapilab.openapipostman.model.OpenApiSpec;
import com.github.openapilab.openapipostman.service.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="", layout = MainLayout.class)
public class UrlView extends VerticalLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlView.class);

    // constructor injection
    private ApplicationProperty applicationProperty;
    private FileService fileService;

    // field injection
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private UrlService urlService;
    @Autowired
    private JsonService jsonService;
    @Autowired
    private YamlService yamlService;

    @Autowired
    public UrlView(ApplicationProperty applicationProperty, FileService fileService) throws Exception {

        this.applicationProperty = applicationProperty;
        this.fileService = fileService;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setPadding(false);

        layout.add(url());

        add(layout);
        add(footer());

    }

    String process(OpenApiSpec openApiSpec) throws Exception {

        if(jsonService.isJson(openApiSpec.getRaw())) {
            if(!jsonService.isValidJson(openApiSpec.getRaw())) {
                throw new BaseException("Invalid JSON");
            }

            // force OpenAPI version
            openApiSpec.setRaw(jsonService.setOpenApiVersion(openApiSpec.getRaw()));
        }

        if(yamlService.isValidYaml(openApiSpec.getRaw())) {
            if(!yamlService.isValidYaml(openApiSpec.getRaw())) {
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

    private Component url() throws Exception {
        Binder<OpenApiSpec> binder = new Binder<>(OpenApiSpec.class);


        VerticalLayout layout = new VerticalLayout();
        TextField textField = new TextField();
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.setPlaceholder("Enter URL of the OpenAPI specification");

        binder.forField(textField).bind(OpenApiSpec::getUrl, OpenApiSpec::setUrl);

        Button submit = new Button("Submit");
        submit.setId("submitBtn");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(submit);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        FormLayout formLayout = new FormLayout(textField, buttonLayout);
        formLayout.setId("url");
        formLayout.setColspan(textField, 2);
        formLayout.setColspan(buttonLayout, 2);

        ReCaptcha reCaptcha = initReCaptcha();
        HorizontalLayout captchaLayout = new HorizontalLayout();
        captchaLayout.setWidthFull();
        captchaLayout.setJustifyContentMode(JustifyContentMode.END);
        captchaLayout.add(reCaptcha);

        submit.addClickListener( e -> {

            OpenApiSpec openApiSpec = new OpenApiSpec();
            try {
                binder.writeBean(openApiSpec);

                if(openApiSpec.getUrl() != null && !openApiSpec.getUrl().trim().isBlank()) {

                    if(!reCaptcha.isValid()) {
                        LOGGER.warn("Invalid reCaptcha challenge");
                        throw new BaseException("Invalid reCaptcha challenge: please try again");
                    }

                    String content = urlService.fetchUrl(openApiSpec.getUrl());
                    openApiSpec.setRaw(content);

                    String postmanFile = process(openApiSpec);

                    VaadinService.getCurrent().getContext().setAttribute(String.class, postmanFile);
                    submit.getUI().get().getCurrent().navigate(PostmanCollectionView.class);
                }

            } catch (BaseException baseException) {
                LOGGER.warn(baseException.getMessage());
                layout.add(setNotification(baseException.getMessage()));
            } catch (Exception ex) {
                LOGGER.warn(ex.getMessage(), ex);
                layout.add(setNotification("An error has occurred: check the specification and try again"));
            }
        });

        layout.add(formLayout);
        layout.add(captchaLayout);

        return layout;
    }

    private ReCaptcha initReCaptcha() {
        return new ReCaptcha(
                applicationProperty.getRecaptchaSiteKey(),
                applicationProperty.getRecaptchaSecretKey()
        );
    }

    private Component footer() {
        Div footer = new Div();
        footer.getElement().getStyle().set("font-size", "14px");
        footer.setText("Made with ❤️, OpenAPI Generator and Vaadin");

        return footer;
    }
}
