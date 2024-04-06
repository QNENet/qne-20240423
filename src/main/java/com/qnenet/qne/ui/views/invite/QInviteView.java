///*
// * Copyright 2000-2020 Vaadin Ltd.
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy of
// * the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations under
// * the License.
// */
//package com.qnenet.qne.views.invite;
//
//
//import com.qnenet.qne.system.constants.QRoutes;
//import com.qnenet.qne.system.constants.QSysConstants;
//import com.qnenet.qne.network.endpoint.QEndPoint;
//import com.qnenet.qne.network.endpoint.QEndPointManager;
//import com.qnenet.qne.system.impl.QSystem;
//import com.qnenet.qne.ui.ContentLayout;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.html.H2;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.EmailField;
//import com.vaadin.flow.router.*;
//import jakarta.annotation.security.PermitAll;
//import org.apache.commons.text.StringEscapeUtils;
//
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.ShortBufferException;
//import java.io.IOException;
//import java.net.URI;
//import java.awt.*;
//import java.security.NoSuchAlgorithmException;
//
//@PermitAll
//@Route(value = QRoutes.INVITE, layout = ContentLayout.class)
//@PageTitle("Invite")
//public class QInviteView extends VerticalLayout implements BeforeEnterObserver, BeforeLeaveObserver {
//    private final QEndPointManager endPointManager;
//    private final QSystem system;
//
//
//    private H2 addMember = new H2("Invite To QNE Community");
//    EmailField inviteMemberEmail = new EmailField("New Member Main Email");
//    Button inviteMemberBtn = new Button("Click to Send Invite");
//
//    public QInviteView(QSystem system, QEndPointManager endPointManager) {
////        sessionInfo = (QSessionInfo) VaadinSession.getCurrent().getAttribute(QSysConstants.SESSION_INFO);
//        this.system = system;
//        this.endPointManager = endPointManager;
//        inviteMemberEmail.setPlaceholder("username@example.com");
//
//        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//        inviteMemberBtn.addClickListener(click -> {
//
//            Desktop desktop = Desktop.getDesktop();
//            String inviteeEmailAddress = inviteMemberEmail.getValue();
//            inviteMemberEmail.clear();
//            inviteMemberEmail.focus();
//
//            QEndPoint endPoint;
//            try {
//                endPoint = endPointManager.addEndPoint(QSysConstants.ENDPOINT_TYPE_MEMBER, inviteeEmailAddress);
//            } catch (ShortBufferException e) {
//                throw new RuntimeException(e);
//            } catch (BadPaddingException e) {
//                throw new RuntimeException(e);
//            } catch (NoSuchAlgorithmException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            String littlePassword = new String(endPoint.getLittlePassword());
//
//            String subject = "Invite to Try QNE Community";
//
//            String body = "Invite " + littlePassword;
//
//            system.getExecutor().submit(new Runnable() {
//                @Override
//                public void run() {
//                    String message = "mailto:" + inviteeEmailAddress +
//                            "?subject=" + subject +
////                            "?subject=Quick N EasyInvite" +
//                            "&body=" + body;
////                    String escaped = StringEscapeUtils.escapeHtml4(message);
//                    String escaped = message.replace(" ", "%20");
//                    URI uri = URI.create(escaped);
//                    try {
//                        desktop.mail(uri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
////            String value = addMemberName.getValue();
////            if (value.isEmpty()) {
////                Notification.show("Must Input Member Name");
////                return;
////            }
////            try {
////                Member instance = sessionInfo.featuresManager.addMember(value);
//////                instance.start(null);
////            } catch (ExecutionException e) {
////                throw new RuntimeException(e);
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            } catch (Exception e) {
////                throw new RuntimeException(e);
////            }
//        });
//
//        add(addMember, inviteMemberEmail, inviteMemberBtn);
//
//    }
//
//    public void beforeEnter(BeforeEnterEvent event) {
//        System.out.println("addMember View BeforeEnter");
//    }
//
//    @Override
//    public void beforeLeave(BeforeLeaveEvent event) {
//        System.out.println("addMember View BeforeLeaveEvent");
//    }
//
//
////    @Override
////    public Registration addAttachListener(ComponentEventListener<AttachEvent> listener) {
////        return super.addAttachListener(listener);
////    }
//
//}
