package com.quimera.model;

import java.util.Objects;

/**
 * Created by Manu on 25/06/2016.
 */
public class Player implements Comparable<Player>{

    private String id;
    private String name;
    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id) &&
                Objects.equals(name, player.name) &&
                Objects.equals(lastName, player.lastName);
    }

    @Override
    public int compareTo(Player o) {
        return this.id.compareTo(o.getId());
    }
}
