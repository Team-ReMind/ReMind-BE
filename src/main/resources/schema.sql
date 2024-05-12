drop
database IF EXISTS remind;

create
database IF NOT EXISTS remind;

use
remind;

create table member
(
    id                     bigint not null auto_increment primary key,
    auth_id                bigint,
    name                   varchar(255),
    age                    int,
    gender                 varchar(5),
    email                  varchar(30),
    phone_number           varchar(20),
    profile_image_url      varchar(50),
    is_onboarding_finished boolean,
    registration_token     varchar(255),
    roles_type             varchar(255),
    protector_phone_number varchar(20),
    city                   varchar(50), 
    district               varchar(50),  
    center_name            varchar(50)   
);

create table mood
(
    id           bigint not null auto_increment primary key,
    patient_id   bigint not null,
    feeling_type varchar(20),
    mood_detail  text,
    mood_date    date,
    foreign key (patient_id) references member (id)
);

create table activity
(
    id            bigint not null auto_increment primary key,
    patient_id    bigint not null,
    activity_name varchar(20),
    activity_icon varchar(255),
    foreign key (patient_id) references member (id)
);

create table mood_activity
(
    id                   bigint not null auto_increment primary key,
    mood_id              bigint not null,
    activity_id          bigint not null,
    mood_activity_detail text,
    feeling_type         varchar(20),
    foreign key (mood_id) references mood (id),
    foreign key (activity_id) references activity (id)
);

create index idx_auth on member (auth_id, roles_type);
