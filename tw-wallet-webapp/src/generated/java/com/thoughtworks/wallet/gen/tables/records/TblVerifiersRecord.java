/*
 * This file is generated by jOOQ.
 */
package com.thoughtworks.wallet.gen.tables.records;


import com.thoughtworks.wallet.gen.tables.TblVerifiers;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TblVerifiersRecord extends UpdatableRecordImpl<TblVerifiersRecord> implements Record3<String, String, String[]> {

    private static final long serialVersionUID = 1236855602;

    /**
     * Setter for <code>public.tbl_verifiers.id</code>.
     */
    public TblVerifiersRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.tbl_verifiers.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.tbl_verifiers.name</code>.
     */
    public TblVerifiersRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.tbl_verifiers.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.tbl_verifiers.vc_types</code>.
     */
    public TblVerifiersRecord setVcTypes(String[] value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.tbl_verifiers.vc_types</code>.
     */
    public String[] getVcTypes() {
        return (String[]) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String[]> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, String[]> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return TblVerifiers.TBL_VERIFIERS.ID;
    }

    @Override
    public Field<String> field2() {
        return TblVerifiers.TBL_VERIFIERS.NAME;
    }

    @Override
    public Field<String[]> field3() {
        return TblVerifiers.TBL_VERIFIERS.VC_TYPES;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public String[] component3() {
        return getVcTypes();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public String[] value3() {
        return getVcTypes();
    }

    @Override
    public TblVerifiersRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public TblVerifiersRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TblVerifiersRecord value3(String[] value) {
        setVcTypes(value);
        return this;
    }

    @Override
    public TblVerifiersRecord values(String value1, String value2, String[] value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TblVerifiersRecord
     */
    public TblVerifiersRecord() {
        super(TblVerifiers.TBL_VERIFIERS);
    }

    /**
     * Create a detached, initialised TblVerifiersRecord
     */
    public TblVerifiersRecord(String id, String name, String[] vcTypes) {
        super(TblVerifiers.TBL_VERIFIERS);

        set(0, id);
        set(1, name);
        set(2, vcTypes);
    }
}
