package com.github.openapilab.openapipostman.views.helpers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PageHelper {

    public static Component pageHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMaxHeight(4, Unit.PERCENTAGE);
        layout.setPadding(false);


        H1 title = new H1("OpenAPI to Postman");
        layout.add(title);

        return layout;
    }
}
