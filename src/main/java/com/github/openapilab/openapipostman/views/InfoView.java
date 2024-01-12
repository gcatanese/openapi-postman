package com.github.openapilab.openapipostman.views;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "info", layout = MainLayout.class)
public class InfoView extends VerticalLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoView.class);


    @Autowired
    public InfoView() throws Exception {


        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setPadding(false);

        H2 title = new H2("Convert OpenAPI files into Postman collections");
        add(title);

        Paragraph p = new Paragraph();
        p.setText("Importing OpenAPI files is supported by Postman but this is a lot better because:");

        UnorderedList unorderedList = new UnorderedList(
                new ListItem("convert placeholders (i.e. {{MY_VAR}}) found in OpenAPI examples into Postman variables"),
                new ListItem(("create a Postman request for each OpenAPI (request) example: multiple examples generates different requests for the same endpoint")),
                new ListItem("define the preferred authorisation method (basic auth, API key)"),
                new ListItem("create a Postman variable for the API key")
        );
        p.add(unorderedList);

        add(p);

    }
}
