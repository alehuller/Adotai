#  🐾 Adotaí! 
![Last Commit](https://img.shields.io/github/last-commit/alehuller/Adotai?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-orange?style=for-the-badge)
![Stars](https://img.shields.io/github/stars/alehuller/Adotai?style=for-the-badge)
![Watchers](https://img.shields.io/github/watchers/alehuller/Adotai?style=for-the-badge)
![Coffee](https://img.shields.io/badge/Powered_by-Coffee-ff69b4?style=for-the-badge&logo=buy-me-a-coffee)

![img_2.png](img_2.png)<br>

## 📑 Tópicos

- [📌 Sobre](#-sobre)
- [🧭 Organização do Projeto](#-organização-do-projeto)
- [🛠️ Tecnologias](#️-tecnologias)
  - [🔧 Backend](#-backend)
  - [🎨 Frontend](#-frontend)
- [🗂️ Documentação e Versionamento](#️-documentação-e-versionamento)
- [🧰 Pré-requisitos](#-pré-requisitos)
- [🚀 Como rodar o projeto](#-como-rodar-o-projeto)
  - [📦 1. Clone o repositório](#-1-clone-o-repositório)
  - [🔙 2. Backend (Java + Spring Boot)](#-2-backend-java--spring-boot)
  - [📘 3. Acesse a documentação da API (Swagger)](#-3-acesse-a-documentação-da-api-swagger)
  - [🌐 Frontend (em desenvolvimento)](#-frontend-em-desenvolvimento)
- [👥 Contribuidores](#-contribuidores)
- [📝 Licença](#-licença)

## 📌 Sobre

Este projeto é uma **plataforma web** voltada para a **adoção responsável de animais**, desenvolvida com foco em facilitar a conexão entre **ONGs, protetores independentes e adotantes**.

Através da aplicação, ONGs e abrigos podem **cadastrar animais disponíveis para adoção**, enquanto usuários interessados podem **filtrar por localização, tipo, raça, porte e outros critérios** para encontrar um pet compatível com seu perfil.

A plataforma foi pensada para **resolver um problema real** enfrentado por diversas instituições e pessoas envolvidas com a causa animal: a **falta de centralização, visibilidade e organização no processo de adoção**.

Além do básico, o sistema contará com **funcionalidades extras**, como:
- Upload de fotos dos animais
- Status detalhado dos pets (disponível, em adoção, adotado, lar temporário)
- Notificações por e-mail
- Área pública para visualização dos animais
- Dashboard para ONGs com estatísticas
- Histórico de adoções
- Sistema de denúncias e avaliações

O objetivo é **ampliar a visibilidade dos animais abrigados**, **agilizar o processo de adoção** e **oferecer ferramentas úteis para ONGs e lares temporários**, tudo isso promovendo o bem-estar animal.

## 🧭 Organização do Projeto

Acesse nosso workspace no Notion para visualizar o planejamento, tarefas, protótipos e evolução do projeto:

🔗 [Clique aqui para acessar o Notion do Adotaí!](https://www.notion.so/Adota-Sistema-de-Ado-o-de-Animais-Integrado-com-ONGs-1e5b6aa4de158011b031c1bb629f3878?source=copy_link)


## 🛠️ Tecnologias

### 🔧 Backend

API RESTful em desenvolvimento com **Java + Spring Boot**, responsável pela lógica de negócio, persistência de dados, autenticação e segurança com JWT. Versionamento do Banco de Dados em PostgreSQL atráves do Flyway. Ainda serão aplicados teste com JUnit5.

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JUnit5 Badge](https://img.shields.io/badge/Tests-JUnit5-green?style=for-the-badge&logo=junit5)


### 🎨 Frontend

Desenvolvimento com React e estilizado com CSS3, o frontend consome a API RESTful do backend por meio de requisições HTTP, garantindo uma interface acessível, responsiva e funcional para ONGs e adotantes.

![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)

## 🗂️ Documentação e Versionamento

Documentação do Backend e suas Requisições por meio do Swagger.<br>
Prototipação das telas utilizando o Figma.<br>
Gerenciamento de tarefas através do Trello.<br>
Organização do Projeto Adotaí! realizada no Notion.<br>
Versionamento de Código via Git.

![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
![Trello](https://img.shields.io/badge/Trello-%23026AA7.svg?style=for-the-badge&logo=Trello&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)

## 🧰 Pré-requisitos

Antes de rodar o projeto, certifique-se de que as seguintes ferramentas estão instaladas na sua máquina:

- [Java 21.0.5](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven](https://maven.apache.org/download.cgi) (versão compatível com o Java 21)
- [PostgreSQL 17.4](https://www.postgresql.org/download/)
- [Postman](https://www.postman.com/downloads/) (opcional, caso queira testar a API)

<!--
### 🌐 Frontend

- [Node.js](https://nodejs.org/) (recomenda-se versão LTS)
- [npm](https://www.npmjs.com/) (ou [yarn](https://yarnpkg.com/) como gerenciador de pacotes)
-->
## 🚀 Como rodar o projeto

Siga os passos abaixo para rodar o projeto localmente em sua máquina.

### 📦 1. Clone o repositório

```bash
git clone https://github.com/alehuller/Adotai.git
cd Adotai
```

### 🔙 2. Backend (Java + Spring Boot)

⬇️ Compile o projeto:

```bash
./mvnw clean install
```

⚙️ Configure o banco de dados PostgreSQL

Certifique-se de que você tem um banco PostgreSQL rodando com as credenciais corretas. No arquivo application.yml, configure:

```bash
url: jdbc:postgresql://localhost:5432/seu_banco
username: seu_usuario
password: sua_senha
```
> 💡 O Flyway aplicará as migrações automaticamente na inicialização. <br>
> 💾 Recomenda-se colocar o nome do banco como `ongadocoes`. <br>
> 🧑‍💻 O PostgreSQL por padrão, coloca o nome de usuário como `postgres`. Se não modificado, coloque em username `postgres`.

▶️ Inicie a API

```bash
./mvnw spring-boot:run
```
A API estará disponível em: http://localhost:8080

### 📘 3. Acesse a documentação da API (Swagger)

Após iniciar o backend, você pode acessar a documentação interativa da API através do Swagger:

🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
ou
🔗 [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

> Lá você poderá visualizar todos os endpoints e métodos HTTP.

📫 É recomendado testar a API utilizando ferramentas como o [Postman](https://www.postman.com/), enviando requisições HTTP diretamente para os endpoints.

🔗 [Acesse a collection pública da API do Adotaí! no Postman](https://victor-5545008.postman.co/workspace/Victor's-Workspace~547f2bc0-b948-4fb6-88fe-f4905bcca801/collection/44764863-fc5303b4-02a1-47f8-81e2-1041139034c1?action=share&creator=45034750&active-environment=45034750-e0faefc3-6481-4a36-ac70-cf12ecfe13b9)


### 🌐 Frontend (em desenvolvimento)

O frontend será desenvolvido com **React** e ficará disponível nesta mesma aplicação.

> 🔧 Em breve adicionaremos instruções para rodar o frontend localmente.


## 👥 Contribuidores
<table>
  <tr>
    <td align="center">
        <img src="https://avatars3.githubusercontent.com/u/92354266" width="100px;" alt="Foto do Alejandro Hüller no GitHub"/><br>
        <sub>
          <span style="font-size:16px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;"><b>Alejandro Hüller</b></span>
        </sub><br>
      <a href="https://github.com/alehuller" title="GitHub do Alejandro Hüller">
        <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="100px;" alt="Link do Alejandro Hüller no GitHub"/><br>
      </a><a href="https://www.linkedin.com/in/alejandro-huller-44171225a/" title="LinkedIn do Alejandro Hüller">
        <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="100px;" alt="Link do Alejandro Hüller no LinkedIn"/>
      </a>
    </td>
    <td align="center">
        <img src="https://avatars3.githubusercontent.com/u/9754413" width="100px;" alt="Foto do Victor Hugo no GitHub"/><br>
        <sub>
          <span style="font-size:16px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;"><b>Victor Hugo</b></span>
        </sub><br>
      <a href="https://github.com/vhugoemcruz" title="GitHub do Victor Hugo">
        <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="100px;" alt="Link do Victor Hugo no GitHub"/><br>
      </a><a href="https://www.linkedin.com/in/victor-hugo-cruz-93180a264/" title="LinkedIn do Victor Hugo">
        <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="100px;" alt="Link do Victor Hugo no LinkedIn"/>
      </a>
    </td>
    <td align="center">
        <img src="https://avatars3.githubusercontent.com/u/105395280" width="100px;" alt="Foto do Victor Mesquita no GitHub"/><br>
        <sub>
          <span style="font-size:16px; font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;"><b>Victor Mesquita</b></span>
        </sub><br>
      <a href="https://github.com/victormesquitta" title="GitHub do Victor Mesquita">
        <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="100px;" alt="Link do Victor Mesquita no GitHub"/><br>
      </a><a href="https://github.com/alehuller" title="LinkedIn do Victor Mesquita">
        <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="100px;" alt="Link do Victor Mesquita no LinkedIn"/>
      </a>
    </td>
  </tr>
</table>

## 📝 Licença

Este projeto está licenciado sob os termos da [MIT License](./LICENSE).

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
