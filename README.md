# Projeto de Demonstração

### Para executar projeto

Clone o repositório com `git`:

    git clone https://github.com/felipe-louzas/dex-server.git

Compile e execute o projeto com o `maven`:

    cd dex-server
    mvn compile
    mvn test         # para testes de unidade e integração
    mvn exec:java

Abra o navegador e vá até http://localhost:8080/. Você deve ver o mockup da inteface de cliente (ainda WIP!) e poderá acessar os endpoints REST.
  
    http://localhost:8080/svc/cadastros/ingredientes
    http://localhost:8080/svc/cadastros/lanches
    http://localhost:8080/svc/venda/nova
    http://localhost:8080/svc/venda/{id}
    http://localhost:8080/svc/venda/{id}/lanches
    http://localhost:8080/svc/venda/{id}/lanches/novo
    http://localhost:8080/svc/venda/{id}/lanches/novo/{tipo}
    http://localhost:8080/svc/venda/{id}/lanches/{numero}
    http://localhost:8080/svc/venda/{id}/lanches/{numero}/add/{ingrediente}
    http://localhost:8080/svc/venda/{id}/lanches/{numero}/remove/{ingrediente}

