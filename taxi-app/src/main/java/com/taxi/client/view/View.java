package com.taxi.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import com.taxi.client.presenter.Presenter;
import com.taxi.client.view.dialog.Login;
import com.taxi.client.view.dialog.NumberDialog;
import com.taxi.client.view.dialog.OrderDialog;
import com.taxi.client.view.dialog.Registration;
import com.taxi.client.view.map.Map;

import javax.inject.Inject;
import java.util.ArrayList;

public class View extends Composite {

    interface ViewUiBinder extends UiBinder<Widget, View> {
    }

    private static ViewUiBinder ourUiBinder = GWT.create(ViewUiBinder.class);

    @UiField
    HTMLPanel root;

    @UiField
    HorizontalPanel header;

    @UiField
    HorizontalPanel contentPanel;

    private Map map;
    private Presenter presenter;
    private EventBus eventBus;
    private OrderDialog orderDialog;
    private NumberDialog numberDialog;
    private Login login;
    private Registration registration;

    @Inject
    public View(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    private void loadMapApi() {
        boolean sensor = true;

        ArrayList<LoadApi.LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
        loadLibraries.add(LoadApi.LoadLibrary.ADSENSE);
        loadLibraries.add(LoadApi.LoadLibrary.DRAWING);
        loadLibraries.add(LoadApi.LoadLibrary.GEOMETRY);
        loadLibraries.add(LoadApi.LoadLibrary.PANORAMIO);
        loadLibraries.add(LoadApi.LoadLibrary.PLACES);
        loadLibraries.add(LoadApi.LoadLibrary.WEATHER);
        loadLibraries.add(LoadApi.LoadLibrary.VISUALIZATION);

        Runnable onLoad = new Runnable() {
            @Override
            public void run() {
                map = new Map();
                map.setPresenter(presenter);
                contentPanel.add(map);
                prepareMap();
            }
        };
        LoadApi.go(onLoad, loadLibraries, sensor);
    }

    public void createUi() {
        initWidget(ourUiBinder.createAndBindUi(this));
        root.getElement().getStyle().setMarginLeft(-8, Style.Unit.PX);
        root.getElement().getStyle().setMarginTop(-8, Style.Unit.PX);
        root.getElement().getStyle().setMarginBottom(8, Style.Unit.PX);
        root.getElement().getStyle().setMarginRight(-8, Style.Unit.PX);

        header.getElement().getStyle().setWidth(Window.getClientWidth(), Style.Unit.PX);
        header.getElement().getStyle().setHeight(Window.getClientHeight() / 15, Style.Unit.PX);

        login = new Login();
        login.show();

        login.getLoginButton().addClickHandler(event -> {
            login.hide();
            loadMapApi();
            orderDialog = new OrderDialog();
            orderDialog.show();
            numberDialog = new NumberDialog();
            numberDialog.show();
        });

        login.getRegistrationButton().addClickHandler(event -> {
            login.hide();
            registration = new Registration();
            registration.show();
        });

        registration.getUserType().addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                boolean isVisible = registration.getUserType().getSelectedItemText().equals("Водитель");
                registration.getCarNumberLabel().setVisible(isVisible);
                registration.getCarNumber().setVisible(isVisible);
            }
        });

        RootPanel.get("root").add(this);
    }

    private void prepareMap() {
        if (map != null) {
            map.getMapWidget().addClickHandler(clickMapEvent -> {

            });
        }
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}