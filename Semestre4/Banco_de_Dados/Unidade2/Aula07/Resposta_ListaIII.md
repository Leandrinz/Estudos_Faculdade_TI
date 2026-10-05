# Lista de Exercícios III — Normalização

**Componente:** Banco de Dados

## Questão 1

Esquema original (chave primária composta `{CodAlu, CodDisc}`):

```
Matricula (CodAlu, CodDisc, NomeDisc, NomeAlu, CodLocalNascAlu, NomeLocalNascAlu)
```

### Dependências funcionais

- `CodDisc → NomeDisc`
- `CodAlu → NomeAlu, CodLocalNascAlu, NomeLocalNascAlu`
- `CodLocalNascAlu → NomeLocalNascAlu`

### 1FN (Primeira Forma Normal)

Uma relação está na 1FN quando todos os atributos são atômicos (sem atributos multivalorados ou compostos) e não há grupos repetidos.

**Resultado:** a tabela **já obedece à 1FN**, pois todos os atributos são atômicos e possui chave primária definida.

```
Matricula (CodAlu, CodDisc, NomeDisc, NomeAlu, CodLocalNascAlu, NomeLocalNascAlu)
```

### 2FN (Segunda Forma Normal)

Uma relação está na 2FN quando está na 1FN e **nenhum atributo não-chave depende parcialmente da chave primária**.

**Resultado:** a tabela **não obedece à 2FN**, pois há dependências parciais:

- `NomeDisc` depende apenas de `CodDisc`;
- `NomeAlu`, `CodLocalNascAlu` e `NomeLocalNascAlu` dependem apenas de `CodAlu`.

**Transformação:** separar os atributos que dependem de parte da chave em novas relações.

```
Matricula (CodAlu, CodDisc)
    CodAlu referencia Aluno
    CodDisc referencia Disciplina

Disciplina (CodDisc, NomeDisc)

Aluno (CodAlu, NomeAlu, CodLocalNascAlu, NomeLocalNascAlu)
```

### 3FN (Terceira Forma Normal)

Uma relação está na 3FN quando está na 2FN e **não há dependências transitivas** de atributos não-chave em relação à chave primária.

**Resultado:** a relação `Aluno` **não obedece à 3FN**, pois existe a dependência transitiva:

`CodAlu → CodLocalNascAlu → NomeLocalNascAlu`

**Transformação:** extrair a localidade para uma nova relação.

```
Matricula (CodAlu, CodDisc)
    CodAlu referencia Aluno
    CodDisc referencia Disciplina

Disciplina (CodDisc, NomeDisc)

Aluno (CodAlu, NomeAlu, CodLocalNascAlu)
    CodLocalNascAlu referencia Localidade

Localidade (CodLocalNascAlu, NomeLocalNascAlu)
```

As relações `Matricula`, `Disciplina`, `Aluno` e `Localidade` estão na **3FN**.