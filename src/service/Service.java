package service;

import db.DB;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Service implements IService {


    Connection con = DB.createC();
    //    Timestamp ts = new Timestamp(System.currentTimeMillis());
    Random random = new Random();
    //    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDate date = LocalDate.parse("20140218", DateTimeFormatter.BASIC_ISO_DATE);
    LocalDateTime ldt = date.atTime(LocalTime.MIN);

    @Override
    public void saveInDB(Statement s, String ts, int min, int hour) {
        try {
            if (hour == 1) {
                System.out.println("update energy.energytable set hr = " + null + " where time_stamp = '" + ts + "'");
                s.execute("update energy.energytable set hr = " + null + " where time_stamp = '" + ts + "'");
            } else {
                System.out.println("update energy.energytable set hr = " + hour + " where time_stamp = '" + ts + "'");
                s.execute("update energy.energytable set hr = " + hour + " where time_stamp = '" + ts + "'");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void insertEnergyMeterVales(int min)  {
        Connection con = DB.createC();
        PreparedStatement s = null;
        try {
            System.out.println("Connection open");
            s = con.prepareStatement("insert into energy.energytable (time_stamp, meter) values (?,?)");
//            Statement s = con.createStatement();
            int t = 0;
            stop:
            while (true) {
//                LocalDateTime now = LocalDateTime.now().plusMinutes(t);
//                String ts = dtf.format(now);
                LocalDateTime ts = ldt.plusMinutes(t);
                t++;
                if (min <= 3000) {
                    s.setTimestamp(1, java.sql.Timestamp.valueOf(ts));
                    s.setInt(2, min);
                    s.executeUpdate();
//                    saveInDB2(s, ts, min, 1);
                    min++;
                    int answer = random.nextInt(10) + 1;
                    int m = min + answer;
                    min = m;
                } else {
                    break stop;
                }
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
                    DB.closeConnection(con);
                    System.out.println("connection Closed!");
                    DB.closePreparedStatement(s);

        }
    }

    @Override
    public void getEnergyConsumption(String date) {
        Connection con = DB.createC();
        PreparedStatement s = null;
        try {
            System.out.println("Connection open");
            System.out.println(" ");
            System.out.println("********************************************************************");
            s = con.prepareStatement("SELECT * FROM energy.energytable where time_stamp like '" + date + "%'");
//            Statement   s = con.createStatement();
//            ResultSet r = s.executeQuery("SELECT * FROM energy.energytable where time_stamp like '" + date + "%'");
            ResultSet r = s.executeQuery();
            while (r.next()) {
                if (r.getInt(3) > 1) {
                    System.out.println("Time: " + r.getString(1));
//                    System.out.println("Meter value:" + r.getInt(2));
                    System.out.println("Hourly energy consumed:" + r.getInt(3));
                    System.out.println("********************************************************************");
                    System.out.println("");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeConnection(con);
            DB.closePreparedStatement(s);
            System.out.println("connection Closed!");
        }
//        finally {
//            try {
//                con.close();
//            } catch (Exception e) { /* Ignored */ }
//        }
    }

    @Override
    public void calculateEnergyConsumption() {
        Connection con = DB.createC();
        PreparedStatement m = null;
        try {
            System.out.println("connection opened");
            Statement s = con.createStatement();
            m = con.prepareStatement("SELECT meter FROM energy.energytable where time_stamp= ?");


            ResultSet r = m.executeQuery("SELECT time_stamp FROM energy.energytable");
            List<String> list = new ArrayList<>();

            while (r.next()) {
                list.add(r.getString("time_stamp"));
            }
            System.out.println(list);
            int f = 0, z = 0, im = 0, hour = 0;
            for (String ts : list) {
                if (f == 0 || z == 60) {
                    if (f == 0) {
//                        ResultSet m = s.executeQuery("SELECT meter FROM energy.energytable where time_stamp='" + ts + "'");
//                        m.next();
                        m.setTimestamp(1, java.sql.Timestamp.valueOf(ts));
                        ResultSet k = m.executeQuery();
                        k.next();
                        int min = k.getInt("meter");
                        im = min;
                        f++;
                        z++;
                        saveInDB(s, ts, min, hour);
                    } else {
//                        ResultSet m = s.executeQuery("SELECT meter FROM energy.energytable where time_stamp='" + ts + "'");
//                        m.next();
                        m.setTimestamp(1, java.sql.Timestamp.valueOf(ts));
                        ResultSet k = m.executeQuery();
                        k.next();
                        int min = k.getInt("meter");
                        hour = min - im;
                        saveInDB(s, ts, min, hour);
                        z = 1;
                        im = min;
                    }
                } else {
                    m.setTimestamp(1, java.sql.Timestamp.valueOf(ts));
                    ResultSet k = m.executeQuery();
                    k.next();
                    int min = k.getInt("meter");
                    saveInDB(s, ts, min, 1);
                    z++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeConnection(con);
            DB.closePreparedStatement(m);
            System.out.println("connection Closed!");
        }
    }
}
