-- Table: users
CREATE TABLE users
(
    id_user              serial       NOT NULL,
    email                varchar(100) NOT NULL,
    hashed_password      varchar(300) NOT NULL,
    created_at           timestamp    NOT NULL,
    modified_at          timestamp    NOT NULL,
    deleted_at           timestamp NULL,
    blocked_at           timestamp NULL,
    is_active            boolean      NOT NULL,
    user_type            char         NOT NULL,
    reset_password_token varchar(400) NULL,
    CONSTRAINT users_pk PRIMARY KEY (id_user)
);

-- tables
-- Table: address
CREATE TABLE address
(
    id_address    serial      NOT NULL,
    country       varchar(30) NOT NULL,
    street_name   varchar(50) NOT NULL,
    street_number int         NOT NULL,
    zip_code      varchar(10) NOT NULL,
    city          varchar(30) NOT NULL,
    created_at    timestamp   NOT NULL,
    modified_at   timestamp   NOT NULL,
    deleted_at    timestamp NULL,
    CONSTRAINT address_pk PRIMARY KEY (id_address)
);

-- Table: admin
CREATE TABLE admin
(
    id_admin_user int         NOT NULL,
    access        varchar(50) NOT NULL,
    CONSTRAINT admin_pk PRIMARY KEY (id_admin_user)
);

-- Table: business
CREATE TABLE business
(
    id_business_user    int         NOT NULL,
    first_name          varchar(30) NOT NULL,
    last_name           varchar(40) NOT NULL,
    business_name       varchar(40) NULL,
    phone_number        bigint      NOT NULL,
    verification_status varchar(30) NOT NULL,
    id_business_address int         NOT NULL,
    CONSTRAINT business_pk PRIMARY KEY (id_business_user)
);

-- Table: business_app_problem
CREATE TABLE app_problem
(
    id_app_problem serial       NOT NULL,
    created_at     timestamp    NOT NULL,
    concern        varchar(100) NOT NULL,
    description    varchar(500) NOT NULL,
    id_user        int          NOT NULL,
    CONSTRAINT app_problem_pk PRIMARY KEY (id_app_problem)
);

alter table app_problem
    add resolved_at timestamp;

-- Table: catering
CREATE TABLE catering
(
    id_catering         serial       NOT NULL,
    name                varchar(100) NOT NULL,
    email               varchar(100) NOT NULL,
    phone_number        bigint       NOT NULL,
    service_cost        decimal(8, 2) NULL,
    description         varchar(500) NULL,
    created_at          timestamp    NOT NULL,
    modified_at         timestamp    NOT NULL,
    deleted_at          timestamp NULL,
    id_business         int          NOT NULL,
    id_catering_address int          NOT NULL,
    CONSTRAINT catering_pk PRIMARY KEY (id_catering)
);

-- Table: catering_image
CREATE TABLE catering_image
(
    id_catering_image serial       NOT NULL,
    image             varchar(300) NOT NULL,
    alt               varchar(50)  NOT NULL,
    id_catering       int          NOT NULL,
    CONSTRAINT catering_image_pk PRIMARY KEY (id_catering_image)
);

-- Table: catering_item
CREATE TABLE catering_item
(
    id_catering_item serial        NOT NULL,
    name             varchar(50)   NOT NULL,
    type             varchar(10)   NOT NULL,
    description      varchar(200) NULL,
    serving_price    decimal(6, 2) NOT NULL,
    created_at       timestamp     NOT NULL,
    modified_at      timestamp     NOT NULL,
    is_vegan         boolean       NOT NULL,
    is_vegetarian    boolean       NOT NULL,
    is_gluten_free   boolean       NOT NULL,
    id_catering      int           NOT NULL,
    CONSTRAINT catering_item_pk PRIMARY KEY (id_catering_item)
);

-- Table: catering_location
CREATE TABLE catering_location
(
    id_catering int NOT NULL,
    id_location int NOT NULL,
    CONSTRAINT catering_location_pk PRIMARY KEY (id_catering, id_location)
);

