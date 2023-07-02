package com.github.openapilab.openapipostman.views;

import com.github.openapilab.openapipostman.service.FileService;
import com.github.openapilab.openapipostman.service.JsonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
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
        FileDownloadWrapper fileDownloadWrapper = new FileDownloadWrapper(
                new StreamResource("postman.json", () -> new ByteArrayInputStream(textArea.getValue().getBytes())));
        fileDownloadWrapper.wrapComponent(downloadButton);

        // Push button
        Icon pushIcon = VaadinIcon.SHARE.create();
        copyIcon.setTooltipText("Push to Postman with curl");

        Button pushButton = new Button("Push", pushIcon);
        pushButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        // open dialog on click
        pushButton.addClickListener(e -> {
            Dialog dialog = createPushDialog();
            add(dialog);
            dialog.open();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(clipboardHelper);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.add(fileDownloadWrapper);
        buttonLayout.add(pushButton);

        HorizontalLayout textAreaHeading = new HorizontalLayout();
        textAreaHeading.setWidthFull();
        textAreaHeading.setJustifyContentMode(JustifyContentMode.CENTER);
        textAreaHeading.setPadding(false);
        textAreaHeading.setSpacing(false);

        Div postmanJsonTitle = new Div();
        postmanJsonTitle.getElement().getStyle().set("font-size", "14px");
        postmanJsonTitle.setText("postman.json");
        textAreaHeading.add(postmanJsonTitle);

        layout.add(buttonLayout);
        layout.add(textAreaHeading);
        layout.add(textArea);

        Anchor homeAnchor = new Anchor("/", VaadinIcon.HOME.create());
        homeAnchor.getElement().setAttribute("router-ignore", "");

        add(layout);
        add(homeAnchor);
    }

    private Dialog createPushDialog() {
        // CURL command
        String command = "curl -X POST -d \"{\\\"collection\\\": $(<postman.json) }\" https://api.getpostman.com/collections --header 'Content-Type: application/json' --header 'X-API-Key: MY_POSTMAN_KEY'";

        Dialog dialog = new Dialog();
        dialog.setWidth("50%");
        dialog.setHeaderTitle("Push to Postman");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);

        Paragraph p1 = new Paragraph();
        p1.setText("Save the postman.json file and open the terminal in the same folder. \n" +
                "Copy the command below, replace with your Postman KEY and run with curl.");
        p1.getStyle().set("white-space", "pre-line");
        dialogLayout.add(p1);

        // Command copy button
        Icon commandCopyIcon = VaadinIcon.COPY.create();
        commandCopyIcon.setTooltipText("Copy");
        Button commandCopyButton = new Button("Copy", commandCopyIcon);
        commandCopyButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        ClipboardHelper commandClipboardHelper = new ClipboardHelper(command, commandCopyButton);

        HorizontalLayout buttonLayout = new HorizontalLayout(commandClipboardHelper);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        dialogLayout.add(buttonLayout);

        TextArea curlCommand = new TextArea();
        curlCommand.setReadOnly(true);
        curlCommand.setWidthFull();
        curlCommand.setMinHeight("100px");
        curlCommand.setValue(command);
        dialogLayout.add(curlCommand);

        dialog.add(dialogLayout);

        Button closeButton = new Button(new Icon("lumo", "cross"),
                (ev) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);

        return dialog;

    }

}
