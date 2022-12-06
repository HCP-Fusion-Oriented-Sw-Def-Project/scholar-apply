package com.example.gbdpuserac.model.vo;

import lombok.Data;

@Data
public class DeptVO {

    private String id;

    private String name;

    public DeptVO() {
    }

    public DeptVO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
