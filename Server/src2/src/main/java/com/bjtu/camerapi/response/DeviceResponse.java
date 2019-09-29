package com.bjtu.camerapi.response;

import com.bjtu.camerapi.entity.Device;
import com.common.spring.Response;
import lombok.Data;

import java.util.List;

public class DeviceResponse {

    @Data
    public static class devicelist implements Response {
        private int state;
        private List<Device> list;

        public devicelist() {
        }

        public devicelist(int state, List<Device> list) {
            this.state = state;
            this.list = list;
        }
    }
}
