
create database if not exists seatreservation;
use seatreservation;
-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    userName      varchar(256)                           null comment '用户昵称',
    userAccount   varchar(256)                           not null comment '账号',
    userAvatar    varchar(256)                           null comment '用户头像',
    gender        tinyint                                null comment '性别',
    userRole      varchar(256) default 'user'            not null comment '用户角色：user / admin',
    userPassword  varchar(512)                           not null comment '密码',
    status        tinyint      default 0                 not null comment '状态（0为正常 1为违规）',
    violationTime datetime comment '违规时间',
    createTime    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';



create table if not exists library
(
    id         bigint auto_increment comment '图书馆id'
        primary key,
    name       varchar(255)                       not null comment '图书馆名称',
    img        varchar(255)                       not null comment '图片',
    seatRow    int                                not null comment '座位行数',
    seatCol    int                                not null comment '座位列',
    num        int                                not null comment '总座位数量',
    useNum     int                                not null comment '使用数量数量',
    isDelete   tinyint  default 0                 not null comment '是否删除 0为没删除 1为删除',
    status     tinyint  default 0                 not null comment '状态 0为可用 1为不可用',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    constraint id
        unique (id)
) comment '图书馆'

