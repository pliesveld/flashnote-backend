package com.pliesveld.flashnote.schema;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class MyPhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier arg0, JdbcEnvironment arg1) {
        return new Identifier(arg0.getText(), arg0.isQuoted());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier arg0, JdbcEnvironment arg1) {
        return new Identifier(arg0.getText(), arg0.isQuoted());
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier arg0, JdbcEnvironment arg1) {
        return new Identifier(arg0.getText(), arg0.isQuoted());
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier arg0, JdbcEnvironment arg1) {
        return new Identifier(arg0.getText(), arg0.isQuoted());
    }

    @Override
    public Identifier toPhysicalTableName(Identifier arg0, JdbcEnvironment arg1) {
        return new Identifier(arg0.getText(), arg0.isQuoted());
    }

}
