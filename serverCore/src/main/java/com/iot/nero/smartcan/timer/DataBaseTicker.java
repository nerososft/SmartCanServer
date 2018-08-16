package com.iot.nero.smartcan.timer;

import com.iot.nero.smartcan.factory.ConfigFactory;
import com.iot.nero.smartcan.utils.dbtools.DataBase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2018/8/3
 * Time   3:17 PM
 */
public class DataBaseTicker {

    private DataBase dataBase;
    // database ticker
    private Timer timer;

    private Long tickInterval;

    public DataBaseTicker(DataBase dataBase, long i) {
        timer = new Timer();
        this.dataBase = dataBase;
        tickInterval = i;
    }

    public void start() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        // tick database
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    dataBase.tick();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, tickInterval , ConfigFactory.getConfig().getDbTickInterval());

    }
}
