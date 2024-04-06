package com.qnenet.qne.ui.viewsmain;

import com.qnenet.qne.system.constants.QRoutes;
import com.qnenet.qne.security.SecurityService;
import com.qnenet.qne.system.api.QView;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteData;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MainLayout extends AppLayout {

//    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;
    @Autowired
    SecurityService securityService;

    private final HorizontalLayout workToolbar;
    private final Button homeBtn;
    //    private Dialog helpDialog;
//    private QSessionInfo sessionInfo;
    private MainLayout INSTANCE;
    private Component content;

    private SubMenu mainSubMenu;

    //    private MenuBar quickMenuBar;
//    private Button quickMenuBtn;
//    private MenuItem quickMenuItem;
//    private SubMenu quickSubMenu;
    private Registration routesListener;
    //    private Button homeBtn;
//    private Button logoutBtn;
//    private List<String> currentRouteList;
    private MenuBar menuBar;
    private List<RouteData> allRoutes = new ArrayList<>();
    private MenuItem menuIconItem;
    private Icon menuIcon;
    private int currentSlideIdx;
    private boolean hasLoggedInUser;
//    private Carousel helpCarousel;
//    private Dialog quickDialog;
//    private QHelpDisplay helpDisplay;
//    private String helpKey;
//    private String prevHelpKey;
    //    private QAllowedViewsInfo allowedViewsInfo;
//    private ArrayList<String> helpHistory = new ArrayList<>();


///////////////////////////////////////////////////////////////////////////////////////////////////
////////// Constructor ////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public MainLayout(AccessAnnotationChecker accessChecker) {
//    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
//        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        INSTANCE = this;

        hasLoggedInUser = false;

        allRoutes = RouteConfiguration.forApplicationScope().getAvailableRoutes();

//        helpDisplay = new QHelpDisplay();
//        if (sessionInfo.newSystemState != null) {  // is new system
//           if (!sessionInfo.newSystemState.viewNewSystemInstalled) {
//               sessionInfo.featuresManager.installArtifacts("qviewnewsystem.jar");
//               sessionInfo.newSystemState.viewNewSystemInstalled = true;
//               sessionInfo.node.saveNodeInfo();
//           }
//        }

//        allowedViewsInfo = sessionInfo.nodeAddons.getAllowedViewsMap().get(sessionInfo.allowedViewsName);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setSpacing(false);
        toolbar.setWidthFull();
        toolbar.setHeight("40px");

        HorizontalLayout menuBarLayout = new HorizontalLayout();
        menuBarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        menuBarLayout.setHeightFull();

        workToolbar = new HorizontalLayout();
        workToolbar.setWidthFull();
        workToolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        workToolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        workToolbar.addClickListener(click -> {
            closeHelp();
        });
//        Span span = new Span("Work Toolbar");
//        workToolbar.add(span);

        homeBtn = new Button(new Icon(VaadinIcon.HOME));
        homeBtn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        homeBtn.getElement().setAttribute("aria-label", "Home");
        homeBtn.addClickListener(click -> {
            closeHelp();
            UI.getCurrent().navigate(QRoutes.PUBLIC_HOME);
        });

        Button quickBtn = new Button(new Icon(VaadinIcon.BOLT));
        quickBtn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        quickBtn.getElement().setAttribute("aria-label", "Quick");
        quickBtn.addClickListener(click -> {
            closeHelp();
            doQuick();
        });

//        Button exitBtn = new Button("Log out " + u, e -> {
//            securityService.logout();
//        }); // <2>


//        Button exitBtn = new Button(new Icon(VaadinIcon.EXIT));
        Button exitBtn = new Button(new Icon(VaadinIcon.USER));
//        Button exitBtn = new Button(new Icon(VaadinIcon.USER_CHECK));
        exitBtn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        exitBtn.getElement().setAttribute("aria-label", "Exit");
        exitBtn.addClickListener(click -> {
//            openLoginOverlay();



//            Button login = new Button("Log in");
//            login.addClickListener(event -> loginOverlay.setOpened(true));

//            if (hasLoggedInUser) { // logged out
//
//                exitBtn.setIcon(new Icon(VaadinIcon.USER));
//
//                closeHelp();
//                securityService.logout();
//                UI.getCurrent().navigate(QRoutes.PUBLIC_HOME);
//                hasLoggedInUser = false;
//
//            } else {
//
//                UI.getCurrent().navigate("login");
//                exitBtn.setIcon(new Icon(VaadinIcon.USER_CHECK));
//                hasLoggedInUser = true;
//                UI.getCurrent().navigate(QRoutes.LOGIN);
//
//
//            }
        });


        Button helpBtn = new Button(new Icon(VaadinIcon.QUESTION));
        helpBtn.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        helpBtn.getElement().setAttribute("aria-label", "Help");

        helpBtn.addClickListener(click -> {
//            if (helpCarousel == null) {
////                helpDialog = new Dialog();
////                helpDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
////                helpDialog.setModal(false);
////                helpDialog.setHeight("200px");
////                helpDialog.setWidth("600px");
//                doHelp();
//            } else {
//                closeHelp();
//            }
        });


        menuBar = new MenuBar();
        toolbar.add(homeBtn, menuBar, quickBtn, workToolbar, helpBtn, exitBtn);

        addToNavbar(true, toolbar);
        updateTopMenu();
        updateMainMenu();
//        updateQuick(sessionInfo.qOSGiServlet.getQuickMap());
        //        QNode n1Node = sessionInfo.network.getNodeByLoginName("n1");
//        addToNavbar(true, createTopMenu());


    }

