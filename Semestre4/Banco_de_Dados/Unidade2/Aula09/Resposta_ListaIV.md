# Lista de Exercícios IV — Álgebra Relacional

**Componente:** Banco de Dados

## Esquema

```
empregado   (id_emp, nome, dt_nasc, sexo, salario, id_depto)
                id_depto referencia departamento
departamento (id_dept, nome, id_gerente, dt_ini_gerencia)
                id_gerente referencia empregado
projeto     (id_proj, nome, id_depto)
                id_depto referencia departamento
trabalha_em (id_emp, id_proj, horas)
                id_emp referencia empregado
                id_proj referencia projeto
dependente  (id_dep, id_emp, nome, sexo, dt_nasc, parentesco)
                id_emp referencia empregado
```

**Notação:** `π` projeção, `σ` seleção, `⋈` junção, `ρ` renomeação, `𝒢` agregação (`<atributos de agrupamento> 𝒢 <função>(atributo)`).

---

## a. Nome dos empregados que trabalham no departamento 5

```
π nome (σ id_depto = 5 (empregado))
```

## b. Nome dos empregados que trabalham no departamento de Pesquisa

Renomeando o nome do departamento para evitar ambiguidade:

```
DEPTO_PESQ ← π id_dept (σ nome = 'Pesquisa' (departamento))

π nome (empregado ⋈ id_depto = id_dept DEPTO_PESQ)
```

## c. Média salarial dos empregados de cada departamento

```
id_depto 𝒢 AVG(salario) (empregado)
```

## d. Nome dos gerentes que tenham dependentes

```
G      ← ρ G(id_g, nome_g) (π id_emp, nome (empregado))
GER    ← π id_gerente (departamento)
DEP    ← π id_emp (dependente)

π nome_g ( G ⋈ id_g = id_gerente GER ⋈ id_g = id_emp DEP )
```

## e. Média de horas trabalhadas em cada projeto

```
id_proj 𝒢 AVG(horas) (trabalha_em)
```

## f. Quantidade de empregados que trabalham em cada projeto

```
id_proj 𝒢 COUNT(id_emp) (trabalha_em)
```