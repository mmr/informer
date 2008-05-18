package org.b1n.informer.core.persistence;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQLDialect;

/**
 * Forca tipos de data do postgresql para terem dados sobre timezone.
 * @author Marcio Ribeiro
 * @date Jan 28, 2008
 */
public class WithTimeZonePostgreSQLDialect extends PostgreSQLDialect {
    /**
     * Construtor.
     */
    public WithTimeZonePostgreSQLDialect() {
        registerColumnType(Types.DATE, "timestamp with time zone");
        registerColumnType(Types.TIMESTAMP, "timestamp with time zone");
    }
}
