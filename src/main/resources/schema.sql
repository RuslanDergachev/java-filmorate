drop table if exists FILM, FRIENDS, GENRE, GENRE_FILM, LIKES, RATING_FILM, USERS;

create table IF NOT EXISTS FILM
(
    FILM_ID            INTEGER auto_increment,
    NAME          CHARACTER VARYING(100) not null,
    DESCRIPTION   CHARACTER VARYING(200),
    RELEASE_DATE  DATE,
    DURATION      INTEGER,
    RATING_MPA_ID INTEGER,
    RATE          INTEGER                not null,
    constraint FILM_PK
        primary key (FILM_ID)
);

create table IF NOT EXISTS RATING_FILM
(
    RATING_FILM_ID INTEGER auto_increment,
    NAME           CHARACTER VARYING(10),
    constraint RATING_FILM_PK
        primary key (RATING_FILM_ID)
);

create table IF NOT EXISTS GENRE
(
    GENRE_ID   INTEGER auto_increment,
    NAME CHARACTER VARYING(20),
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS GENRE_FILM (
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER,
    constraint GENRE_FILM_FK
    foreign key (FILM_ID) references FILM,
    constraint GENRE_FK
    foreign key (GENRE_ID) references GENRE
    );

create table IF NOT EXISTS USERS (
    USER_ID  INTEGER auto_increment,
    NAME     CHARACTER VARYING(50),
    LOGIN    CHARACTER VARYING(50) not null,
    E_MAIL   CHARACTER VARYING(50) not null,
    BIRTHDAY DATE,
    constraint USERS_PK
    primary key (USER_ID)
);

create table IF NOT EXISTS LIKES (
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_FILM_FILM_ID_FK
    foreign key (FILM_ID) references FILM,
    constraint USER_FK
    foreign key (USER_ID) references USERS
    );



create unique index IF NOT EXISTS USERS_USER_ID_UINDEX
    on USERS (USER_ID);

create table IF NOT EXISTS FRIENDS (
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS    BOOLEAN,
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);







