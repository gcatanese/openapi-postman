package com.github.openapilab.openapipostman.views;

import com.github.openapilab.openapipostman.service.FileService;
import com.github.openapilab.openapipostman.service.JsonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.olli.ClipboardHelper;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Route(value = "postmanCollection", layout = MainLayout.class)
public class PostmanCollectionView extends VerticalLayout {

    private FileService fileService;

    private JsonService jsonService;

    private String postmanFilePath;

    @Autowired
    public PostmanCollectionView(FileService fileService, JsonService jsonService) throws IOException {

        this.fileService = fileService;
        this.jsonService = jsonService;

        this.postmanFilePath = VaadinService.getCurrent().getContext().getAttribute(String.class);

        String content = fileService.read(this.postmanFilePath);
        content = jsonService.prettifyJson(content);

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(false);

        TextArea textArea = new TextArea();
        textArea.setReadOnly(true);
        textArea.setWidthFull();
        textArea.setMinHeight("300px");
        textArea.setMaxHeight("500px");
        textArea.setValue(content);

        // Copy button
        Icon copyIcon = VaadinIcon.COPY.create();
        copyIcon.setTooltipText("Copy");

        Button copyButton = new Button("Copy", copyIcon);
        copyButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        ClipboardHelper clipboardHelper = new ClipboardHelper(textArea.getValue(), copyButton);
        // Save button
        Icon downloadIcon = VaadinIcon.DOWNLOAD.create();
        downloadIcon.setTooltipText("Download");

        Button downloadButton = new Button("Save", downloadIcon);
        downloadButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                new StreamResource("postman.json", () -> new ByteArrayInputStream(textArea.getValue().getBytes())));
        buttonWrapper.wrapComponent(downloadButton);

        HorizontalLayout buttonLayout = new HorizontalLayout(clipboardHelper);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.add(buttonWrapper);

        layout.add(buttonLayout);
        layout.add(textArea);

        Icon homeIcon = VaadinIcon.HOME.create();

        Button homeButton = new Button("Home", homeIcon);
        homeButton.addClickListener(ev -> {
                    homeButton.getElement().executeJs("window.location.href = '/' ");
                });

        add(layout);
        add(homeButton);
    }

}
