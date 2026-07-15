package br.com.agi.enums;
public enum RecordType {

    SALESMAN("001"),
    CUSTOMER("002"),
    SALE("003");

    private final String code;

    RecordType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static RecordType fromCode(String code) {

        for (RecordType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Tipo de registro inválido: " + code);
    }

}
