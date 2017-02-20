/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.builder.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public class NationalEntity {

    private String id;
    private String name;
    private final List<Region> regions;

    public NationalEntity() {
        regions = new ArrayList<>();
    }

    public NationalEntity(String id, String name) {
        this.id = id;
        this.name = name;
        regions = new ArrayList<>();
    }

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

    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public String toString() {
        return "NationalEntity{" + "id=" + id + ", name=" + name + ", regions=" + regions + '}';
    }

}
