package com.example.myfair;

public class Student extends User {
    private String university;
    private String major;
    private String gradYear;

    public Student() {
        super(User.TYPE_STUDENT);
    }

    public Student(String firstName, String lastName, String university,
                   String major, String gradYear) {
        super(User.TYPE_STUDENT, firstName, lastName);
        this.university = university;
        this.major = major;
        this.gradYear = gradYear;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGradYear() {
        return gradYear;
    }

    public void setGradYear(String gradYear) {
        this.gradYear = gradYear;
    }
}