-- Table: catering_review
CREATE TABLE catering_review
(
    id_catering_review serial       NOT NULL,
    title              varchar(100) NOT NULL,
    comment            varchar(300) NOT NULL,
    created_at         timestamp    NOT NULL,
    id_catering        int          NOT NULL,
    star_rating        int          NOT NULL,
    id_customer        int          NOT NULL,
    CONSTRAINT catering_review_pk PRIMARY KEY (id_catering_review)
);

-- Table: customer
CREATE TABLE customer
(
    id_customer_user int         NOT NULL,
    first_name       varchar(30) NOT NULL,
    last_name        varchar(40) NOT NULL,
    birthdate        date        NOT NULL,
    phone_number     bigint      NOT NULL,
    CONSTRAINT customer_pk PRIMARY KEY (id_customer_user)
);

-- Table: description_item
CREATE TABLE description_item
(
    name        varchar(40) NOT NULL,
    description varchar(100) NULL,
    CONSTRAINT description_item_pk PRIMARY KEY (name)
);

-- Table: event_type
CREATE TABLE event_type
(
    id_event_type serial      NOT NULL,
    type          varchar(30) NOT NULL,
    CONSTRAINT event_type_pk PRIMARY KEY (id_event_type)
);

create
    unique index event_type_type_uindex
    on event_type (type);

-- Table: guest
CREATE TABLE guest
(
    id_guest    serial       NOT NULL,
    first_name  varchar(30)  NOT NULL,
    last_name   varchar(40)  NOT NULL,
    email       varchar(100) NOT NULL,
    created_at  timestamp    NOT NULL,
    modified_at timestamp    NOT NULL,
    id_customer int          NOT NULL,
    CONSTRAINT guest_pk PRIMARY KEY (id_guest)
);

-- Table: location
CREATE TABLE location
(
    id_location         serial        NOT NULL,
    name                varchar(50)   NOT NULL,
    email               varchar(100)  NOT NULL,
    phone_number        bigint        NOT NULL,
    seating_capacity    int           NOT NULL,
    standing_capacity   int           NOT NULL,
    description         varchar(300) NULL,
    daily_rent_cost     decimal(8, 2) NOT NULL,
    size_in_sq_meters   int NULL,
    created_at          timestamp     NOT NULL,
    modified_at         timestamp     NOT NULL,
    deleted_at          timestamp NULL,
    id_business         int           NOT NULL,
    id_location_address int           NOT NULL,
    CONSTRAINT location_pk PRIMARY KEY (id_location)
);

-- Table: location_description
CREATE TABLE location_description
(
    id_location int         NOT NULL,
    name        varchar(40) NOT NULL,
    CONSTRAINT location_description_pk PRIMARY KEY (id_location, name)
);

-- Table: location_for_event
CREATE TABLE location_for_event
(
    id_location_for_event serial  NOT NULL,
    time_from             time    NOT NULL,
    time_to               time NULL,
    guests                int     NOT null,
    confirmation_status   varchar NOT null,
    id_organized_event    int     NOT NULL,
    id_location           int     NOT NULL,
    CONSTRAINT location_for_event_pk PRIMARY KEY (id_location_for_event)
);

-- Table: location_image
CREATE TABLE location_image
(
    id_location_image serial       NOT NULL,
    image             varchar(300) NOT NULL,
    alt               varchar(50)  NOT NULL,
    id_location       int          NOT NULL,
    CONSTRAINT location_image_pk PRIMARY KEY (id_location_image)
);

-- Table: location_review
CREATE TABLE location_review
(
    id_location_review serial       NOT NULL,
    title              varchar(100) NOT NULL,
    comment            varchar(300) NOT NULL,
    star_rating        int          NOT NULL,
    created_at         timestamp    NOT NULL,
    id_location        int          NOT NULL,
    id_customer        int          NOT NULL,
    CONSTRAINT location_review_pk PRIMARY KEY (id_location_review)
);

