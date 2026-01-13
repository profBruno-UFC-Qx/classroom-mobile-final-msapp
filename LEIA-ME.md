## Descrição da Entrega 01

Desenvolvimento de cerca de 60 a 80 por cento do app, implementação de telas de acesso, CRUID para os gastos (falta polir) e para usuário (falta exclusão e polir);
App não está responsivo/adaptativo ainda;
Próximas etapas a polir e melhorar: MainSreen -> aparencia dos cards; referencias da moeda; [ X ]
                                    PerfilScreen -> aparencia de CardConf; [ X ]
                                    Integração a dados persistentes; [ x ] -- Parcialmente implementado
                                    Material Theme na tela de lançamento. [ X ]
                                    Comentários nas funções -> "Documentação" [ X ]
                                    Aperfeiçoar sistema de registro e login [ ]
                                    Implementação de enum class para tipo de gasto [ X ]
                                    Correção de ViewModels entre telas [ X ]
---
OBS: Projeto não está finalizado. tive problemas com a IDE e a parte de integração a persistência; Não sei por que, mas entre uma execução e outra a DataUser e suas funções entrou em conflito, como se houvesse 2 no projeto.
    Faltam poucas coisas para implementar com persistência e alguns extras na parte do gráfico (seções gráficas de cada tipo de gasto).
## Funcionalidades Principais -- Entrega 01

- [ x ] Funcionalidade 1: [Cálculo orçamento - gastos de modo geral; o usuário informa seu orçamento e a cada gasto adicionado o tópico "gastos" incrementa]
- [ x ] Funcionalidade 2: [Disposição gráfica dos gastos em lista] -- 50% a 70% polidas
- [ x ] Funcionalidade 3: [Consumo de API para conversão de moedas]
- [ x ] Funcionalidade 4: [Telas de acesso] -- parte da integração CRUID
- [ x ] Funcionalidade 5: [Integração com a galeria]
- [ x ] Funcionalidade 6: [Integração de tema dinâmico usando material theme]
- [ x ] Funcionalidade 7: [ Balanço entre ganhos <-> gastos ]
- [ x ] Funcionalidade 8: [ Implementação de gráfico para balanço entre ganhos e gastos]
---

> [!WARNING]
> Daqui em diante o README.md só deve ser preenchido no momento da entrega final.

##  Tecnologias: 
 - Arquitetura MVVM;
 - Manipulação de dados - CRUID;
 - Consumo de API - lib. Retrofit;
 - Animações Android - libs. padrões;
 - Manipulação de temas - Material Theme;

 ps: Acredito ser isso o pedido, senão, nas próximas atualizações será consertado;
 Ao final colocarei as referencias do material usado.
---

## Fontes e materiais de apoio
-- https://youtube.com/playlist?list=PLQkwcJG4YTCSpJ2NLhDTHhi6XBNfk9WiC&si=NZHTHPI3Gbu075oK
-- https://youtube.com/playlist?list=PLSrm9z4zp4mEWwyiuYgVMWcDFdsebhM-r&si=_Nf9C6NVJLy2WDNc
-- https://youtu.be/WKz8SpPxZKk?si=pld288ydDlyisiNy
-- https://youtu.be/J-d96V9n8Po?si=X3VwqDepOxCM9mzu
-- https://youtu.be/4gUeyNkGE3g?si=3uK2D0CTbOVubiDj
-- https://m3.material.io/develop/android/jetpack-compos
--  


## Instruções para Execução
[ Execução padrão ]
**Somente caso haja alguma coisa diferente do usual**

```bash
# Clone o repositório
git clone https://github.com/profBruno-UFC-Qx/classroom-mobile-final-msapp.git

# Navegue para o diretório
cd [nome-do-repositorio]

# Siga as instruções específicas para a sua tecnologia...