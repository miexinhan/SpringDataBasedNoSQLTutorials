/************************************************************************************
table column information
************************************************************************************/
SELECT 
    COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM
    information_schema.COLUMNS
WHERE TABLE_SCHEMA='cloudbill' 
	AND TABLE_NAME='business_audit_phase_mch_user';

/************************************************************************************
table references - active
************************************************************************************/
SELECT 
    concat(table_name, '.', column_name) as 'foreign key',
    concat(referenced_table_name,  '.',  referenced_column_name) as 'referenced column'
FROM
    information_schema.key_column_usage
WHERE
    referenced_table_name IS NOT NULL
        AND table_schema = 'cloudbill' 
		AND TABLE_NAME = 'business_audit_phase_mch_user';

/************************************************************************************
table references - passive
************************************************************************************/
SELECT 
	kcu.CONSTRAINT_NAME, 
	concat(kcu.TABLE_NAME, '.', kcu.COLUMN_NAME) AS active,
	concat(kcu.REFERENCED_TABLE_NAME, '.', kcu.REFERENCED_COLUMN_NAME) AS passive
FROM information_schema.key_column_usage kcu
WHERE kcu.REFERENCED_TABLE_SCHEMA = kcu.TABLE_SCHEMA AND kcu.TABLE_SCHEMA = 'cloudbill'
AND kcu.REFERENCED_TABLE_NAME = 'business_audit_phase_mch_user';








/************************************************************************************
important things
************************************************************************************/
# important `Edit` -> Preferences -> SQL Queries[Safe updates]


/**
模拟FULL OUTER JOIN
*/
/**
create table test1(id int primary key, name varchar(55));
create table test2(id int primary key, name varchar(55));

insert into test1(id, name) values
(1, "1"),(2, "2"), (3, "3");

insert into test2(id, name) values
(2, "2"), (3, "3"), (4, "4");

select * from test1;
select * from test2;

-- FULL OUTER JOIN
select t1.id, t2.id from test1 t1 LEFT JOIN test2 t2 ON (t1.id = t2.id)
UNION
select t1.id, t2.id from test1 t1 RIGHT JOIN test2 t2 ON (t1.id = t2.id);


DROP TABLE `billing_data`.`bill_template`;
DROP TABLE `billing_data`.`bill_template_type`;

**/
