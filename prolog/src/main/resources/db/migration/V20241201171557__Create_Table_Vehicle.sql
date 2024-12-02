create table vehicle (
	id uuid not null,
	plate varchar(10),
	mark varchar(255),
	mileage int,
	status_type varchar(255),
	vehicle_type uuid not null
);

alter table vehicle add primary key (id);
alter table vehicle add constraint fk_vehicle_vehicle_type_id foreign key (vehicle_type) references vehicle_type (id);