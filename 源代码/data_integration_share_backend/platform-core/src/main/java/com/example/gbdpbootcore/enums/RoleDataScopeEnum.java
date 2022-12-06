package com.example.gbdpbootcore.enums;

/**
 * @author kongweichang
 */
public enum RoleDataScopeEnum {

    // 数据范围（1：所有数据；2：所在公司及以下数据；3：所在部门及以下数据；4：仅本人数据；5：访客数据）
    DATA_SCOPE_ALL("DATA_SCOPE_ALL", 1),
    DATA_SCOPE_COMPANY("DATA_SCOPE_COMPANY", 2),
    DATA_SCOPE_OFFICE("DATA_SCOPE_OFFICE", 3),
    DATA_SCOPE_SELF("DATA_SCOPE_SELF", 4),
    DATA_SCOPE_CUSTOM("DATA_SCOPE_CUSTOM", 5);

    String name;
    Integer value;

    RoleDataScopeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static RoleDataScopeEnum getDataScope(String name) {
        for (RoleDataScopeEnum roleDataScopeEnum : RoleDataScopeEnum.values()) {
            if (roleDataScopeEnum.getName().equals(name)) {
                return roleDataScopeEnum;
            }
        }
        return DATA_SCOPE_CUSTOM;
    }

    public static boolean compareScopeRange(String dataScope, String maxDataScope) {
        return getDataScope(dataScope).getValue() > getDataScope(maxDataScope).getValue();
    }

    public static RoleDataScopeEnum getDataScope(Integer value) {
        for (RoleDataScopeEnum roleDataScopeEnum : RoleDataScopeEnum.values()) {
            if (roleDataScopeEnum.getValue().equals(value)) {
                return roleDataScopeEnum;
            }
        }
        return DATA_SCOPE_CUSTOM;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }


}

