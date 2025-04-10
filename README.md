# WeatherApp

**WeatherApp** — это веб-приложение для отображения погодных условий с поддержкой пользователей и локаций. Проект реализован с использованием чистого Spring MVC. Проект выполнен в рамках [роадмапа](https://zhukovsd.github.io/java-backend-learning-course/) Сергея Жукова 

## 🚀 Технологии

- **Java 17**
- **Spring MVC** — веб-фреймворк
- **Spring Test** — для написания интеграционных тестов
- **Thymeleaf** — для отображения фронта
- **PostgreSQL** — база данных
- **Flyway** — миграции базы данных
- **Docker & Docker Compose** — контейнеризация приложения
- **Tomcat** — встраиваемый сервер для WAR-файла
- **nginx** — обратный прокси
- **JUnit 5** — для тестирования
- **Maven** — система сборки

## 📦 Структура проекта

```
weatherAppV2/
├── src/
│   ├── main/
│   │   ├── java/              # Java-код: контроллеры, сервисы, DAO
│   │   ├── resources/
│   │   │   ├── static/        # Статические ресурсы (css, js)
│   │   │   ├── templates/     # JSP-файлы
│   │   │   └── application.properties
│   └── test/                  # Интеграционные тесты
├── db/
│   └── migration/             # Скрипты миграций Flyway
├── docker/
│   ├── nginx/                 # Конфигурация nginx
│   └── tomcat/                # Dockerfile и настройки Tomcat
├── Dockerfile                 # Сборка WAR-архива
├── docker-compose.yml
├── .env                       # Переменные окружения
└── README.md
```

## ⚙️ Сборка и запуск

```bash
# Сборка WAR-файла
mvn clean package

# Запуск через Docker Compose
docker-compose up --build -d
```

## 🛠 Конфигурация через .env

```env
POSTGRES_DB=weatherapp_db
POSTGRES_USER=weatherapp_user
POSTGRES_PASSWORD=weatherapp_pass
APP_PROFILE=prod
```

## 🔍 Тестирование

```bash
mvn test
1 тест падает, так как не могу пробросить в тестовый контекст request из запроса
```

## 📌 Автор

TG: @hesaro_kan

---

> Проект разворачивается в продакшене как WAR-файл через Tomcat, за проксированием nginx и с PostgreSQL в контейнере.
