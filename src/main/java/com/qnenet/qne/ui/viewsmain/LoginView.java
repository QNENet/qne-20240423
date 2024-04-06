package com.qnenet.qne.ui.viewsmain;

import com.qnenet.qne.system.constants.QRoutes;
import com.qnenet.qne.objects.classes.QUser;
import com.qnenet.qne.security.users.QUsersManager;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(QRoutes.LOGIN)
@PageTitle("QNE Login")
@AnonymousAllowed
//public class LoginView extends Composite<LoginOverlay> {
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();
    private final QUsersManager usersManager;

    public LoginView(QUsersManager usersManager) {
        this.usersManager = usersManager;

//		addClassName("enter-view");
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

//		login.setAction("login");
        loginForm.addLoginListener(loginEvent -> {
//            user.setUserName(loginEvent.getUsername());
//            user.setPwd(loginEvent.getPassword().toCharArray());
            QUser user = usersManager.findByUsername(loginEvent.getUsername());
         if (user == null) {}


        });
        add(loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}