insert into permissions (Name) values ('public');
insert into permissions (Name) values ('private');
insert into permissions (Name) values ('shared');

insert into accounttypes (Name) values('regular');
insert into accounttypes (Name) values('admin');
insert into accounttypes (Name) values('premium');
insert into accounttypes (Name) values('pending');

select * from users;
delete from users where ID = 16

select * from accounttypes order by ID