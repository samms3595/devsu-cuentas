create database cuentas;
use cuentas;

create table cuenta
(id bigint not null,
estado tinyint check (estado between 0 and 1),
id_cliente bigint,
numero_cuenta bigint,
saldo_actual float(53) not null,
saldo_inicial float(53) not null,
tipo_cuenta tinyint check (tipo_cuenta between 0 and 1),
primary key (id)) engine=InnoDB;

create table movimiento (
    id bigint not null,
    fecha date,
    saldo float(53) not null,
    tipo_movimiento enum ('DEPOSITO','RETIRO'),
    valor float(53) not null,
    cuenta_id bigint,
    primary key (id)) engine=InnoDB;

CREATE USER 'cuentas'@'localhost' IDENTIFIED BY 'Devsu123.';
GRANT ALL PRIVILEGES ON cuentas TO 'cuentas'@'localhost';
FLUSH PRIVILEGES;