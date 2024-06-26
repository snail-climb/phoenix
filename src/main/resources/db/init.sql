create database if not exists hive_lineage;

use hive_lineage;

create table if not exists raw_lineage
(
    hql_key         varchar(64) primary key comment 'hql key',
    raw_lineage     longtext comment '原始血缘信息',
    exec_start_time datetime comment '执行开始时间',
    exec_spend_ms   bigint comment '执行耗时',
    create_time     datetime default current_timestamp comment '创建时间'
) comment '原始血缘信息';

create table if not exists field_lineage
(
    target_hql_key   varchar(64) comment '目标表hql key',
    target_db        varchar(255) comment '目标库',
    target_table     varchar(255) comment '目标表',
    target_field     varchar(255) comment '目标字段',
    downstream_db    varchar(255) comment '下游库',
    downstream_table varchar(255) comment '下游表',
    downstream_filed varchar(255) comment '下游字段',
    create_time      datetime default current_timestamp comment '创建时间',
    primary key (target_hql_key, target_db, target_table, target_field)
) comment '字段血缘表';
