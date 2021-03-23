package com.abacaba.ha.db;

import android.provider.BaseColumns;

public final class BillsContract {
    private BillsContract(){}

    public static class Company implements BaseColumns{
        public static final String TABLE_NAME = "company";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static class Person implements BaseColumns{
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DEBT = "debt";
        public static final String COLUMN_NAME_COMPANYID = "companyid";
    }

    public static class Transaction implements BaseColumns{
        public static final String TABLE_NAME = "tsction";
        public static final String COLUMN_NAME_FROM_PERSON = "from_person";
        public static final String COLUMN_NAME_TO_PERSON = "to_person";
        public static final String COLUMN_NAME_SUM = "sum";
        public static final String COLUMN_NAME_TRANSACTION_ID = "trans_id";
    }

    public static class TransactionList implements BaseColumns{
        public static final String TABLE_NAME = "transaction_list";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COMPANY_ID = "company_id";
    }

    public static class Payment implements BaseColumns{
        public static final String TABLE_NAME = "payments";
        public static final String COLUMN_NAME_PERSON_ID = "person_id";
        public static final String COLUMN_NAME_SUM = "sum";
        public static final String COLUMN_NAME_TRANSACTION_ID = "trans_id";
        public static final String COLUMN_NAME_TYPE = "payment_type";

    }
}
