package net.biliarski.cashdesk.model;

import java.util.Objects;

public class Denomination {
    private int value;
    private int count;

    // Constructors
    public Denomination() {
    }

    public Denomination(int value, int count) {
        this.value = value;
        this.count = count;
    }

    // Getters and Setters
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // Business methods
    public int getTotal() {
        return value * count;
    }
//    public int getTotal() {
//        return value * count;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Denomination that = (Denomination) o;
        return value == that.value && count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, count);
    }

    @Override
    public String toString() {
        return "Denomination{" +
                "value=" + value +
                ", count=" + count +
                '}';
    }
    

}
