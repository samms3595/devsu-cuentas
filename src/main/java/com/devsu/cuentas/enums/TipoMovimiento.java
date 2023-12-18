package com.devsu.cuentas.enums;

public enum TipoMovimiento {

    RETIRO("retiro"), DEPOSITO("deposito");

    private final String value;

    TipoMovimiento(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
