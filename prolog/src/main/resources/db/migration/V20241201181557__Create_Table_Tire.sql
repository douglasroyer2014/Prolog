create table tire(
	id uuid not null,
    position_number int,
	"position" varchar(255),
	mark varchar(255),
	pressure int,
	status_type varchar(255),
	vehicle uuid
);

alter table tire add primary key (id);
alter table tire add constraint fk_tire_vehicle_id foreign key (vehicle) references vehicle (id);