-- Table: optional_service
CREATE TABLE optional_service
(
    id_optional_service         serial        NOT NULL,
    first_name                  varchar(50)   NOT NULL,
    last_name                   varchar(50)   NOT NULL,
    "alias"                     varchar(50) NULL,
    type                        varchar(50)   NOT NULL,
    email                       varchar(100)  NOT NULL,
    description                 varchar(300)  NOT NULL,
    service_cost                decimal(8, 2) NOT NULL,
    created_at                  timestamp     NOT NULL,
    modified_at                 timestamp     NOT NULL,
    kids_performer_type         varchar(40) NULL,
    age_from                    int NULL,
    age_to                      int NULL,
    people_count                int NULL,
    instrument                  varchar(40) NULL,
    deleted_at                  timestamp NULL,
    id_business                 int           NOT NULL,
    id_optional_service_address int           NOT NULL,
    CONSTRAINT optional_service_pk PRIMARY KEY (id_optional_service)
);

-- Table: service_translator_language
CREATE TABLE service_translator_language
(
    id_optional_service int NOT NULL,
    id_language         int NOT NULL,
    CONSTRAINT service_translator_language_pk PRIMARY KEY (id_optional_service, id_language)
);

-- Table: translation_language
CREATE TABLE translation_language
(
    id_language serial      NOT NULL,
    name        varchar(40) NOT NULL,
    CONSTRAINT translation_language_pk PRIMARY KEY (id_language)
);

-- Table: music_style
CREATE TABLE music_style
(
    id_music_style serial      NOT NULL,
    name           varchar(40) NOT NULL,
    CONSTRAINT music_style_pk PRIMARY KEY (id_music_style)
);

-- Table: service_music_style
CREATE TABLE service_music_style
(
    id_optional_service int NOT NULL,
    id_music_style      int NOT NULL,
    CONSTRAINT service_music_style_pk PRIMARY KEY (id_optional_service, id_music_style)
);

-- Table: optional_service_image
CREATE TABLE optional_service_image
(
    id_optional_service_image serial       NOT NULL,
    image                     varchar(300) NOT NULL,
    alt                       varchar(50)  NOT NULL,
    id_optional_service       int          NOT NULL,
    CONSTRAINT optional_service_image_pk PRIMARY KEY (id_optional_service_image)
);

-- Table: organized_event
CREATE TABLE organized_event
(
    id_organized_event serial      NOT NULL,
    name               varchar(50) NOT NULL,
    event_date         date        NOT NULL,
    start_time         time        NOT NULL,
    end_time           time        NOT NULL,
    event_status       varchar(30) NOT NULL,
    guest_count        int         NOT NULL,
    created_at         timestamp   NOT NULL,
    modified_at        timestamp   NOT NULL,
    deleted_at         timestamp NULL,
    id_event_type      int         NOT NULL,
    id_customer        int         NOT NULL,
    CONSTRAINT organized_event_pk PRIMARY KEY (id_organized_event)
);

-- Table: organized_event_guest
CREATE TABLE event_guest
(
    id_guest           int NOT NULL,
    id_organized_event int NOT NULL,
    CONSTRAINT event_guest_pk PRIMARY KEY (id_guest, id_organized_event)
);

-- Table: service_for_event
CREATE TABLE service_for_event
(
    id_chosen_service_for_event serial       NOT NULL,
    time_from                   timestamp    NOT NULL,
    time_to                     timestamp    NOT NULL,
    comment                     varchar(200) NOT NULL,
    id_optional_service         int          NOT NULL,
    id_location_for_event       int          NOT NULL,
    CONSTRAINT service_for_event_pk PRIMARY KEY (id_chosen_service_for_event)
);

-- Table: service_review
CREATE TABLE service_review
(
    id_service_review   serial       NOT NULL,
    title               varchar(100) NOT NULL,
    comment             varchar(300) NOT NULL,
    star_rating         int          NOT NULL,
    created_at          timestamp    NOT NULL,
    id_optional_service int          NOT NULL,
    id_customer         int          NOT NULL,
    CONSTRAINT service_review_pk PRIMARY KEY (id_service_review)
);

