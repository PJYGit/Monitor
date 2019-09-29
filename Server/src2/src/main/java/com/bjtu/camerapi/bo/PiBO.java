package com.bjtu.camerapi.bo;

import com.bjtu.camerapi.entity.Device;
import com.bjtu.camerapi.response.CommonResponse;
import com.bjtu.camerapi.response.PiResponse;
import com.bjtu.camerapi.server.WebSocketServer;
import com.bjtu.camerapi.service.PiService;
import com.bjtu.camerapi.service.ServiceGetter;
import com.common.spring.Response;
import com.common.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@Slf4j
public class PiBO {

    private static PiService piService;

    static {
        piService = ServiceGetter.getBean(PiService.class);
    }

    public static Response deviceLogin(String mac, String ip) {
        Device reqDevice = piService.selectDeviceByMAC(mac);
        if (reqDevice == null) {
            reqDevice = piService.insertDeviceByMAC(mac);
        }

        piService.updateDeviceIP(reqDevice.getDevid(), ip);
        piService.updateDeviceState(reqDevice.getDevid(), 2);
        piService.updateDeviceLastLogin(reqDevice.getDevid(), NumberUtil.getTimeStrap());
        return new PiResponse.hi(reqDevice.getDevid());
    }

    /* Receive heartbeat from PI
     * local value: -1 for device no response(temp disconnected)
     *              0 for device shutdown
     *              1 for devices pause
     *              2 for devices run as normal
     *
     *  when devices list shown, for any devices no response, set it to -1.
     *  when a devices with -1 state heart beat, set state to 2
     *  otherwise response with value in data board.
     */
    public static Response deviceHeartbeat(int id, long time) {
        Device device = piService.selectDeviceByID(id);
        if (device == null) {
            log.info("fake device.");
            return new PiResponse.beat(0, 2);
        }

        if (device.getState() == -1) {
            piService.updateDeviceState(device.getDevid(), 2);
            device.setState(2);
        }

        piService.updateDeviceHeartbeat(device.getDevid(), NumberUtil.getTimeStrap());

        return new PiResponse.beat(NumberUtil.getTimeStrap(), device.getState());
    }


    public static Response uploadPicture(int id, int type, MultipartFile[] file) {
        if (file.length != 1) {
            log.debug("no file or more than one file.");
        }
        return uploadPicture(id, type, file[0]);
    }

    public static Response uploadPicture(int id, int type, MultipartFile file) {
        String fileName = new Date().getTime() + file.getOriginalFilename();
        try {
            byte[] tmp = file.getBytes();
            log.info("" + tmp.length);
            log.info("filename:" + fileName);
            file.transferTo(new File(fileName));
        } catch (Exception e) {
            log.info(e.toString());
        }

        piService.insertPicture(id, type, "/pic/" + fileName);
        WebSocketServer.sendAll(JSONObject.fromObject(piService.selectPictureByUrl("/pic/" + fileName)).toString());

        return new CommonResponse.StateResponse(0);
    }

}
