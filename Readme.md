
## Linhas editas/implementadas no projeto da branch feature/eduardo


- **Caminho** : main/java/com/eduardoomarson/quizpdm/data/remote/firestore/FirestoreRepository.kt
  + Pacote : package com.eduardoomarson.quizpdm.data.remote.firestore
  + Linha : 105 

- **Caminho**  : main/java/com/eduardoomarson/quizpdm/navigation/QuizAppNavHost.kt
  + Pacote : package com.eduardoomarson.quizpdm.navigation
  + Linha : 160 ate 167 | 179 ate 181

- **Caminho**  :  main/java/com/eduardoomarson/quizpdm/ui/feature/board
+ Pacote : package com.eduardoomarson.quizpdm.ui.feature.board
+ Arquivo criado/editado por inteiro :
   +  main/java/com/eduardoomarson/quizpdm/ui/feature/board/components/LeaderRow.kt
   +  main/java/com/eduardoomarson/quizpdm/ui/feature/board/components/OnBackRow.kt
   +  main/java/com/eduardoomarson/quizpdm/ui/feature/board/components/TopUserBox.kt
   +  main/java/com/eduardoomarson/quizpdm/ui/feature/board/BoardScreen.kt
   +  main/java/com/eduardoomarson/quizpdm/ui/feature/board/BoardViewModel.kt

 # Preview #

   https://github.com/user-attachments/assets/3fd9e9b0-6a8f-421a-879f-1750cc9a1274

# 📱 QuizPDM

Aplicativo Android de quiz multiplataforma desenvolvido com **Jetpack Compose**, **Firebase** e arquitetura **MVVM**. Permite jogar quizzes por categoria, criar seus próprios quizzes, acompanhar o histórico de partidas e competir no placar global.

---

## 🚀 Funcionalidades

- **Autenticação** — login e cadastro por e-mail/senha e Google (Credential Manager)
- **Recuperação de senha** — envio de e-mail de redefinição via Firebase Auth
- **Home** — visão geral do usuário com pontuação, avatar e categorias disponíveis
- **Quiz** — modo aleatório ou por categoria/quiz específico, com contagem de tempo
- **Criar Quiz** — criação de quizzes personalizados salvos no Firestore
- **Histórico** — registro local de todas as partidas jogadas, com acertos, pontuação e tempo
- **Placar (Board)** — ranking global dos usuários
- **Perfil** — configuração de nome e avatar do usuário

---

## 🛠️ Tecnologias e Bibliotecas

| Camada | Tecnologia |
|--------|-----------|
| UI | Jetpack Compose + Material 3 |
| Navegação | Navigation Compose (type-safe routes) |
| Injeção de dependência | Hilt |
| Banco de dados local | Room |
| Backend / Autenticação | Firebase Auth + Firestore |
| Autenticação Google | Credential Manager |
| Concorrência | Kotlin Coroutines + Flow |
| Arquitetura | MVVM |

---

## 🏗️ Arquitetura

O projeto segue o padrão **MVVM** com separação clara de responsabilidades:

```
app/
├── authentication/         # AuthViewModel e AuthState
├── data/
│   ├── local/
│   │   ├── dao/            # DAOs do Room (QuizDao, UserDao, HistoryDao...)
│   │   ├── database/       # QuizDatabase (Room)
│   │   └── entities/       # Entidades Room (QuizEntity, HistoryEntity...)
│   ├── remote/
│   │   └── firestore/      # FirestoreRepository
│   └── repository/         # QuizRepository
├── di/                     # Módulos Hilt
├── navigation/             # QuizAppNavHost (rotas tipadas)
└── ui/
    └── feature/
        ├── home/           # HomeScreen + HomeViewModel
        ├── quiz/           # QuizScreen + QuizViewModel
        ├── history/        # HistoryScreen + HistoryViewModel
        ├── board/          # BoardScreen
        ├── profile/        # ProfileSetupScreen
        ├── createquiz/     # CreateQuizScreen
        ├── quizlist/       # QuizListScreen
        ├── questions/      # components
        ├── score/          # ScoreScreen
        └── authenticationScreens/
```

---

## 🗄️ Banco de Dados Local (Room)

O banco `quiz_pdm` (versão 2) contém as seguintes tabelas:

| Tabela | Descrição |
|--------|-----------|
| `quizzes` | Quizzes disponíveis |
| `question` | Perguntas de cada quiz |
| `user` | Dados do usuário logado |
| `user_quiz_progress` | Progresso do usuário por quiz |
| `quiz_history` | Histórico de partidas jogadas |

---

## ☁️ Sincronização com Firebase

- Ao fazer login, os dados do usuário são sincronizados do Firestore para o Room (**offline-first**)
- Quizzes são observados em tempo real via listener do Firestore e persistidos localmente
- Pontuação do usuário é atualizada no Room e refletida na HomeScreen via `Flow`

---

## ▶️ Como rodar o projeto

### Pré-requisitos

- Android Studio Hedgehog ou superior
- JDK 17+
- Conta Firebase com projeto configurado

### Configuração

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/PDM_Trabalho4.git
   ```

2. Adicione o arquivo `google-services.json` do seu projeto Firebase em `app/`.

3. No Firebase Console, habilite:
   - **Authentication** → E-mail/Senha e Google
   - **Firestore Database**

4. Abra no Android Studio e rode o projeto em um emulador ou dispositivo físico.

---

## 👥 Contribuidores

| Nome | Contribuições |
|------|--------------|
| Eduardo Marson | Arquitetura base, autenticação, quiz, sincronização Firebase |
| Samuel  Souto| BoardScreen, navegação |
| José Vitor Marson| HistoryScreen, navegação |
| Matheus | Auxilio no Firestore, Arquitetura |

---

## 📄 Licença

Projeto acadêmico — desenvolvido para a disciplina de Programação para Dispositivos Móveis (PDM).