-- Table: catering_for_chosen_location
CREATE TABLE catering_for_chosen_location
(
    id_catering_for_chosen_location serial       NOT NULL,
    order_time                      time         NOT NULL,
    order_date                      date         NOT NULL,
    comment                         varchar(300) NOT NULL,
    confirmation_status             varchar(30)  NOT NULL,
    id_catering                     int          NOT NULL,
    id_location_for_event           int          NOT NULL,
    CONSTRAINT catering_for_chosen_location_pk PRIMARY KEY (id_catering_for_chosen_location)
);

-- Table: catering_order_choice
CREATE TABLE catering_order_choice
(
    id_catering_item                serial NOT NULL,
    count                           int    NOT NULL,
    id_catering_for_chosen_location int    NOT NULL,
    CONSTRAINT catering_order_choice_pk PRIMARY KEY (id_catering_item)
);

create table location_business_hours
(

    id_business_hours serial      NOT NULL,
    day               varchar(20) NOT NULL,
    time_from         time        NOT NULL,
    time_to           time        NOT NULL,
    id_location       int NULL,
    CONSTRAINT location_business_hours_pk PRIMARY KEY (id_business_hours)
);

create table catering_business_hours
(

    id_business_hours serial      NOT NULL,
    day               varchar(20) NOT NULL,
    time_from         time        NOT NULL,
    time_to           time        NOT NULL,
    id_catering       int NULL,
    CONSTRAINT catering_business_hours_pk PRIMARY KEY (id_business_hours)
);

create table optional_service_business_hours
(

    id_business_hours   serial      NOT NULL,
    day                 varchar(20) NOT NULL,
    time_from           time        NOT NULL,
    time_to             time        NOT NULL,
    id_optional_service int NULL,
    CONSTRAINT optional_service_business_hours_pk PRIMARY KEY (id_business_hours)
);


--REFERENCES
ALTER TABLE catering_for_chosen_location
    ADD CONSTRAINT catering_for_chosen_location_location_for_event
        FOREIGN KEY (id_location_for_event)
            REFERENCES location_for_event (id_location_for_event)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_order_catering (table: catering_for_chosen_location)
ALTER TABLE catering_for_chosen_location
    ADD CONSTRAINT catering_order_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_order_choice_catering_for_chosen_location (table: catering_order_choice)
