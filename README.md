# Sistema de Apuração Eleitoral de Vereadores

Este projeto implementa o processamento e geração de relatórios de resultados da eleição de vereadores de um município brasileiro, conforme especificações do Trabalho de POO 2025/1 na UFES.

## Estrutura do Projeto

```
.
├── build.xml
├── dist/
│   └── vereadores.jar
├── bin/
├── src/
│   └─ br/ufes/Gabriel/eleicao/
│       ├─ model/
│       │   ├ Candidato.java      # Classe que representa um candidato
│       │   ├ Partido.java        # Classe que representa um partido
│       │   └ VotoSecao.java      # (Opcional) Modela votos por seção, se houver
│       ├─ util/
│       │   └ Leitor.java         # Lê e parseia os CSVs de candidatos e votos
│       ├─ service/
│       │   ├ Eleicao.java        # Armazena candidatos, partidos e configurações
│       │   └ Relatorio.java      # Gera os 10 relatórios conforme especificação
│       └─ App.java               # Main: valida args, invoca Leitor e Relatorio
├── consulta_cand_2024_AC.csv     # Dados de candidatos (exemplo)
├── votacao_secao_2024_AC.csv     # Dados de votação (exemplo)
└── README.md                      # (Este arquivo)
```

## Requisitos

- Java 17
- Apache Ant
- Encoding UTF-8 nos arquivos `.java`
- CSVs em `ISO-8859-1`, separados por `;`

## Compilação e Empacotamento

1. **Compilar**:
   ```bash
   ant compile
   ```
2. **Gerar JAR executável**:
   ```bash
   ant jar
   ```
3. O artefato final `vereadores.jar` ficará em `dist/`.

## Execução

```bash
java -jar dist/vereadores.jar <codigo_municipio> <arquivo_candidatos> <arquivo_votacao> <data_eleicao>
```

- **codigo_municipio**: código da UE (ex.: `01554`)
- **arquivo_candidatos**: CSV de candidatos (ex.: `consulta_cand_2024_AC.csv`)
- **arquivo_votacao**: CSV de votação (ex.: `votacao_secao_2024_AC.csv`)
- **data_eleicao**: `dd/MM/yyyy` (ex.: `06/10/2024`)

Exemplo:
```bash
java -jar dist/vereadores.jar 01554 consulta_cand_2024_AC.csv votacao_secao_2024_AC.csv 06/10/2024
```

4. De maneira mais simples e casual:
   ```bash
   javac -d bin $(find src -name "*.java")
   ```
   Exemplo:
   ```bash
   java -cp bin br.ufes.Gabriel.eleicao.App 57053 consulta_cand_2024_ES.csv votacao_secao_2024_ES.csv 06/10/2024

   ```

## Relatórios Gerados

O programa imprime **10 relatórios** na saída padrão:

1. **Número de vagas** (total de eleitos).  
2. **Vereadores eleitos**: lista de eleitos pelo sistema proporcional, ordenados por votos nominais e idade, com `*` para federações.  
3. **Top-N mais votados (nominal)**: os N mais votados, onde N é o número de vagas.  
4. **Prejudicados**: candidatos no Top-N que não foram eleitos.  
5. **Beneficiados**: eleitos que não estavam no Top-N.  
6. **Votação dos partidos**: total de votos (nominais+legenda) e número de eleitos por partido.  
7. **Primeiro e último por partido**: candidato mais e menos votado de cada partido.  
8. **Faixa etária dos eleitos**: distribuição em `<30`, `30–39`, `40–49`, `50–59`, `>=60`.  
9. **Gênero dos eleitos**: contagem e percentual de homens e mulheres.  
10. **Total de votos**: válidos, nominais e de legenda.

## Observações

- A ordenação de candidatos e partidos segue as regras de desempate especificadas:  
  - Empate em votos nominais → candidato mais velho primeiro.  
  - Empate em votos de partido → menor número de partido primeiro.  
- Tratamento robusto de I/O e parsing de CSVs com `ISO-8859-1`.

---

Desenvolvido por Gabriel Saymon para a disciplina de Programação Orientada a Objetos, UFES 2025/1.
