package com.devsu.cuentas.enums;

public enum TipoCuenta {
    AHORRO(0), CORRIENTE(1);

    private final int value;

    TipoCuenta(int value) {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
