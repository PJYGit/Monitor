package com.bjtu.camerapi.response;

import com.bjtu.camerapi.entity.PictureLine;
import com.common.spring.Response;
import lombok.Data;

import java.util.List;

public class PictureResponse {
    @Data
    public static class query implements Response {
        private int state;
        private List<PictureLine> list;

        public query() {
        }

        public query(int state, List<PictureLine> list) {
            this.state = state;
            this.list = list;
        }
    }
}
