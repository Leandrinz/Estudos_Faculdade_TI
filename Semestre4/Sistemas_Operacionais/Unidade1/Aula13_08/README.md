# Sistemas Operacionais — Aula 13/08/2026

## Foco: Software

---

## 1. Software

No contexto de Sistemas Operacionais, podemos dividir o software em duas partes principais:

### Modo Usuário

É onde ficam os programas utilizados diretamente pelo usuário, como editores, navegadores, jogos etc.

- Tem como objetivo fornecer serviços e funcionalidades úteis ao usuário.
- Não possui acesso direto e irrestrito ao hardware.

### Núcleo (Kernel)

É a parte central do Sistema Operacional.

Responsabilidades principais:

- Gerenciar os recursos do computador.
- Intermediar o acesso dos programas ao hardware.
- Controlar a execução dos programas em modo usuário.
- Proteger o hardware.
- Evitar que um programa interfira indevidamente em outro ou no próprio sistema.

> **Ideia principal:** o kernel facilita o trabalho dos programadores porque fornece uma interface para utilizar os recursos do hardware, sem que cada programa precise controlar diretamente os dispositivos.

---

## 2. Kernel e proteção do hardware

Um programa em **modo usuário** normalmente não pode acessar diretamente recursos críticos do computador.

Quando precisa realizar alguma operação privilegiada, ele solicita o serviço ao **kernel**, geralmente por meio de uma **chamada de sistema (System Call)**.

### Exemplo

    Programa (Modo Usuário)
              ↓
          System Call
              ↓
            Kernel
              ↓
           Hardware

Isso proporciona **abstração e proteção**:

- O programa trabalha com uma interface mais simples.
- O kernel controla como os recursos do hardware serão utilizados.
- O hardware fica protegido contra acessos indevidos.

---

## 3. Dispositivo, Controladora e Registradores

Uma visão simplificada da comunicação com um dispositivo é:

    Programa
       ↓
    Kernel
       ↓
    Controladora
       ↓
    Registradores
       ↓
    Dispositivo

### Controladora

A **controladora de dispositivo** é o componente responsável por controlar e gerenciar a comunicação entre o computador e determinado dispositivo.

Ela possui **registradores**, que podem ser utilizados para:

- Enviar comandos.
- Informar configurações.
- Consultar o estado do dispositivo.
- Fornecer ou receber dados.

### Registradores

Os **registradores da controladora** são pequenas áreas de armazenamento utilizadas para controlar e acompanhar o funcionamento do dispositivo.

É importante saber **qual é a função de cada registrador** da controladora estudada.

> Os números `[4, 3, 2, 1]` anotados em aula provavelmente representam registradores específicos apresentados pelo professor. É necessário associar cada número à sua respectiva função conforme o exemplo apresentado em sala.

### Cuidado com os registradores

Não se deve escrever qualquer valor em qualquer registrador.

Um registrador pode representar, por exemplo, um **comando de controle**. Escrever um valor incorreto pode:

- Fazer o dispositivo executar uma operação inesperada.
- Alterar sua configuração.
- Causar falhas de funcionamento.
- Em determinados dispositivos, até provocar danos físicos.

Por isso, o acesso ao hardware é controlado pelo **Sistema Operacional** e pela própria interface da **controladora**.

---

## 4. Conceitos para guardar

- **Modo Usuário:** ambiente onde os programas comuns são executados.
- **Kernel:** núcleo do Sistema Operacional, responsável por gerenciar recursos e proteger o sistema.
- **System Call:** mecanismo utilizado pelos programas para solicitar serviços ao kernel.
- **Controladora:** componente responsável por controlar a comunicação com um dispositivo.
- **Registradores:** locais utilizados pela controladora para comandos, estados, configurações e/ou dados.
- **Abstração:** permite que o programa utilize uma interface mais simples, sem precisar conhecer todos os detalhes do hardware.

---

## 5. Resumo do fluxo

    Aplicação
       ↓
    System Call
       ↓
    Kernel
       ↓
    Controladora
       ↓
    Registradores
       ↓
    Dispositivo