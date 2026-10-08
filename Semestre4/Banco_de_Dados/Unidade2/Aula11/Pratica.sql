create table departamento (
	id_dep serial,
	nome_dep text not null,
	predio text not null,
	orcamento float not null,
	primary key (id_dep)
);

create table aluno (
	id_alu serial,
	nome_alu text not null,
	matricula bigint unique not null,
	telefone bigint not null,
	cidade text not null,
	nacionalidade text default 'Brasileiro' not null,
	primary key (id_alu)
);

create table disciplina (
	id_dis serial,
	nome_dis text not null,
	credito int not null,
	id_dep int not null,
	primary key (id_dis),
	foreign key (id_dep) references departamento on update cascade on delete cascade
);

create table instrutor (
	id_ins serial,
	nome_ins text not null,
	salario float check (salario between 5000 and 8000) not null,
	id_dep int not null,
	primary key (id_ins),
	foreign key (id_dep) references departamento on update cascade on delete cascade
);

create table ministra (
	id_min serial,
	ano int not null,
	semestre int not null,
	id_ins int not null,
	id_dis int not null,
	primary key (id_min),
	foreign key (id_ins) references instrutor on update cascade on delete cascade,
	foreign key (id_dis) references disciplina on update cascade on delete cascade
);

drop table ministra;

alter table instrutor
rename to professor;

alter table professor
rename id_ins to id_pro;

alter table professor
rename nome_ins to nome_pro;

create table ministra (
	id_min serial,
	ano int not null, 
	semestre int not null,
	turma int not null,
	id_pro int not null,
	id_dis int not null
);

alter table ministra
add primary key (id_min);

alter table ministra
add foreign key (id_pro) references professor
on update cascade on delete cascade,
add foreign key (id_dis) references disciplina
on update cascade on delete cascade;

alter table professor
add cpf bigint;

alter table professor
add unique (cpf);

alter table disciplina
add check (credito in (2, 4, 6));

alter table professor 
alter cpf type text;

alter table professor
alter cpf set not null;

alter table disciplina
alter credito set default 4;

alter table professor
drop cpf;

insert into departamento (nome_dep, predio, orcamento)
values ('Ciências Exatas', 'DCE-A', 2000000);


insert into departamento (id_dep, nome_dep, orcamento, predio)
values (1, 'Ciências Exatas', 2000000, 'DCE-A'),
	   (2, 'Engenharias', 5000000, 'DE-A')
on conflict (id_dep) do nothing;

insert into departamento (id_dep, nome_dep, predio, orcamento)
values (2, 'Engenharias', 'DE-A', 7000000),
(3, 'Ciências da Saúde', 'DCS-A', 10000000),
(4, 'Ciências Agrárias', 'DCA-A', 2000000),
(5, 'Ciências Humanas', 'DCH', 2000000)
on conflict (id_dep) do update
set nome_dep = excluded.nome_dep,
predio = excluded.predio,
orcamento = excluded.orcamento;

insert into professor (nome_pro, salario, id_dep)
values ('Paulo', 8000, 1),
('Cíntia', 6000, 1),
('Sara', 6000, 2),
('Felipe', 8000, 3),
('Tereza', 6000, 4),
('Ricardo', 6000, 5);

insert into disciplina (nome_dis, credito, id_dep)
values ('Estatística', 4, 1),
('Topografia', 4, 2),
('Anatomia', 4, 3),
('Controle de plantas daninhas', 4, 4),
('Filosofia', 4, 5);

insert into aluno (nome_alu, matricula, telefone, cidade)
values ('João', 111111111, 996111111, 'Patu'),
('Maria', 222222222, 996222222, 'Apodi'),
('José', 333333333, 996333333, 'Pau dos Ferros');

update professor
set salario = 7000;

update professor
set salario = salario * 1.10;

update disciplina
set credito = 6
where id_dis = 3;

update departamento
set orcamento = orcamento * 1.05
where orcamento = 2000000 or id_dep = 2;

delete
from professor;

select *
from departamento;