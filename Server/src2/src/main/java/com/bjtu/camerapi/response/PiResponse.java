package com.bjtu.camerapi.response;

import com.common.spring.Response;
import lombok.Data;

public class PiResponse {
    @Data
    public static class hi implements Response {
        private int id;

        public hi() {
        }

        public hi(int id) {
            this.id = id;
        }
    }

    @Data
    public static class beat implements Response {
        private long time;
        private int op;

        public beat() {
        }

        public beat(long time, int op) {
            this.time = time;
            this.op = op;
        }
    }

    @Data
    public static class upload implements Response {
        private int id;
        private int type;
        private String data;

        public upload() {
        }

        public upload(int id, int type, String data) {
            this.id = id;
            this.type = type;
            this.data = data;
        }
    }

}
