# Projeto de Demonstração

### Para executar projeto

Clone o repositório com `git`:

    git clone https://github.com/felipe-louzas/dex-server.git

Compile e execute o projeto com o `maven`:

    cd dex-server
    mvn compile
    mvn test         # para testes de unidade e integração
    mvn exec:java

Abra o navegador e vá até http://localhost:8080/ para acessar o front-end. O front-end permite que você realize vendas dos lanches, customizando seus ingredientes. O sistema irá adicionar as promoções automaticamente aos lanches lançados. Você pode também customizar seu lanche, escolhendo os ingredientes desejados.

### Para executar o frontend em modo de desenvolvimento
  
Você pode rodar o frontend em modo de desenvolvimento, permitindo análise e alterações no código. Quando em modo de desenvolvimento, o frontend ira conectar no http://localhost:8080/svc/ para se comunicar com o servidor. Garanta que o backend esteja sendo executado nessa porta, ou então faça a alteração em `src/views/RootView.js`

Será necessario instalar o Node.Js e utilizar o gerenciador de pacotes `npm`:

    cd src
    cd frontend
    npm install
    npm start      # inicia frontend em modo de desenvolvimento
    npm build		# gera pacote de produção
  