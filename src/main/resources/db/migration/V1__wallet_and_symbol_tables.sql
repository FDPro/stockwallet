create table SYMBOL (
  CODE varchar(5) primary key,
  NAME varchar(50) not null
);

create table WALLET (
  NAME varchar(50) primary key
);

create table CASH_FLOW (
  ID bigserial primary key,
  WALLET_NAME varchar(50) not null,
  CASH_FLOW_DATE date not null,
  TYPE varchar(20) not null,
  AMOUNT varchar(50) not null,

  constraint CASH_FLOW_WALLET_FK foreign key(WALLET_NAME) references WALLET
);

create table TRANSACTION (
  ID bigserial primary key,
  WALLET_NAME varchar(50) not null,
  SYMBOL varchar(5) not null,
  TRANSACTION_DATE date not null,
  UNITS int not null,
  UNIT_PRICE varchar(50) not null,
  TRANSACTION_COST varchar(50) not null,

  constraint TRANSACTION_WALLET_FK foreign key(WALLET_NAME) references WALLET,
  constraint TRANSACTION_SYMBOL_FK foreign key(SYMBOL) references SYMBOL
);