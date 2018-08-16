package com.iot.nero.smartcan.plugin.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.iot.nero.smartcan.entity.platoon.SmartFaultRequestMessage;
import com.iot.nero.smartcan.plugin.dao.CarAdminDao;
import com.iot.nero.smartcan.plugin.entity.CarAdmin;
import com.iot.nero.smartcan.plugin.entity.User;
import com.iot.nero.smartcan.spi.OnSmartFaultListener;
import com.iot.nero.smartcan.utils.cache.CacheFactory;
import com.iot.nero.smartcan.utils.cache.CacheManager;
import com.iot.nero.smartcan.utils.cache.PushBean;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.iot.nero.smartcan.constant.CONSTANT.pInfo;
import static com.sun.corba.se.impl.util.RepositoryId.cache;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2018/7/16
 * Time   2:28 PM
 */
public class SmartFaultPush{

    private static String FAULT_EMAIL_TEMPLETE = "车辆 __vid__ 出现了 __fault__ 异常，请及时处理。 时间 __time__";
    static SqlSessionFactory factory = null;
    static InputStream is = null;

    static {//用一个静态块读取配置文件，获取返回的文件流。静态块在编译时就初始化
        try {
            is = new FileInputStream(System.getProperty("user.dir") + "/plugin/pushPlugin/config/mybatis-config.xml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //采用单例模式来保证工厂创建唯一
    public static SqlSessionFactory getSqlSessionFactory() {
        if (factory == null) {
            //创建SqlsessionFactory工厂
            factory = new SqlSessionFactoryBuilder().build(is);
        }
        return factory;
    }

    //获取SqlSession对象
    public static SqlSession getSqlSession() {
        return getSqlSessionFactory().openSession();
    }

    public static Boolean sendMail(String email,String vid,String fault) {

        String verifyEmail = FAULT_EMAIL_TEMPLETE.replaceAll("__vid__", vid);
        verifyEmail = verifyEmail.replaceAll("__fault__", fault);
        verifyEmail = verifyEmail.replaceAll("__time__", String.valueOf(System.currentTimeMillis()));

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIE8TWMUpJxQo8", "J4zdWbhL8DPLlnpnVX58tbvHs2HoE7");
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();
        try {
            request.setAccountName("fault@autobrain.com");
            request.setFromAlias("SmartCanServer");
            request.setAddressType(1);
            request.setTagName("notify");
            request.setReplyToAddress(true);
            request.setToAddress(email);
            request.setSubject("车辆"+vid+"异常");
            request.setHtmlBody(verifyEmail);
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);

            return true;
        } catch (ClientException e) {
            return false;
        }
    }


    public static void onPush(SmartFaultRequestMessage smartFaultRequestMessage) {

        pInfo("(PUSH_PLUGIN) 车辆异常推送触发.");
        // 推送 短信 或者 邮件 通知
        byte[] vid = smartFaultRequestMessage.vid;
        String vidString = bytesToString(vid);

        SqlSession sqlSession = getSqlSession();
        CarAdminDao carAdminDao = sqlSession.getMapper(CarAdminDao.class);
        List<CarAdmin> vidUserList = carAdminDao.getCarAdminAll(vidString);

        pInfo("(CARADMIN)"+vidUserList.toString());
        if (!vidUserList.isEmpty()) {
            for (CarAdmin carAdmin : vidUserList) {
                if (carAdmin.getIsMailOpen() == 1 || carAdmin.getIsPhoneOpen() == 1) {
                    pInfo("(CARADMIN)"+carAdmin);

                    User user = carAdminDao.getUserById(carAdmin.getUserId());
                    pInfo("(USER)"+user.toString());
                        if (carAdmin.getIsPhoneOpen() == 1) {

                        }
                        if (carAdmin.getIsMailOpen() == 1) {
                            PushBean pushBean = (PushBean) CacheManager.getData(user.getuEmail()); //不会重新加载创建cache
                            if(pushBean==null){
                                PushBean newPushBean = new PushBean();
                                newPushBean.setTimes(1);
                                newPushBean.setCode(smartFaultRequestMessage.ftype);
                                newPushBean.setEmail(user.getuEmail());
                                newPushBean.setUniqueId(smartFaultRequestMessage.vid);
                                sendMail(user.getuEmail(), vidString, bytesToString(smartFaultRequestMessage.ftype));
                                CacheManager.setData(user.getuEmail(), newPushBean,60*60);
                                pInfo("(PUSH_PLUGIN) FIRST MAIL TO:" + user.getuEmail());
                            }else {
                                if (pushBean.getTimes() < 5) { // 异常类型相同 车id相同 并小于5条

                                    sendMail(user.getuEmail(), vidString, bytesToString(smartFaultRequestMessage.ftype));
                                    pushBean.setTimes(pushBean.getTimes() + 1);
                                    CacheManager.setData(user.getuEmail(), pushBean,60*60);
                                    pInfo("(PUSH_PLUGIN) "+pushBean.getTimes()+" MAIL TO:" + user.getuEmail());
                                }else {
                                    pInfo("(PUSH_PLUGIN) "+pushBean.getTimes()+" ALREADY MAILED TO:" + user.getuEmail());
                                }
                            }
                        }

                }
            }
        }

        sqlSession.commit();
        sqlSession.close();
    }

    public static String bytesToString(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b:bytes){
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString();
    }
}
