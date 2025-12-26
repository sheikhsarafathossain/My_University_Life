package edu.ewubd.cse4892022360109;

public class StudentAttendance {
    String name;
    boolean status;
    String remarks;
    public StudentAttendance(String name, boolean status, String remarks){
        this.name = name;
        this.status = status;
        this.remarks = remarks;
    }

    public String toString(){
        return name+";"+status+";"+remarks;
    }
}