//    private void openLoginOverlay() {
//        LoginOverlay loginOverlay = new LoginOverlay();
//        loginOverlay.setOpened(true);
//        INSTANCE.add(loginOverlay);
//    }
//

//    private void doHelp() {
//        String helpKey = sessionInfo.helpKey;
//        ArrayList<VerticalLayout> layouts = sessionInfo.helpMap.get(helpKey);
//        doHelp(layouts);
//    }
///////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Menu //////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateMenus() {
        updateTopMenu();
        updateMainMenu();
    }


    public void updateTopMenu() {
        menuBar.removeAll();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
        menuBar.setHeight("40px");

//// Home
//        MenuItem homeMenuItem = createIconItem(menuBar, VaadinIcon.HOME, null, null, false);
//        homeMenuItem.addClickListener(click -> {
//            getCurrentRoute();
//
////            Notification home_clicked = Notification.show("Home Clicked", 1000, Notification.Position.MIDDLE);
////            home_clicked.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
//            UI.getCurrent().navigate(QRoutes.ROUTE_HOME);
//        });

// Menu
        menuIconItem = createIconItem(menuBar, VaadinIcon.MENU, null, null, false);
        mainSubMenu = menuIconItem.getSubMenu();


//// Quick
//        MenuItem quickMenuItem = createIconItem(menuBar, VaadinIcon.BOLT, null, null, false);
//        quickSubMenu = quickMenuItem.getSubMenu();
//        updateQuick();
////        quickMenuItem.addClickListener(click -> {
////            updateQuick();
////        });

//// Exit
//        MenuItem logoutMenuItem = createIconItem(menuBar, VaadinIcon.EXIT, null, null, false);
//        logoutMenuItem.addClickListener(click -> {
//            UI.getCurrent().navigate(QRoutes.ROUTE_ENTER);
//        });

    }

    private void getCurrentRoute() {
        UI.getCurrent().getPage().fetchCurrentURL(System.out::println);

    }


    public void updateMainMenu() {
        mainSubMenu.removeAll();
//        closeHelp();

//        ArrayList<String> allowedRoutes = null;
//        if (sessionInfo.isLoggedIn) {
////            allowedRoutes = allowedViewsInfo.privateRoutesList;
//        } else {
////            allowedRoutes = allowedViewsInfo.publicRoutesList;
//        }
//        if (allowedRoutes == null || allowedRoutes.isEmpty()) return;

        Map<String, SubMenu> map = new ConcurrentHashMap<>();

//        ArrayList<String> allowedRoutesoutes = allRoutes;

//        if (allowedRoutes.isEmpty()) {
//            Notification no_views_available = Notification.show("No Views Available", 1000, Notification.Position.MIDDLE);
////            no_views_available.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
//            return;
//        }

//////////////////////////////////////////////////////////////////////////

        for (RouteData routeData : allRoutes) {
            if (routeData.getTemplate().equals("login")) continue;
            if (!accessChecker.hasAccess(routeData.getNavigationTarget())) continue;
            String[] routeParts = routeData.getTemplate().split("/");

            // setup import contacts

///////// One /////////////////////////////////////////////////////////////////

            SubMenu parentSubMenu = mainSubMenu;
            String setupText = routeParts[0];                        // item text = Setup
            SubMenu setupSubMenu = map.get(setupText);
            MenuItem setupMenuItem = null;
            if (setupSubMenu == null) {
                setupMenuItem = parentSubMenu.addItem(setupText);          // menuItem   = Setup
                setupSubMenu = setupMenuItem.getSubMenu();                 // submenu    = Setup submenu
                map.put(setupText, setupSubMenu);                      // setup submenu to map
            }
            if (routeParts.length == 1) {
                gotoRoute(routeData.getTemplate(), setupMenuItem);
                continue;
            }

///////// Two /////////////////////////////////////////////////////////////////

            parentSubMenu = setupSubMenu;
            String setupImportRouteStr = routeParts[0] + "/" + routeParts[1]; // Setup/Import
            String importText = routeParts[1];                           // Import
            setupText = routeParts[0];                                   // Setup
            MenuItem setupImportMenuItem = null;
            SubMenu setupImportSubMenu = map.get(setupImportRouteStr);
            if (setupImportSubMenu == null) {
                setupImportMenuItem = parentSubMenu.addItem(importText);
                setupImportSubMenu = setupImportMenuItem.getSubMenu();
                map.put(setupImportRouteStr, setupImportSubMenu);
            }

            if (routeParts.length == 2) {
                gotoRoute(routeData.getTemplate(), setupImportMenuItem);
                continue;
            }

///////// Three ///////////////////////////////////////////////////////////////

            parentSubMenu = setupImportSubMenu;
            String setupImportContactsRouteStr = routeParts[0] + "/" + routeParts[1] + "/" + routeParts[2]; // Setup/Import/Contacts
            String contactsText = routeParts[2];                               // Contacts
            MenuItem contactsMenuItem = null;
            SubMenu setupImportContactsSubMenu = map.get(setupImportContactsRouteStr);
            if (setupImportContactsSubMenu == null) {
                contactsMenuItem = parentSubMenu.addItem(contactsText);
                SubMenu contactsSubMenu = contactsMenuItem.getSubMenu();
                map.put(setupImportContactsRouteStr, contactsSubMenu);
            }
            if (routeParts.length == 3) {
                gotoRoute(routeData.getTemplate(), contactsMenuItem);
                continue;
            }
        

        }
    }


