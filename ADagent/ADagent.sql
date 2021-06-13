// EmployeeTable
create table tEmployee(
eID int not null identity primary key,
eName char(64),
ePassword char (32)
)
drop table tEmployee

insert into tEmployee(eName,ePassword) values('Johne','123')
insert into tEmployee(eName,ePassword) values('Thomas','456')

select * from tEmployee

create table tOrder(
eID int constraint FK_eID foreign key references tEmployee(eID),
oID int not null identity primary key,
cName char(64),
aCont smallint constraint FK_aCont foreign key references tContType(cID),
aSize smallint constraint FK_aSize foreign key references tSizeType(sID),
aPage smallint constraint FK_aPage foreign key references tPageType(pID),
aBill money,
aPostingDate datetime)

drop table tOrder

insert into tOrder(eID, cName, aCont, aSize, aPage, aBill,aPostingDate)  values(1, 'Toyota', 1,1,1, 1000.000,'2010-09-11')
insert into tOrder(eID, cName, aCont, aSize, aPage, aBill,aPostingDate)  values(1, 'Suzuki', 1,2,1, 500.00,'2010-09-10')
insert into tOrder(eID, cName, aCont, aSize, aPage, aBill,aPostingDate)  values(2, 'Softbank', 2,3,2, 700.00,'2010-09-09')

select * from tOrder
select * from tOrder t where t.[Size]=1
select * from tOrder t where t.eID=1

select * from tOrder where 
select p.PageName, p.PageCost from tPageType p where p.pID = 1

-- To store information in class private variable.
create table tPageType(
pID smallint not null identity primary key,
PageName char(16),
PageCost money)

insert into tPageType(PageName,PageCost) values('Top',   1000.0)
insert into tPageType(PageName,PageCost) values('Second',700.0)
insert into tPageType(PageName,PageCost) values('Middle',500.0)
insert into tPageType(PageName,PageCost) values('Last',  800.0)

drop table tPageType

create table tContType(
cID smallint not null identity primary key,
ContentName char(16),
ContentCost money)

drop table tContType

insert into tContType(ContentName,ContentCost) values('Text', 200.00)
insert into tContType(ContentName,ContentCost) values('Graphics', 500.00)

create table tSizeType(
sID smallint not null identity primary key,
SizeName char(16),
SizeWeight float)

drop table tSizeType

insert into tSizeType(SizeName,SizeWeight) values('Large',10.0)
insert into tSizeType(SizeName,SizeWeight) values('Medium', 3.0)
insert into tSizeType(SizeName,SizeWeight) values('Small',1.0)

select * from tPageType
select * from tContType
select * from tSizeType

