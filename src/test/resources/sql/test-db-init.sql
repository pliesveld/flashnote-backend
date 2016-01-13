
    drop table ANSWER if exists;

    drop table CATEGORY if exists;

    drop table DECK if exists;

    drop table DECK_FLASHCARD if exists;

    drop table FLASHCARD if exists;

    drop table QUESTION if exists;

    drop table STUDENT if exists;

    drop sequence if exists ANSWER_SEQ_GEN;

    drop sequence if exists CATEGORY_SEQ_GEN;

    drop sequence if exists DECK_SEQ_GEN;

    drop sequence if exists FLASHCARD_SEQ_GEN;

    drop sequence if exists QUESTION_SEQ_GEN;

    drop sequence if exists STUDENT_SEQ_GEN;

    create sequence ANSWER_SEQ_GEN start with 1 increment by 50;

    create sequence CATEGORY_SEQ_GEN start with 1 increment by 50;

    create sequence DECK_SEQ_GEN start with 1 increment by 50;

    create sequence FLASHCARD_SEQ_GEN start with 1 increment by 50;

    create sequence QUESTION_SEQ_GEN start with 1 increment by 50;

    create sequence STUDENT_SEQ_GEN start with 1 increment by 50;

    create table ANSWER (
        ANSWER_ID integer not null,
        CONTENT varchar(65600),
        primary key (ANSWER_ID)
    );

    create table CATEGORY (
        CATEGORY_ID smallint not null,
        CATEGORY_DESC varchar(255) not null,
        CATEGORY_NAME varchar(17) not null,
        CATEGORY_PARENT_ID smallint,
        primary key (CATEGORY_ID)
    );

    create table DECK (
        DECK_ID smallint not null,
        DECK_TITLE varchar(177),
        primary key (DECK_ID)
    );

    create table DECK_FLASHCARD (
        DECK_ID smallint not null,
        FLASHCARD_ID integer not null,
        FLASHCARD_ORDER integer not null,
        primary key (DECK_ID, FLASHCARD_ORDER)
    );

    create table FLASHCARD (
        FLASHCARD_ID integer not null,
        ANSWER_ID integer not null,
        QUESTION_ID integer not null,
        primary key (FLASHCARD_ID)
    );

    create table QUESTION (
        QUESTION_ID integer not null,
        CONTENT varchar(65600),
        primary key (QUESTION_ID)
    );

    create table STUDENT (
        STUDENT_ID integer not null,
        STUDENT_NAME varchar(32) not null,
        primary key (STUDENT_ID)
    );

    alter table CATEGORY 
        add constraint UK_krhdtxhxmd2b1kbcbnqck48di unique (CATEGORY_NAME);

    alter table CATEGORY 
        add constraint FKtc8rhc8yoa2jed24rvughyfu1 
        foreign key (CATEGORY_PARENT_ID) 
        references CATEGORY;

    alter table CATEGORY 
        add constraint FKk27io9pyt832xttamejqpmbfp 
        foreign key (CATEGORY_ID) 
        references CATEGORY;

    alter table DECK_FLASHCARD 
        add constraint UK_sia8nrv35977wtbgr6y6dm8s3 unique (FLASHCARD_ID);

    alter table DECK_FLASHCARD 
        add constraint FKincx12om5x39yhgc1ppyug88v 
        foreign key (FLASHCARD_ID) 
        references FLASHCARD;

    alter table DECK_FLASHCARD 
        add constraint FKkehh7ysoumupjgo7xn2grke5d 
        foreign key (DECK_ID) 
        references DECK;

    alter table FLASHCARD 
        add constraint UK8gfpbg0dx2he3ytfmrsvqkw2g unique (QUESTION_ID, ANSWER_ID);

    alter table FLASHCARD 
        add constraint FK1uj9bue4m945cei799uxsxyvy 
        foreign key (ANSWER_ID) 
        references ANSWER;

    alter table FLASHCARD 
        add constraint FKd5fgj075plv8hr2gdyub2v0bq 
        foreign key (QUESTION_ID) 
        references QUESTION;
