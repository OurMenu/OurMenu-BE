use users_db

create table article (
                         menu_count integer not null,
                         views integer not null,
                         article_id bigint not null auto_increment,
                         created_at datetime(6),
                         modified_at datetime(6),
                         user_id bigint,
                         content varchar(255),
                         title varchar(255),
                         status enum ('CREATED','DELETED','UPDATED'),
                         primary key (article_id)
) engine=InnoDB

create table article_menu (
                              price integer not null,
                              article_id bigint,
                              article_menu_id bigint not null auto_increment,
                              group_id bigint,
                              address varchar(255),
                              img_url varchar(255),
                              place_title varchar(255),
                              title varchar(255),
                              primary key (article_menu_id)
) engine=InnoDB;

create table jdbc_entity (
                             id bigint not null auto_increment,
                             name varchar(255),
                             primary key (id)
) engine=InnoDB;

create table menu (
                      price integer not null,
                      created_at datetime(6),
                      group_id bigint,
                      menu_id bigint not null auto_increment,
                      menulist_id bigint,
                      modified_at datetime(6),
                      place_id bigint,
                      user_id bigint,
                      memo varchar(255),
                      memo_title varchar(255),
                      menu_icon_type varchar(255),
                      title varchar(255),
                      status enum ('CREATED','DELETED','UPDATED'),
                      primary key (menu_id)
) engine=InnoDB

create table menu_image (
                            menu_id bigint,
                            menu_image_id bigint not null auto_increment,
                            url varchar(255),
                            primary key (menu_image_id)
) engine=InnoDB;

create table menu_list (
                           created_at datetime(6),
                           menulist_id bigint not null auto_increment,
                           modified_at datetime(6),
                           priority bigint,
                           user_id bigint,
                           icon_type varchar(255),
                           title varchar(255),
                           image tinytext,
                           status enum ('CREATED','DELETED','UPDATED'),
                           primary key (menulist_id)
) engine=InnoDB;

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