# Sistema_Eleitoral

## Estrutura do Projeto

```
.
├── bin/                     
│   └── vereadores.jar       ← artefato compilado (output do build)
├── lib/                     
│   └── (dependências externas, se houver)
├── src/                     
│   ├── App.java   ← classe com o método main()
│   ├── Leitor.java            ← utilitário de leitura genérica de CSV
│   ├── ConversorData.java        ← utilitário para converter “dd/MM/yyyy” em LocalDate
│   ├── Candidato.java            ← modelo que representa um candidato
│   ├── RegistroVoto.java         ← modelo de um registro de votos por seção
│   ├── Partido.java              ← agrega candidatos e soma votos
│   └── Relatorios.java    ← formata e imprime todos os relatórios
├── build.xml                ← script Ant para compile/jar
└── README.md                ← instruções de uso e compile
```