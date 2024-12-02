create table vehicle_type (
	id uuid not null,
	"name" varchar(255),
	wheels_count smallint
);

alter table vehicle_type add primary key (id);