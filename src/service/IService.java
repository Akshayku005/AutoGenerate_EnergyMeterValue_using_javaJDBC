package service;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface IService {
    public void saveInDB(Statement s, String ts, int min, int hour);
//    public void saveInDB2(Statement s, LocalDateTime ts, int min, int hour);
    public void insertEnergyMeterVales(int min) ;

    public void getEnergyConsumption(String date);
    public void calculateEnergyConsumption();
}
