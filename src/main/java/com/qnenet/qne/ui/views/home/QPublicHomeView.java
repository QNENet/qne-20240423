/*
 * Copyright 2000-2020 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.qnenet.qne.ui.views.home;


import com.qnenet.qne.system.constants.QRoutes;
import com.qnenet.qne.system.api.QView;
import com.qnenet.qne.ui.ContentLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

//@PermitAll
@AnonymousAllowed
@PageTitle("Public Home")
@Route(value = QRoutes.PUBLIC_HOME, layout = ContentLayout.class)
public class QPublicHomeView extends VerticalLayout implements QView, BeforeEnterObserver, BeforeLeaveObserver {

//    private final QSessionInfo sessionInfo;
//    private final MainLayout mainLayout;
    private Dialog newInstallDialog;
    private String pageName = "";

    public QPublicHomeView() {
//        sessionInfo = (QSessionInfo) VaadinSession.getCurrent().getAttribute(QSystemConstants.SESSION_INFO);
//        mainLayout = (MainLayout) VaadinSession.getCurrent().getAttribute(QSystemConstants.MAIN_LAYOUT);

        //        QHelpDisplay helpDisplay = sessionInfo.qHelp.getDisplay();
//        add(helpDisplay);

        addClassName("home-view");

        setSizeFull();
        setAlignItems(Alignment.CENTER);
//        setJustifyContentMode(JustifyContentMode.CENTER);

        H4 h10 = new H4("This is not a web site.");
        H4 h20 = new H4("It is a web application that runs in your browser.");
        H4 h21 = new H4("");

//        H4 h30 = new H4("It does things for you without telling anyone else what IT is doing.");
//        H4 h40 = new H4("It does things for you without telling anyone else what YOU are doing.");
//        H4 h50 = new H4("It lets you do things without telling anyone else what YOU are doing.");
//        H4 h51 = new H4("It lets YOU decide who has access to your data and who can keep your data.");

        H4 h52 = new H4("");

        H3 h60 = new H3("This page is the Public Home page of this \"Quick N Easy\" demo.");

        H4 h70 = new H4("");
        H4 h80 = new H4("");

        H4 h90 = new H4("Top left is the HOME Button,");
        H4 h100 = new H4("next the MENU button");
        H4 h110 = new H4("and then the QUICK Button");
        H4 h120 = new H4("");
        H4 h130 = new H4("Top right we have the HELP Button");
        H4 h140 = new H4("and the LOGIN/LOGOUT Button");

        H4 h150 = new H4("");
        H4 h160 = new H4("After playing with each button, you will know all you need");
        H4 h170 = new H4("to become an expert \"Quick N Easy\" Web Application user.");

        H4 h180 = new H4("");
        H4 h190 = new H4("F11 turns full page mode on and off *");
        H4 h200 = new H4("* Does not work in some browsers and on some computers");

        add(h10, h20, h21, h52, h60, h70, h80, h90, h100, h110,
                h120, h130, h140, h150, h160, h170, h180, h190, h200);
//        add(h10, h20, h21, h30, h40, h50, h51, h52, h60, h70, h80, h90, h100, h110, h120, h130, h140, h150, h160, h170);


//        if (sessionInfo.newSystemState != null) {  // is new system
//            newInstallDialog = new Dialog();
//            newInstallDialog.setMaxWidth("500px");
//            newInstallDialog.setMinWidth("280px");
//
//            H4 menuBtnTitle = new H4("Menu Button");
//            String menuBtnStr = "Allows quick access to a multilevel selection of installed features.";
//            Paragraph menuBtnPara = new Paragraph(menuBtnStr);
//
//            H4 quickBtnTitle = new H4("Quick Button");
//            String quickBtnStr = "Used to quickly record items such as notes, events, reminders etc.. " +
//                    "without leaving the page you are working on.";
//            Paragraph quickBtnPara = new Paragraph(quickBtnStr);
//
//            H4 exitBtnTitle = new H4("Exit Button");
//            String exitBtnStr = "Used to log out";
//            Paragraph exitBtnPara = new Paragraph(exitBtnStr);
//
//            H4 securityTitle = new H4("Security");
//            String securityStr = "NONE of the data you input during this install is sent to the internet during " +
//                    "this install. It is stored ENCRYPTED, to be used later, when active on the QNE Network.";
//            Paragraph securityPara = new Paragraph(securityStr);
//
//            H4 typeOnceTitle = new H4("Type Once");
//            String typeOnceStr = "The data you input during this install will NEVER have to be typed again when" +
//                    " you are communicating within the QNE Network";
//            Paragraph typeOncePara = new Paragraph(typeOnceStr);
//
//            H4 willNeedTitle = new H4("You will need");
//            String willNeedStr = "The password for your MAIN email account and the \"Outgoing SMTP Server Name\".";
//            Paragraph willNeedPara = new Paragraph(willNeedStr);
//
//            H4 nowTitle = new H4("Now");
//            String nowStr = "Press ESC, then select \"New System\" from the menu";
//            Paragraph nowPara = new Paragraph(nowStr);
//
//            newInstallDialog.add(menuBtnTitle, menuBtnPara, quickBtnTitle, quickBtnPara, exitBtnTitle, exitBtnPara,
//                    securityTitle, securityPara, typeOnceTitle, typeOncePara, willNeedTitle, willNeedPara, nowTitle, nowPara);
//        }

//        Video video = new Video("https://player.vimeo.com/progressive_redirect/playback/671545451/rendition/1080p/1080p.mp4?loc=external&signature=7fa8a790d633f0665db2b04a690310ff124b3b63902b0265382580414fe693aa");
////        video.setMaxWidth("300px");
//        video.setMaxWidth("90%");
//
//        add(video);

//        add(new H1(pageName), video);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // this view cannot use sessionInfo.mainLayout.enterCurrentView() because
        // mainLayout not yet available
//        sessionInfo.currentView = this;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        doBeforeLeave();
    }

    @Override
    public void doBeforeLeave() {
//        mainLayout.exitCurrentView();
    }


    @Override
    public VerticalLayout getLayout() {
        return this;
    }
}
