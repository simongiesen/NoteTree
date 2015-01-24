package com.ian.notetree.database;

/**
 * Created by Ian on 1/5/2015.
 */
public final class DatabaseTable
{
    /**
     * <code>private</code>, so as to prevent outside instantiation.
     */
    private DatabaseTable() {
    }

    /**
     *
     * @return
     */
    public static final String Category() {
        return DatabaseTable.Category.class.toString();
    }

    /**
     *
     * @return
     */
    public static final String Note() {
        return Note.class.toString();
    }

    /**
     *
     */
    public static final class Category
    {
        /**
         * <code>private</code>, so as to prevent outside instantiation.
         */
        private Category() {
        }

        /**
         *
         * @return
         */
        public static final String CID() {
            return "CID";
        }

        /**
         *
         * @return
         */
        public static final String CName() {
            return "CName";
        }

        /**
         *
         * @return
         */
        public static final String ParentCID() {
            return "ParentCID";
        }

        /**
         *
         */
        public static final class CName
        {
            /**
             * <code>private</code>, so as to prevent outside instantiation.
             */
            private CName() {
            }

            /**
             *
             * @return
             */
            public static final String Default() { return "Default"; }
        }
    }

    /**
     *
     */
    public static final class Note
    {
        /**
         * <code>private</code>, so as to prevent outside instantiation.
         */
        private Note() {
        }

        /**
         *
         * @return
         */
        public static final String NID() {
            return "NID";
        }

        /**
         *
         * @return
         */
        public static final String CID() {
            return "CID";
        }

        /**
         *
         * @return
         */
        public static final String NText() {
            return "NText";
        }

        /**
         *
         * @return
         */
        public static final String DateTime() {
            return "DateTime";
        }
    }
}
