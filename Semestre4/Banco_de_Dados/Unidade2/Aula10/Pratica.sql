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