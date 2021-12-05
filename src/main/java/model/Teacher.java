package model;

import java.util.Objects;

/**
 * Date: 5.12.2021
 */
public class Teacher extends Person{

    private int teacherId;

    public Teacher(String firstName, String lastName, int teacherId) {
        super(firstName, lastName);
        this.teacherId = teacherId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", teacherId=" + teacherId +
                '}';
    }

    /**
     * @return true if two teacher objects are equal (they have the same teacherId); false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher teacher)) return false;
        if (!super.equals(o)) return false;
        return getTeacherId() == teacher.getTeacherId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTeacherId());
    }
}