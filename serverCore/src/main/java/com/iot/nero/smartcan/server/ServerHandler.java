package com.iot.nero.smartcan.server;

import com.iot.nero.smartcan.SmartCanBootstrap;
import com.iot.nero.smartcan.constant.CONSTANT;
import com.iot.nero.smartcan.core.Protocol;
import com.iot.nero.smartcan.entity.Tick;
import com.iot.nero.smartcan.entity.platoon.LogoutRequestMessage;
import com.iot.nero.smartcan.entity.response.Response;
import com.iot.nero.smartcan.exceptions.PackageBrokenException;
import com.iot.nero.smartcan.service.IProtocolService;
import com.iot.nero.smartcan.service.impl.ProtocolService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import static com.iot.nero.smartcan.constant.CONSTANT.pInfo;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2018/6/5
 * Time   7:37 PM
 */
public class ServerHandler implements Runnable, IHandler {

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;

    ByteBuffer input = ByteBuffer.allocate(2<<20);
    byte[] receivedBytes;

    Response<Object> response;

    static final int READING = 0, SENDING = 1;

    int state = READING;

    public ServerHandler(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;

        this.socketChannel.configureBlocking(false);
        this.selectionKey = socketChannel.register(selector, 0);

        this.selectionKey.attach(this);
        this.selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    public void run() {
        try {
            if (this.state == READING) {
                this.read();
            } else if (this.state == SENDING) {
                this.write();
            }
        } catch (IOException e) {
            pInfo(CONSTANT.CLIENT_OFFLINE);
            //
            //
            LogoutRequestMessage logoutRequestMessage = new LogoutRequestMessage();
            logoutRequestMessage.msgCount = 0L;
            logoutRequestMessage.syncNum = 0L;
            logoutRequestMessage.timestamp = String.valueOf(System.currentTimeMillis()).getBytes();
            logoutRequestMessage.token = "".getBytes();

            Tick tick = SmartCanBootstrap.tickMap.get(socketChannel);


            try {
                if(tick!=null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    logoutRequestMessage.ber_encode(outputStream);
                    Protocol protocol = new Protocol();
                    protocol.setCheckCode(tick.getProtocol().checkCode);
                    protocol.setCommandUnit(tick.getProtocol().getCommandUnit());
                    protocol.setInditicalCode(tick.getProtocol().getInditicalCode());
                    protocol.setStartSymbol(tick.getProtocol().getStartSymbol());
                    protocol.setDataUnitLength((short) outputStream.toByteArray().length);
                    protocol.setDataUnit(outputStream.toByteArray());

                    IProtocolService iProtocolService = new ProtocolService();
                    iProtocolService.logout(protocol, socketChannel);
                    SmartCanBootstrap.tickMap.remove(socketChannel);
                }else {
                    pInfo("(TICK) not found!");
                }
                this.state = READING;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IOException e1) {
                this.state = READING;
            }


        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
            this.state = READING;
        } catch (PackageBrokenException e) {
            e.printStackTrace();
            this.state = READING;
        }
    }

    public void read() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException, PackageBrokenException {
        int readCount = socketChannel.read(this.input);
        this.readBytes(readCount);
        this.state = SENDING;
        this.selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    protected void readBytes(int readCount) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, PackageBrokenException {
        if (readCount > 0) {
            this.input.flip();
            this.receivedBytes = new byte[readCount];
            byte[] array = this.input.array();
            System.arraycopy(array, 0, this.receivedBytes, 0, readCount);
            this.readProcess();
            this.input.clear();
        }
    }


    @Override
    public synchronized void readProcess() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException, PackageBrokenException {
        // need override
    }

    @Override
    public void writeProcess() throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        // need override
    }


    void write() throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        this.writeProcess();
        this.selectionKey.interestOps(SelectionKey.OP_READ);
        this.state = READING;
    }
}
