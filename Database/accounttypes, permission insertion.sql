-- ----------------------------------------------
-- Adds all the permissions available to the 
-- permissions table, these will all be constant
-- for the most part.
-- ----------------------------------------------
insert into permissions (Name) values ('public');
insert into permissions (Name) values ('private');
insert into permissions (Name) values ('shared');

-- -----------------------------------------------
-- adds the different account types to the 
-- accounttypes table, this information will be
-- mostly constant.
-- -----------------------------------------------
insert into accounttypes (Name) values('regular');
insert into accounttypes (Name) values('admin');
insert into accounttypes (Name) values('premium');
insert into accounttypes (Name) values('pending');
