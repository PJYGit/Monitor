package com.bjtu.camerapi.response;

import com.bjtu.camerapi.entity.UserLine;
import lombok.Data;
import com.common.spring.Response;

import java.util.List;

public class UserResponse {
    @Data
    public static class login implements Response {
        private int state;
        private int group;
        private String token;

        public login() {
        }

        public login(int state, int group, String token) {
            this.state = state;
            this.group = group;
            this.token = token;
        }
    }

    @Data
    public static class memberlist implements Response {
        private int state;
        private List<UserLine> list;

        public memberlist() {
        }

        public memberlist(int state, List<UserLine> list) {
            this.state = state;
            this.list = list;
        }
    }

    @Data
    public static class info implements Response {
        private int state;
        private int uid;
        private String newps;
        private int totalpic;

        public info() {

        }

        public info(int state, int uid, String newps, int totalpic) {
            this.state = state;
            this.uid = uid;
            this.newps = newps;
            this.totalpic = totalpic;
        }
    }

}
