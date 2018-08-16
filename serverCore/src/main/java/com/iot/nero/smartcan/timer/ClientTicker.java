package com.iot.nero.smartcan.timer;

import com.iot.nero.smartcan.SmartCanBootstrap;
import com.iot.nero.smartcan.core.Protocol;
import com.iot.nero.smartcan.entity.Tick;
import com.iot.nero.smartcan.entity.platoon.LogoutRequestMessage;
import com.iot.nero.smartcan.factory.ConfigFactory;
import com.iot.nero.smartcan.service.IProtocolService;
import com.iot.nero.smartcan.service.impl.ProtocolService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.iot.nero.smartcan.constant.CONSTANT.pInfo;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2018/8/3
 * Time   3:21 PM
 */
public class ClientTicker {
    // database ticker
    private Timer timer;

    private Long tickInterval;

    private Long timeOut;

    private IProtocolService iProtocolService;

    public ClientTicker(long i,Long timeOut) throws NoSuchMethodException, InstantiationException, IOException, IllegalAccessException {
        this.timer = new Timer();
        this.iProtocolService = new ProtocolService();
        this.tickInterval = i;
        this.timeOut = timeOut;

    }

    public void start() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        // tick database
        timer.schedule(new TimerTask() {
            public void run() {
                pInfo("(ALIVE) check alive.");

                if(!SmartCanBootstrap.tickMap.isEmpty()) {
                    for (Map.Entry<SocketChannel, Tick> tickEntry : SmartCanBootstrap.tickMap.entrySet()) {
                        if (System.currentTimeMillis() - tickEntry.getValue().getLast() >= timeOut) { // 超时了
                            try {
                                LogoutRequestMessage logoutRequestMessage = new LogoutRequestMessage();
                                logoutRequestMessage.msgCount = 0L;
                                logoutRequestMessage.syncNum = 0L;
                                logoutRequestMessage.timestamp = String.valueOf(System.currentTimeMillis()).getBytes();
                                logoutRequestMessage.token = "".getBytes();
                                Protocol protocol = new Protocol();
                                protocol.setCheckCode(tickEntry.getValue().getProtocol().checkCode);
                                protocol.setCommandUnit(tickEntry.getValue().getProtocol().getCommandUnit());
                                protocol.setInditicalCode(tickEntry.getValue().getProtocol().getInditicalCode());
                                protocol.setStartSymbol(tickEntry.getValue().getProtocol().getStartSymbol());

                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                logoutRequestMessage.ber_encode(outputStream);
                                protocol.setDataUnitLength((short) outputStream.toByteArray().length);
                                protocol.setDataUnit(outputStream.toByteArray());

                                try {
                                    iProtocolService.logout(protocol, tickEntry.getKey());
                                    tickEntry.getKey().close();
                                    SmartCanBootstrap.tickMap.remove(tickEntry.getKey());
                                }catch (IOException e) {
                                    tickEntry.getKey().close();
                                    SmartCanBootstrap.tickMap.remove(tickEntry.getKey());
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }, tickInterval , ConfigFactory.getConfig().getDbTickInterval());

    }
}
