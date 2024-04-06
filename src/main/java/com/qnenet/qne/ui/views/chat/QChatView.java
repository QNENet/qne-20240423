package com.qnenet.qne.ui.views.chat;//package com.qnenet.qaddons001.qchatview;



import com.qnenet.qne.system.constants.QRoutes;
import com.qnenet.qne.ui.ContentLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

//@PageTitle("Chat")
//@Route(value = "chat", layout = MainLayout.class)
@AnonymousAllowed
//@RolesAllowed("USER")
@Route(value = QRoutes.CHAT, layout = ContentLayout.class)
@PageTitle("Chat")
//@CssImport(value = "./styles/views/cardlist/card-list-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")

public class QChatView extends VerticalLayout {

    public QChatView() {
        addClassName("chat-view");
//        setSpacing(false);
//        // UserInfo is used by Collaboration Engine and is used to share details
//        // of users to each other to able collaboration. Replace this with
//        // information about the actual user that is logged, providing a user
//        // identifier, and the user's real name. You can also provide the users
//        // avatar by passing an url to the image as a third parameter, or by
//        // configuring an `ImageProvider` to `avatarGroup`.
//        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");
//
//        // Tabs allow us to change chat rooms.
//        Tabs tabs = new Tabs(new Tab("#general"), new Tab("#support"), new Tab("#casual"));
//        tabs.setWidthFull();
//
//        // `CollaborationMessageList` displays messages that are in a
//        // Collaboration Engine topic. You should give in the user details of
//        // the current user using the component, and a topic Id. Topic id can be
//        // any freeform string. In this template, we have used the format
//        // "chat/#general". Check
//        // https://vaadin.com/docs/latest/ce/collaboration-message-list/#persisting-messages
//        // for information on how to persisting are retrieving messages over
//        // server restarts.
//        CollaborationMessageList list = new CollaborationMessageList(userInfo, "chat/#general");
//        list.setWidthFull();
//        list.addClassNames("chat-view-message-list");
//
//        // `CollaborationMessageInput is a textfield and button, to be able to
//        // submit new messages. To avoid having to set the same info into both
//        // the message list and message input, the input takes in the list as an
//        // constructor argument to get the information from there.
//        CollaborationMessageInput input = new CollaborationMessageInput(list);
//        input.addClassNames("chat-view-message-input");
//        input.setWidthFull();
//
//        // Layouting
//        add(tabs, list, input);
//        setSizeFull();
//        expand(list);
//
//        // Change the topic id of the chat when a new tab is selected
//        tabs.addSelectedChangeListener(event -> {
//            String channelName = event.getSelectedTab().getLabel();
//            list.setTopic("chat/" + channelName);
//        });


//        Person person = DataService.getPeople(1).get(0);
        MessageList list = new MessageList();
        Instant yesterday = LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC);
        Instant fiftyMinsAgo = LocalDateTime.now().minusMinutes(50).toInstant(ZoneOffset.UTC);
        MessageListItem message1 = new MessageListItem(
                "Linsey, could you check if the details with the order are okay?",
                yesterday, "Matt Mambo");

        message1.setUserColorIndex(1);

//        MessageListItem message2 = new MessageListItem("All good. Ship it.",
//                fiftyMinsAgo, "Linsey Listy", person.getPictureUrl());

//        message2.setUserColorIndex(2);
//        list.setItems(Arrays.asList(message1, message2));


        MessageInput input = new MessageInput();
        input.addSubmitListener(submitEvent -> {
            Notification.show("Message received: " + submitEvent.getValue(),
                    3000, Notification.Position.MIDDLE);
        });
        add(list, input);
    }

}