//    private MenuItem createIconItem(MenuBar menu, VaadinIcon iconName, String ariaLabel) {
//        menuIcon = new Icon(iconName);
//        MenuItem item = menu.addItem(menuIcon);
//        item.getElement().setAttribute("aria-label", ariaLabel);
//        return item;
//    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel) {
        return createIconItem(menu, iconName, label, ariaLabel, false);
    }

    private MenuItem createIconItem(HasMenuItems menu, VaadinIcon iconName, String label, String ariaLabel, boolean isChild) {
        Icon icon = new Icon(iconName);
        icon.addClickListener(click -> {
            closeHelp();
        });

        if (isChild) {
            icon.getStyle().set("width", "var(--lumo-icon-size-s)");
            icon.getStyle().set("height", "var(--lumo-icon-size-s)");
            icon.getStyle().set("marginRight", "var(--lumo-space-s)");
        }

        MenuItem item = menu.addItem(icon, e -> {
        });

        if (ariaLabel != null) {
            item.getElement().setAttribute("aria-label", ariaLabel);
        }

        if (label != null) {
            item.add(new Text(label));
        }

        return item;
    }

    private void gotoRoute(String route, MenuItem setupMenuItem) {
        setupMenuItem.addClickListener(click -> UI.getCurrent().navigate(route));
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// MBus /////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

//    @Handler
//    public void handle(QMBusViewCreated viewCreated){
//        UI.getCurrent().access(() -> {
//            Component view = (Component) viewCreated.view;
//            System.out.println("View Created -> " + view.getClass().getSimpleName());
//        });
//
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Attach ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//        RouteRegistry registry = attachEvent.getUI().getInternals().getRouter().getRegistry();
//        List<RouteData> registeredRoutes = registry.getRegisteredRoutes();
//        for (RouteData data : registeredRoutes) {
//            String route = data.getTemplate();
//            if (route.isEmpty()) continue;
////            if (route.contains("Home") && sessionInfo.node.getNodeInfo().newSystemState != null) continue;
//            if (route.contains("Home")) continue;
//            if (route.contains("unknown")) continue;
//            if (route.contains(":")) {
//                System.out.println(route);
//                List<RouteAliasData> routeAliases = data.getRouteAliases();
//                route = routeAliases.get(0).getTemplate();
//                System.out.println(route);
//            }
//            allRoutes.add(route);
//        }
//
//        UI ui = attachEvent.getUI();
//        setupMenu(attachEvent.getUI());
//        routesListener = registry.addRoutesChangeListener(event -> ui.access(() -> {
//            List<String> addedURLs = event.getAddedURLs();
//            handleAddedRoutes(addedURLs);
//            allRoutes.addAll(addedURLs);
//
//            List<String> removedURLs = event.getRemovedURLs();
//            allRoutes.removeAll(removedURLs);
//            updateMainMenu();
//            handleRemovedRoutes(removedURLs);
//
//
//            //            List<String> addedURLs = event.getAddedURLs();
////            List<String> removedURLs = event.getRemovedURLs();
////            setupMenu(ui);
////            if (!isCurrentRouteExists(ui)) {
////                ui.navigate(DeregisteredRouteView.class,
////                        ui.getInternals().getActiveViewLocation()
////                                .getPath());
////            }
//        }));
//    }
//
//
//    private void handleAddedRoutes(List<String> addedURLs) {
//    }
//
//
//    private void handleRemovedRoutes(List<String> removedURLs) {
////        if (sessionInfo.currentView == null) return;
////        sessionInfo.currentView.doBeforeLeave();
////        UI.getCurrent().navigate(QRoutes.ROUTE_HOME);
//    }


    private void setupMenu(UI ui) {
        updateMainMenu();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (routesListener != null) {
            routesListener.remove();
            routesListener = null;
        }
    }

    private boolean isCurrentRouteExists(UI ui) {
        Location currentLocation = ui.getInternals().getActiveViewLocation();

        List<String> segments = currentLocation.getSegments();
        String path = segments.get(0);
        if (!segments.isEmpty()) {
            segments = segments.subList(1, segments.size());
        }

        RouteConfiguration configuration = RouteConfiguration.forRegistry(ui.getInternals().getRouter().getRegistry());
        return configuration.getRoute(path, segments).isPresent();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Navigation ///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void afterNavigation() {
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//////////// Getters & Setters ////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public HorizontalLayout getWorkToolbar() {
        return workToolbar;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Quick Dialogs /////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

//    private void showQuick() {
//        if (quickList.isEmpty()) return;
//        for (String quickName : quickList) {
//            quickSubMenu.addItem(quickName, e -> {
//                Dialog dialog = quickMap.get(quickName).getDialog();
//                dialog.open();
//            });
//        }
//    }

    public void doQuick() {

//        ConcurrentHashMap<String, QuickItem> quickMap = sessionInfo.fixedVaadinServlet.getQuickMap();
//        if (quickMap.isEmpty()) return;
//
////        Dialog quickDialog = new Dialog();
////        quickDialog.setModal(true);
////        quickDialog.setHeaderTitle("Quick");
//
//        Dialog dialog = new Dialog();
//        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
//        dialog.setWidth("300px");
//        dialog.setHeight("100%");
//
//        dialog.setModal(true);
//        dialog.setDraggable(false);
//        dialog.setHeaderTitle("Quick");
//
//        VerticalLayout layout = new VerticalLayout();
//        layout.setMargin(false);
//        layout.setSpacing(false);
//        layout.setPadding(false);
//
//        ArrayList<String> quickList = new ArrayList(quickMap.keySet());
//        Collections.sort(quickList);
//
////        VerticalLayout quickListBox = new VerticalLayout();
////        quickListBox.setWidth("100px");
//        for (int i = 0; i < quickList.size(); i++) {
//            String selectedItemStr = quickList.get(i);
//            Button btn = new Button(selectedItemStr);
//            btn.setWidth("80%");
//            btn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
//            btn.addClickListener(click -> {
//                dialog.close();
//                QuickItem quickItem = quickMap.get(selectedItemStr);
//                Dialog quickItemDialog = quickItem.getDialog();
//                quickItemDialog.open();
//            });
//            layout.add(btn);
//        }
////        RadioButtonGroup<String> rbg = new RadioButtonGroup<>();
////        rbg.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
////        rbg.setItems(quickList);
////
////        rbg.addValueChangeListener(event -> {
////            quickDialog.close();
////            String selectedItemName = event.getValue();
////            QuickItem quickItem = quickMap.get(selectedItemName);
////            Dialog quickItemDialog = quickItem.getDialog();
////            quickItemDialog.open();
////        });
////
////        layout.add(rbg);
//
//        dialog.add(layout);
//
//        dialog.open();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////
/////// Help //////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

    public void doHelp() {
//        QView currentView = sessionInfo.currentView;
//        if (currentView == null) return;
//
//
//        ArrayList<QSlideContent> slidesContentList = sessionInfo.help.getHelpSlidesContentsList(currentView);
//
//        if (currentView == null || slidesContentList == null || slidesContentList.isEmpty()) {
//
//            slidesContentList = new ArrayList<>();
//
//            QSlideContent slideContent = new QSlideContent("<H1>Help not available. 1</H1>", "#B0E0E0");
//            slidesContentList.add(slideContent);
//
//            slideContent = new QSlideContent("<p>Help not available. 2</p>", "#B0E0E3");
//            slidesContentList.add(slideContent);
//
//            slideContent = new QSlideContent("<p>Help not available. 3</p>", "#B0E0E6");
//            slidesContentList.add(slideContent);
//
//            sessionInfo.help.setHelpSlidesContentsList(currentView, slidesContentList);
//        }
//
//
//        Slide[] slides = new Slide[slidesContentList.size()];
//        for (int i = 0; i < slidesContentList.size(); i++) {
//            QHelpSlideEditor helpSlide = new QHelpSlideEditor(slidesContentList.get(i));
//            slides[i] = new Slide(helpSlide);
//        }
//
//        helpCarousel = new Carousel();
//        helpCarousel.setWidthFull();
//        helpCarousel.setHeight("200px");
//        helpCarousel.addChangeListener(change -> {
//            currentSlideIdx = Integer.valueOf(change.getPosition());
//        });
//        helpCarousel.getElement().getStyle().set("margin-top", "auto");
//
//        ContextMenu contextMenu = new ContextMenu();
//        contextMenu.setTarget(helpCarousel);
//        contextMenu.addItem("Edit", event -> {
//            Notification.show("Edit");
//
//        });
//        contextMenu.addItem("Add", event -> {
//            Notification.show("Add");
//        });
//        contextMenu.addItem("Save", event -> {
//            Notification.show("Save");
//        });
//
//        helpCarousel.setSlides(slides);
//        currentView.getLayout().add(helpCarousel);
////        helpDialog.add(carousel);
////        helpDialog.open();
    }

    private void closeHelp() {
//        if (helpCarousel == null) return;
//        QView currentView = sessionInfo.currentView;
//        VerticalLayout layout = currentView.getLayout();
//        layout.remove(helpCarousel);
//        helpCarousel = null;
    }


//    private ArrayList<QSlideContent> createNoHelpContent() {
//        ArrayList<QSlideContent> slidesContentList = new ArrayList<>();
//        QSlideContent content1 = new QSlideContent();
//        content1.setHtmlStr("Help for this view not available yet");
//        slidesContentList.add(content1);
//        QSlideContent content2 = new QSlideContent();
//        content2.setHtmlStr("Help for this view STILL not available");
//        slidesContentList.add(content2);
//        return slidesContentList;
//    }

//    private Component createSlideContent(String string, String color) {
//        H1 label = new H1(string);
//        label.getStyle().set("margin-top", "auto");
//        label.getStyle().set("margin-bottom", "auto");
//        VerticalLayout d = new VerticalLayout(label);
//        d.setAlignItems(FlexComponent.Alignment.CENTER);
//        d.setSizeFull();
//        d.getStyle().set("background-color", color);
//        return d;
//    }

    public void enterCurrentView(QView view) {

//        sessionInfoionInfo.currentView = view;
    }

    public void exitCurrentView() {
//        if (sessionInfo.currentView != null) {
//            sessionInfo.prevView = sessionInfo.currentView;
//            sessionInfo.currentView = null;
//        }
//        closeHelp();
    }


//    public void addHelpKey(String key) {
//        helpHistory.add(key);
//    }
//
//    public void removeHelpKey(String key) {
//        helpHistory.remove(key);
//    }


//    @Route
//    @PWA(name = "My Application", shortName = "My Application")
//    @CssImport(value = "./styles/custom-styles.css", id = "custom-dialog-styles", themeFor = "vaadin-*-overlay")
//    public class MainView extends VerticalLayout {
//
//        public MainView() {
//            Dialog d = new Dialog(new TextField("Text field", "Text Field"));
//            d.getElement().setAttribute("theme", "dialog-top-left");
//
//            Button button = new Button("Click me",
//                    event ->  {
//                        d.open();
//                    });
//            add(button);
//        }
//    }
//
//    And custom-styles.css:
//
//            :host([theme~="dialog-top-left"]) [part~="overlay"]
//    {
//        align-self: flex-start;
//        position: absolute;
//        top: 0px;
//        left: 0px;
//    }


///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Health Notes //////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private Dialog createHealthNoteDialog() {
//        Dialog dialog = new Dialog();
//        dialog.setWidth("300px");
//
//        VerticalLayout content = new VerticalLayout();
//        content.setSizeFull();
//        content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
//        H4 title = new H4("Health Note");
//        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
//        listBox.setItems("Pain", "Sensation", "Skin", "Ear", "Nose", "Throat", "Other");
//
//        TextArea textArea = new TextArea();
////        textArea.setWidth("");
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.add(new Button("Cancel", click -> {
//            dialog.close();
//        }));
//        buttons.add(new Button("Save", click -> {
//            QHealthNote healthNote = new QHealthNote();
//            healthNote.timestamp = System.currentTimeMillis();
//            healthNote.note = textArea.getValue();
//            Set<String> selectedItems = listBox.getSelectedItems();
//            for (String selectedItem : selectedItems) {
//                healthNote.classifiers.add(selectedItem);
//            }
////            try {
//            sessionInfo.node.saveObject(healthNote);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            dialog.close();
//        }));
//        content.add(title, textArea, buttons);
//        dialog.add(content);
//        return dialog;
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Event /////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private Dialog createEventDialog() {
//        Dialog dialog = new Dialog();
//        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
//        listBox.setItems("Pain", "Sensation", "Skin", "Ear", "Nose", "Throat", "Other");
//
//        TextArea textArea = new TextArea();
//        dialog.add(textArea);
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.add(new Button("Cancel", click -> {
//            dialog.close();
//        }));
//        buttons.add(new Button("Save", click -> {
//            QEvent event = new QEvent();
//            event.timestamp = System.currentTimeMillis();
//            event.note = textArea.getValue();
//            Set<String> selectedItems = listBox.getSelectedItems();
//            for (String selectedItem : selectedItems) {
//                event.classifiers.add(selectedItem);
//            }
////            try {
//            sessionInfo.node.saveObject(event);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            dialog.close();
//        }));
//        dialog.add(buttons);
//        return dialog;
//    }
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Shopping List /////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private Dialog createShoppingListItemDialog() {
//        Dialog dialog = new Dialog();
//        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
//        listBox.setItems("Pain", "Sensation", "Skin", "Ear", "Nose", "Throat", "Other");
//
//        TextArea textArea = new TextArea();
//        dialog.add(textArea);
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.add(new Button("Cancel", click -> {
//            dialog.close();
//        }));
//        buttons.add(new Button("Save", click -> {
//            QShoppingListItem shoppingListItem = new QShoppingListItem();
//            shoppingListItem.timestamp = System.currentTimeMillis();
//            shoppingListItem.note = textArea.getValue();
//            Set<String> selectedItems = listBox.getSelectedItems();
//            for (String selectedItem : selectedItems) {
//                shoppingListItem.classifiers.add(selectedItem);
//            }
////            try {
//            sessionInfo.node.saveObject(shoppingListItem);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            dialog.close();
//        }));
//        dialog.add(buttons);
//        return dialog;
//    }
//

/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Todo //////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////

//    private Dialog createTodoItemDialog() {
//        Dialog dialog = new Dialog();
//        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
//        listBox.setItems("Pain", "Sensation", "Skin", "Ear", "Nose", "Throat", "Other");
//
//        TextArea textArea = new TextArea();
//        dialog.add(textArea);
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.add(new Button("Cancel", click -> {
//            dialog.close();
//        }));
//        buttons.add(new Button("Save", click -> {
//            QTodoItem todoItem = new QTodoItem();
//            todoItem.timestamp = System.currentTimeMillis();
//            todoItem.note = textArea.getValue();
//            Set<String> selectedItems = listBox.getSelectedItems();
//            for (String selectedItem : selectedItems) {
//                todoItem.classifiers.add(selectedItem);
//            }
////            try {
//                sessionInfo.node.saveObject(todoItem);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            dialog.close();
//        }));
//        dialog.add(buttons);
//        return dialog;
//    }


/////////////////////////////////////////////////////////////////////////////////////////////////////
///////// Personal Weight ///////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////

//    private Dialog createPersonalWeightRecordDialog() {
//        Dialog dialog = new Dialog();
//        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
//        listBox.setItems("Pain", "Sensation", "Skin", "Ear", "Nose", "Throat", "Other");
//
//        TextArea textArea = new TextArea();
//        dialog.add(textArea);
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.add(new Button("Cancel", click -> {
//            dialog.close();
//        }));
//        buttons.add(new Button("Save", click -> {
//            QPersonalWeightRecord personalWeightRecord = new QPersonalWeightRecord();
//            personalWeightRecord.timestamp = System.currentTimeMillis();
//            personalWeightRecord.note = textArea.getValue();
//            Set<String> selectedItems = listBox.getSelectedItems();
//            for (String selectedItem : selectedItems) {
//                personalWeightRecord.classifiers.add(selectedItem);
//            }
////            try {
//            sessionInfo.node.saveObject(personalWeightRecord);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            dialog.close();
//        }));
//        dialog.add(buttons);
//        return dialog;
//    }
//
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
/////////// Password //////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    private Dialog createPasswordInfoDialog() {
//        Dialog dialog = new Dialog();
//        MultiSelectListBox<String> listBox = new MultiSelectListBox<>();
//        listBox.setItems("Pain", "Sensation", "Skin", "Ear", "Nose", "Throat", "Other");
//
//        TextArea textArea = new TextArea();
//        dialog.add(textArea);
//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.add(new Button("Cancel", click -> {
//            dialog.close();
//        }));
//        buttons.add(new Button("Save", click -> {
//            QPasswordInfo passwordInfo = new QPasswordInfo();
////            passwordInfo.passwordId.timestamp = System.currentTimeMillis();
////            passwordInfo.note = textArea.getValue();
////            Set<String> selectedItems = listBox.getSelectedItems();
////            for (String selectedItem : selectedItems) {
////                passwordInfo.classifiers.add(selectedItem);
////            }
////            try {
//            sessionInfo.node.saveObject(passwordInfo);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            dialog.close();
//        }));
//        dialog.add(buttons);
//        return dialog;
//    }
//

///////////////////////////////////////////////////////////////////////////////////////////////////
} ///////////// End Class /////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


