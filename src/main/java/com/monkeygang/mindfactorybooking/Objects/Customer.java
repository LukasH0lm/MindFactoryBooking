package com.monkeygang.mindfactorybooking.Objects;

public class Customer {

    int id;
    String name;
    String title;
    String email;
    String phone;
    Organization organization;

    public Customer(int id, String name, String title, String email, String phone, Organization organization) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Organization getOrganisation() {
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;
        if (!name.equals(customer.name)) return false;
        if (!title.equals(customer.title)) return false;
        if (!email.equals(customer.email)) return false;
        if (!phone.equals(customer.phone)) return false;
        return organization.equals(customer.organization);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + organization.hashCode();
        return result;
    }
}
