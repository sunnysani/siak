package com.ppl.siakngnewbe.pengecekanirs.checker;

public class IpsOutOfBoundException extends Exception {
    public IpsOutOfBoundException() {
        super("IP semester harus bernilai kurang dari atau sama dengan 4.00");
    }
}
