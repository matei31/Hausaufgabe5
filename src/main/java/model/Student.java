package model;

import java.util.Comparator;
import java.util.Objects;

/**
 * Date: 5.12.2021
 */
public class Student extends Person{

    private int studentId;
    private int totalCredits;

    public Student(String firstName, String lastName, int studentId, int totalCredits) {
        super(firstName, lastName);
        this.studentId = studentId;
        this.totalCredits = totalCredits;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studentId=" + studentId +
                ", totalCredits=" + totalCredits +
                '}';
    }

    /**
     * @return true if two student objects are equal (they have the same Id and no of credits); false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        if (!super.equals(o)) return false;
        return getStudentId() == student.getStudentId() && getTotalCredits() == student.getTotalCredits();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStudentId(), getTotalCredits());
    }

    public static class NameSorter implements Comparator<Student> {
        @Override
        public int compare(Student s1, Student s2){
            return s1.getLastName().compareToIgnoreCase(s2.getLastName());
        }
    }
}