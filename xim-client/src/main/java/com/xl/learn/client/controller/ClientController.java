package com.xl.learn.client.controller;

import com.xl.learn.client.server.XIMClient;
import com.xl.learn.client.vo.res.BaseResponse;
import com.xl.learn.client.vo.res.NULLBody;
import com.xl.learn.common.enums.StatusEnum;
import com.xl.learn.common.msg.SendMsgReqVO;
import com.xl.learn.common.utils.Constants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xl
 * @date: 2021/8/5
 **/
@Controller
@RequestMapping("/")
public class ClientController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    private static final Map<Long, XIMClient> CLIENT_MAP = new ConcurrentHashMap<>(16);

    @Value("${xim.server.port}")
    private int nettyPort;

    @Value("${xim.server.ip}")
    private String nettyIp;

    @ApiOperation("登录 API")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<NULLBody> login(@RequestParam("userId") @NotNull Long userId) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        LOGGER.info("login userId=[{}]", userId);
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        if (CLIENT_MAP.containsKey(userId)) {
            res.setCode(StatusEnum.REPEAT_LOGIN.getCode());
            res.setMessage(StatusEnum.REPEAT_LOGIN.getMessage());
        } else {
            XIMClient client = new XIMClient();
            client.start(nettyIp, nettyPort);
            client.loginXIMServer(userId);
            CLIENT_MAP.putIfAbsent(userId, client);
        }
        return res;
    }

    @ApiOperation("私聊 API")
    @RequestMapping(value = "p2p", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<NULLBody> p2p(@RequestBody SendMsgReqVO msgReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();
        LOGGER.info("send from=[{}] to=[{}] msg=[{}]", msgReqVO.getFrom(), msgReqVO.getTo(), msgReqVO.getMsg());
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        if (!CLIENT_MAP.containsKey(msgReqVO.getFrom()) ||
                !CLIENT_MAP.containsKey(msgReqVO.getTo())) {
            res.setCode(StatusEnum.OFF_LINE.getCode());
            res.setMessage(StatusEnum.OFF_LINE.getMessage());
        } else {
            msgReqVO.setType(Constants.CommandType.MSG);
            CLIENT_MAP.get(msgReqVO.getFrom()).sendStringMsg(msgReqVO);
        }
        return res;
    }


    @ApiOperation("免登录直聊 API")
    @RequestMapping(value = "fastp2p", method = RequestMethod.POST)
    @ResponseBody()
    public BaseResponse<NULLBody> fastp2p(@RequestBody SendMsgReqVO msgReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();
        LOGGER.info("send from=[{}] to=[{}] msg=[{}]", msgReqVO.getFrom(), msgReqVO.getTo(), msgReqVO.getMsg());
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        if (!CLIENT_MAP.containsKey(msgReqVO.getFrom())) {
            XIMClient fromClient = new XIMClient();
            fromClient.start(nettyIp, nettyPort);
            fromClient.loginXIMServer(msgReqVO.getFrom());
            CLIENT_MAP.putIfAbsent(msgReqVO.getFrom(), fromClient);
        }
        if (!CLIENT_MAP.containsKey(msgReqVO.getTo())) {
            XIMClient toClient = new XIMClient();
            toClient.start(nettyIp, nettyPort);
            toClient.loginXIMServer(msgReqVO.getTo());
            CLIENT_MAP.putIfAbsent(msgReqVO.getTo(), toClient);
        }
        msgReqVO.setType(Constants.CommandType.MSG);
        Thread.sleep(500);
        CLIENT_MAP.get(msgReqVO.getFrom()).sendStringMsg(msgReqVO);
        return res;
    }

}
