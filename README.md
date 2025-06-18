# Sistema_Eleitoral

## Estrutura do Projeto

```
.
├── bin/                     
│   └── vereadores.jar       ← artefato compilado (output do build)
├── src/                     
│   ├── App.java
│   ├── Candidato.java
│   ├── Eleicao.java
│   ├── Impressora.java  
│   ├── Leitor.java                  
│   ├── Partido.java             
│   └── Relatorio.java   
├── build.xml                ← script Ant para compile/jar
└── README.md                ← instruções de uso e compile
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
   javac -d bin src/App.java src/*.java
   ```
   Exemplo:
   ```bash
   java -cp bin App 57053 consulta_cand_2024_AC.csv votacao_secao_2024_AC.csv 06/10/2024
   ```
