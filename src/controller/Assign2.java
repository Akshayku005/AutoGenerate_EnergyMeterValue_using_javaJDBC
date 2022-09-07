package controller;

import db.DB;
import service.IService;
import service.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;

public class Assign2 {

    public static  void menu(){
        System.out.println("Enter any option: ");
        System.out.println(" 1. To insert Energymeter value"
                + "\n 2. To Calculate Hourly energy Consumption"
                + "\n 3. To get all data from DB"
                + "\n 4. To Exit");
    }
    public static void main(String[] args){
        IService service = new Service();
        Scanner scan = new Scanner(System.in);
        do {
            menu();
            int ch = scan.nextInt();
            switch (ch) {
                case 1:
                    System.out.println("Energy data here :: ");
                    int meter = scan.nextInt();
                    service.insertEnergyMeterVales(meter);
                    break;
                case 2:
                    System.out.println("The calculated Hourly energy value :  ");
                    service.calculateEnergyConsumption();
                    break;
                case 3:
                    System.out.println("pass the date here in the format 2014-02-18  YYYY-MM-DD to get that particular date energy consumption data:");
                    String date = scan.next();
                    service.getEnergyConsumption(date);
                    break;
                case 4:
                    System.out.println("come back again...");
                    System.exit(0);
                default:
                    System.out.println("Your choice not valid");
                    break;
            }
        }while (true);

    }
}
