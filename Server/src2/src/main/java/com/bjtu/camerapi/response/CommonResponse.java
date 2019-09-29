package com.bjtu.camerapi.response;

import com.common.spring.Response;
import lombok.Data;

public class CommonResponse {
    @Data
    public static class StateResponse implements Response {
        private int state;

        public StateResponse() {
        }

        public StateResponse(int state) {
            this.state = state;
        }
    }

    @Data
    public static class NoResponse implements Response {
        public NoResponse() {
        }
    }

}
