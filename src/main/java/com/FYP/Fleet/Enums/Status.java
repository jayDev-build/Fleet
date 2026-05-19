package com.FYP.Fleet.Enums;

import lombok.Getter;

@Getter
public enum Status {
    CREATED(1, "Created"),
    ACTIVE(2, "Active"),
    COMPLETED(3, "Completed");

    private final int id;
    private final String label;

    Status(int id, String label){
        this.id = id;
        this.label = label;
    }

}
