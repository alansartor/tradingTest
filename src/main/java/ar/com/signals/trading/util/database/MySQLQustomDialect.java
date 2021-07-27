package ar.com.signals.trading.util.database;

import java.sql.Types;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MySQLQustomDialect extends MySQL5InnoDBDialect{
    protected void registerColumnType(int code, String name) {
        if (code == Types.TIMESTAMP) {
            super.registerColumnType(code, "TIMESTAMP(3)");
        } else {
            super.registerColumnType(code, name);
        }
    }
}
