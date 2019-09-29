package com.bjtu.camerapi.server;

import com.bjtu.camerapi.entity.Login;
import com.bjtu.camerapi.entity.User;
import com.bjtu.camerapi.service.ServiceGetter;
import com.bjtu.camerapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint("/picsoc")
@Component
public class WebSocketServer {

    private static CopyOnWriteArraySet<WebSocketServer> waitingWebSocketSet = new CopyOnWriteArraySet<>();
    private static CopyOnWriteArraySet<WebSocketServer> loggedWebSocketSet = new CopyOnWriteArraySet<>();

    public static void sendAll(String message) {
        for (WebSocketServer server : loggedWebSocketSet) {
            try {
                server.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isLogin = false;
    private Session session;

    @OnOpen
    public void OnOpen(Session session) {
        this.session = session;
        waitingWebSocketSet.add(this);
    }

    @OnClose
    public void OnClose() {
        (isLogin ? loggedWebSocketSet : waitingWebSocketSet).remove(this);
    }

    @OnMessage
    public void OnMessage(String message) {
        log.info("WebSocket got:" + message);
        if (isLogin) return;
        if (message.length() < 32) return;
        log.info("Valid message:" + message);

        String token = message.substring(0, 32);
        String name = message.substring(32);

        UserService userService = ServiceGetter.getBean(UserService.class);
        User user = userService.getUserByUsername(name);
        if (user == null) return;
        Login login = userService.getLoginInfo(user.getUid(), token);
        if (login == null) return;

        isLogin = true;
        loggedWebSocketSet.add(this);
        waitingWebSocketSet.remove(this);
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}
