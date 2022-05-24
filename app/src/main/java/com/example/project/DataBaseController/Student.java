package com.example.project.DataBaseController;

public class Student {
    private String name;
    private String surname;
    private String age;
    private double averageMark;

    public Student(){
       name = "";
       surname = "";
       averageMark = 0;
    }

    public Student(String name, String surname, String age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public Student(Student student){
        name = student.name;
        surname = student.surname;
        averageMark = student.averageMark;
        age = student.age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(double averageMark) {
        this.averageMark = averageMark;
    }

    public String getFullName(){
        StringBuilder stringBuilder = new StringBuilder(name);
        stringBuilder.append(" ").append(surname);
        return stringBuilder.toString();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
