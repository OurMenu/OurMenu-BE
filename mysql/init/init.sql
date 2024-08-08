use users_db

create table menu_tag (
                          menu_id bigint,
                          menu_tag_id bigint not null auto_increment,
                          tag_id bigint,
                          primary key (menu_tag_id)
) engine=InnoDB;

create table place (
                       latitude float(53),
                       longitude float(53),
                       created_at datetime(6),
                       modified_at datetime(6),
                       place_id bigint not null auto_increment,
                       user_id bigint,
                       address varchar(255),
                       info varchar(255),
                       title varchar(255),
                       status enum ('CREATED','DELETED','UPDATED'),
                       primary key (place_id)
) engine=InnoDB;

create table tag (
                     is_custom bit,
                     tag_id bigint not null auto_increment,
                     name varchar(255),
                     primary key (tag_id)
) engine=InnoDB;

create table test_entity (
                             id bigint not null auto_increment,
                             name varchar(255),
                             primary key (id)
) engine=InnoDB;

create table user (
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP null,
                      modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP null,
                      user_id bigint not null auto_increment,
                      email varchar(255),
                      nickname varchar(255),
                      password varchar(255),
                      img_url tinytext,
                      status ENUM('CREATED', 'DELETED', 'UPDATED') DEFAULT 'CREATED',
                      primary key (user_id)
) engine=InnoDB;

create table user_store (
                            id bigint not null auto_increment,
                            modified_at datetime(6),
                            user_id bigint,
                            address varchar(255),
                            store_id varchar(255),
                            store_name varchar(255),
                            primary key (id)
) engine=InnoDB;