ALTER TABLE catering_order_choice
    ADD CONSTRAINT catering_order_choice_catering_for_chosen_location
        FOREIGN KEY (id_catering_for_chosen_location)
            REFERENCES catering_for_chosen_location (id_catering_for_chosen_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_order_choice_catering_item (table: catering_order_choice)
ALTER TABLE catering_order_choice
    ADD CONSTRAINT catering_order_choice_catering_item
        FOREIGN KEY (id_catering_item)
            REFERENCES catering_item (id_catering_item)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;


-- foreign keys
-- Reference: admin_user (table: admin)
ALTER TABLE admin
    ADD CONSTRAINT admin_user
        FOREIGN KEY (id_admin_user)
            REFERENCES users (id_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: business_address (table: business)
ALTER TABLE business
    ADD CONSTRAINT business_address
        FOREIGN KEY (id_business_address)
            REFERENCES address (id_address)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;


-- Reference: business_user (table: business)
ALTER TABLE business
    ADD CONSTRAINT business_user
        FOREIGN KEY (id_business_user)
            REFERENCES users (id_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_address (table: catering)
ALTER TABLE catering
    ADD CONSTRAINT catering_address
        FOREIGN KEY (id_catering_address)
            REFERENCES address (id_address)
            ON DELETE CASCADE
            ON UPDATE CASCADE
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_address (table: catering)
ALTER TABLE optional_service
    ADD CONSTRAINT service_address
        FOREIGN KEY (id_service_address)
            REFERENCES address (id_address)
            ON DELETE CASCADE
            ON UPDATE CASCADE
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_business (table: catering)
ALTER TABLE catering
    ADD CONSTRAINT catering_business
        FOREIGN KEY (id_business)
            REFERENCES business (id_business_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_for_location_catering (table: catering_location)
ALTER TABLE catering_location
    ADD CONSTRAINT catering_for_location_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_image_catering (table: catering_image)
ALTER TABLE catering_image
    ADD CONSTRAINT catering_image_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_item_catering (table: catering_item)
ALTER TABLE catering_item
    ADD CONSTRAINT catering_item_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_location_location (table: catering_location)
ALTER TABLE catering_location
    ADD CONSTRAINT catering_location_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_review_catering (table: catering_review)
ALTER TABLE catering_review
    ADD CONSTRAINT catering_review_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: catering_review_customer (table: catering_review)
ALTER TABLE catering_review
    ADD CONSTRAINT catering_review_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: customer_user (table: customer)
ALTER TABLE customer
    ADD CONSTRAINT customer_user
        FOREIGN KEY (id_customer_user)
            REFERENCES users (id_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: guest_customer (table: guest)
ALTER TABLE guest
    ADD CONSTRAINT guest_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_address (table: location)
ALTER TABLE location
    ADD CONSTRAINT location_address
        FOREIGN KEY (id_location_address)
            REFERENCES address (id_address)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_business (table: location)
ALTER TABLE location
    ADD CONSTRAINT location_business
        FOREIGN KEY (id_business)
            REFERENCES business (id_business_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_description_description_item (table: location_description)
ALTER TABLE location_description
    ADD CONSTRAINT location_description_description_item
        FOREIGN KEY (name)
            REFERENCES description_item (name)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_description_location (table: location_description)
ALTER TABLE location_description
    ADD CONSTRAINT location_description_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_for_event_location (table: location_for_event)
ALTER TABLE location_for_event
    ADD CONSTRAINT location_for_event_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_for_event_organized_event (table: location_for_event)
ALTER TABLE location_for_event
    ADD CONSTRAINT location_for_event_organized_event
        FOREIGN KEY (id_organized_event)
            REFERENCES organized_event (id_organized_event)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;


-- Reference: location_image_location (table: location_image)
ALTER TABLE location_image
    ADD CONSTRAINT location_image_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_review_customer (table: location_review)
ALTER TABLE location_review
    ADD CONSTRAINT location_review_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: location_review_location (table: location_review)
ALTER TABLE location_review
    ADD CONSTRAINT location_review_location
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: optional_service_business (table: optional_service)
ALTER TABLE optional_service
    ADD CONSTRAINT optional_service_business
        FOREIGN KEY (id_business)
            REFERENCES business (id_business_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: optional_service_image_optional_service (table: optional_service_image)
ALTER TABLE optional_service_image
    ADD CONSTRAINT optional_service_image_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: organized_event_customer (table: organized_event)
ALTER TABLE organized_event
    ADD CONSTRAINT organized_event_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: organized_event_event_type (table: organized_event)
ALTER TABLE organized_event
    ADD CONSTRAINT organized_event_event_type
        FOREIGN KEY (id_event_type)
            REFERENCES event_type (id_event_type)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

ALTER TABLE event_guest
    ADD CONSTRAINT event_guest_guest
        FOREIGN KEY (id_guest)
            REFERENCES guest (id_guest)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

ALTER TABLE event_guest
    ADD CONSTRAINT event_event_guest
        FOREIGN KEY (id_organized_event)
            REFERENCES organized_event (id_organized_event)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_for_event_location_for_event (table: service_for_event)
ALTER TABLE service_for_event
    ADD CONSTRAINT service_for_event_location_for_event
        FOREIGN KEY (id_location_for_event)
            REFERENCES location_for_event (id_location_for_event)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_for_event_optional_service (table: service_for_event)
ALTER TABLE service_for_event
    ADD CONSTRAINT service_for_event_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_review_customer (table: service_review)
ALTER TABLE service_review
    ADD CONSTRAINT service_review_customer
        FOREIGN KEY (id_customer)
            REFERENCES customer (id_customer_user)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_review_optional_service (table: service_review)
ALTER TABLE service_review
    ADD CONSTRAINT service_review_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

ALTER TABLE location_business_hours
    ADD CONSTRAINT location_location_business_hours
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

ALTER TABLE catering_business_hours
    ADD CONSTRAINT catering_catering_business_hours
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

ALTER TABLE optional_service_business_hours
    ADD CONSTRAINT optional_service_optional_service_business_hours
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_translator_language_optional_service (table: service_translator_language)
ALTER TABLE service_translator_language
    ADD CONSTRAINT service_translator_language_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_translator_language_translation_language (table: service_translator_language)
ALTER TABLE service_translator_language
    ADD CONSTRAINT service_translator_language_translation_language
        FOREIGN KEY (id_language)
            REFERENCES translation_language (id_language)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_music_style_music_style (table: service_music_style)
ALTER TABLE service_music_style
    ADD CONSTRAINT service_music_style_music_style
        FOREIGN KEY (id_music_style)
            REFERENCES music_style (id_music_style)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Reference: service_translator_language_optional_service (table: service_music_style)
ALTER TABLE service_music_style
    ADD CONSTRAINT service_translator_language_optional_service
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

create table location_availability
(
    id_availability serial      NOT NULL,
    date            timestamp   NOT NULL,
    time_from       timestamp   NOT NULL,
    time_to         timestamp   NOT NULL,
    status          varchar(30) not null,
    id_location     int         NOT NULL,
    CONSTRAINT location_availability_pk PRIMARY KEY (id_availability)
);

-- Reference: location_location_availability (table: location_availability)
ALTER TABLE location_availability
    ADD CONSTRAINT location_location_availability
        FOREIGN KEY (id_location)
            REFERENCES location (id_location)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

create table catering_availability
(
    id_availability serial      NOT NULL,
    status          varchar(30) NOT NULL,
    date            timestamp   NOT NULL,
    time_from       timestamp   NOT NULL,
    time_to         timestamp   NOT NULL,
    id_catering     int         NOT NULL,
    CONSTRAINT catering_availability_pk PRIMARY KEY (id_availability)
);

-- Reference: catering_catering_availability (table: catering_availability)
ALTER TABLE catering_availability
    ADD CONSTRAINT catering_catering_availability
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;


create table optional_service_availability
(
    id_availability     serial      NOT NULL,
    status              varchar(30) NOT NULL,
    date                timestamp   NOT NULL,
    time_from           timestamp   NOT NULL,
    time_to             timestamp   NOT NULL,
    id_optional_service int         NOT NULL,
    CONSTRAINT optional_service_availability_pk PRIMARY KEY (id_availability)
);

-- Reference: catering_catering_availability (table: catering_availability)
ALTER TABLE optional_service_availability
    ADD CONSTRAINT optional_service_optional_service_availability
        FOREIGN KEY (id_optional_service)
            REFERENCES optional_service (id_optional_service)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

-- Table: catering_cuisine
CREATE TABLE catering_cuisine
(
    id_catering int NOT NULL,
    id_cuisine  int NOT NULL,
    CONSTRAINT catering_cuisine_pk PRIMARY KEY (id_catering, id_cuisine)
);

-- Table: cuisine
CREATE TABLE cuisine
(
    id_cuisine serial      NOT NULL,
    name       varchar(50) NOT NULL,
    CONSTRAINT cuisine_pk PRIMARY KEY (id_cuisine)
);

-- foreign keys
-- Reference: catering_cuisine_catering (table: catering_cuisine)
ALTER TABLE catering_cuisine
    ADD CONSTRAINT catering_cuisine_catering
        FOREIGN KEY (id_catering)
            REFERENCES catering (id_catering)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

CREATE TABLE customer_avatar
(
    id_avatar serial NOT NULL,
    image     bytea  NOT NULL,
    CONSTRAINT customer_avatar_pk PRIMARY KEY (id_avatar)
);

alter table customer
    add id_avatar int default null;

-- Reference: user_user_avatar (table: user)
ALTER TABLE customer
    ADD CONSTRAINT user_user_avatar
        FOREIGN KEY (id_avatar)
            REFERENCES customer_avatar (id_avatar)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;