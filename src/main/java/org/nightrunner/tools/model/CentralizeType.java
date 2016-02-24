package org.nightrunner.tools.model;

import org.apache.commons.lang.enums.Enum;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class CentralizeType extends Enum {

    public static final CentralizeType SELF = new CentralizeType(0, "self", "本部门");
    public static final CentralizeType ALL_BROTHER = new CentralizeType(1, "allBrother", "所有同级部门");
    public static final CentralizeType MANY_BROTHER = new CentralizeType(2, "manyBrother", "某几个部门");
    public static final CentralizeType DISABLED = new CentralizeType(3, "disabled", "不启用");

    private static final long serialVersionUID = 3727482472227857554L;
    private Integer id;
    private String code;
    private String showName;

    private CentralizeType(Integer id, String code, String showName) {
        super(code);
        this.id = id;
        this.code = code;
        this.showName = showName;
    }

    public static CentralizeType getEnumById(Integer id) {
        List<CentralizeType> budgetTypeData = getEnumList(CentralizeType.class);
        Iterator it = budgetTypeData.iterator();
        while (it.hasNext()) {
            CentralizeType budgetType = (CentralizeType) it.next();
            if (budgetType.getId().equals(id)) {
                return budgetType;
            }
        }
        return null;
    }

    public static CentralizeType getEnumByCode(String code) {
        List<CentralizeType> budgetTypeData = getEnumList(CentralizeType.class);
        Iterator it = budgetTypeData.iterator();
        while (it.hasNext()) {
            CentralizeType budgetType = (CentralizeType) it.next();
            if (budgetType.getCode().equals(code)) {
                return budgetType;
            }
        }
        return null;
    }

    public static CentralizeType getEnumByShowName(String showName) {
        List<CentralizeType> budgetTypeData = getEnumList(CentralizeType.class);
        Iterator it = budgetTypeData.iterator();
        while (it.hasNext()) {
            CentralizeType budgetType = (CentralizeType) it.next();
            if (budgetType.getShowName().equals(showName)) {
                return budgetType;
            }
        }
        return null;
    }

    public static String getShowNameByCode(String code) {
        List<CentralizeType> budgetTypeData = getEnumList(CentralizeType.class);
        Iterator it = budgetTypeData.iterator();
        while (it.hasNext()) {
            CentralizeType budgetType = (CentralizeType) it.next();
            if (budgetType.getCode().equals(code)) {
                return budgetType.getShowName();
            }
        }
        return null;
    }

    public static Map getEnumMap() {
        return getEnumMap(CentralizeType.class);
    }

    public static List getEnumList() {
        return getEnumList(CentralizeType.class);
    }

    public static Iterator iterator() {
        return iterator(CentralizeType.class);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShowName() {
        return showName;
    }

    public void setName(String showName) {
        this.showName = showName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

