package com.github.openapilab.openapipostman.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("OpenAPI to Postman");
        logo.addClassNames(
                LumoUtility.FontSize.XXLARGE,
                LumoUtility.Margin.MEDIUM);

        var header = new HorizontalLayout(new DrawerToggle(), logo );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private void createDrawer() {
        Anchor urlMenu = new Anchor("/", "From URL");
        urlMenu.getElement().setAttribute("router-ignore", "");
        Anchor urlRawText = new Anchor("/rawtext", "From raw text");
        urlRawText.getElement().setAttribute("router-ignore", "");
        addToDrawer(new VerticalLayout(
                urlMenu, urlRawText
        ));
        setDrawerOpened(false);
    }
}
