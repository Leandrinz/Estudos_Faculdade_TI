Create table Doador (
	codDoa Serial primary key,
	Nome text,
	Email Text Unique,
	Telefone Text,
	CPF Varchar(11) Unique,
	TipoSang Varchar(3),
	Rua Text,
	Numero Text,
	Bairro Text,
	Cidade Text,
	Estado Text
);

Create table Paciente (
	CodPaci Serial primary key,
	Nome text,
	Email text unique,
	telefone text,
	cpf varchar(11) unique,
	TipoSang varchar(3),
	rua text,
	numero text,
	bairro text,
	cidade text
);

Create table Bolsa_de_Sangue (
	CodBolsa Serial primary key,
	DataVali date,
	DataDoa date,
	DataTrans date,
	CodDoa Serial,
	foreign key (CodDoa) references Doador
	on update cascade on delete cascade,
	CodPaci Serial,
	foreign key (CodPaci) references Paciente
	on update cascade on delete cascade
);