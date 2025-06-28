# Sistema_Eleitoral

## Estrutura do Projeto

```
.
src/
└─ br/ufes/Gabriel/eleicao/
   ├─ model/
   │   ├ Candidato.java
   │   ├ Partido.java
   │   └ VotoSecao.java
   ├─ util/
   │   └ Leitor.java
   ├─ service/
   │   ├ Eleicao.java
   │   └ Relatorio.java
   └─ App.java
```

## Como compilar e executar

1. Compile o projeto:
   ```bash
   ant compile
   ```
2. Gere o JAR:
   ```bash
   ant jar
   ```
3. Execute com os parâmetros:
   ```bash
   java -jar bin/vereadores.jar <codigo_municipio> <arquivo_candidatos> <arquivo_votacao> <data_eleicao>
   ```
   Exemplo:
   ```bash
   java -jar bin/vereadores.jar 57053 consulta_cand_2024_AC.csv votacao_secao_2024_AC.csv 06/10/2024
   ```
4. De maneira mais simples e casual:
   ```bash
   javac -d bin $(find src -name "*.java")
   ```
   Exemplo:
   ```bash
   java -cp bin br.ufes.Gabriel.eleicao.App 01554 consulta_cand_2024_AC.csv votacao_secao_2024_AC.csv 06/10/2024

   ```
