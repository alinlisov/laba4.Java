# Лабораторна робота №4: Hard task

Модуль динамічного завантаження та гарячого перезавантаження класів (Hot-reloading ClassLoader) із забезпеченням суворої типобезпеки (Type Safety) за допомогою **Java Generics** та **Wildcards (`? extends`, `? super`)**.

Проєкт є еволюцією Лабораторної роботи №1 і трансформує базовий механізм завантаження класів у гнучкий, типізований фреймворк для розширюваних модулів.

---

##  Основні можливості

- **Hot-Reloading:** Відстеження змін у файлі `.java` під час виконання програми та автоматична перекомпіляція через `JavaCompiler` API.
- **Strict Type Safety:** Використання узагальненого інтерфейсу `ExecutableModule<R>` замість Raw Types та приведення типів `(Object)`.
- **Generic ClassLoader:** Кастомний `ModuleLoader<T>`, який завантажує та перевіряє сумісність класів на рівні метаданих (`Class<? extends T>`).
- **PECS Principle:** Застосування Wildcards для гнучкої роботи з колекціями-продюсерами (`? extends`) та контейнерами-споживачами (`? super`).

---

##  Технології

- **Java 11+** (JDK)
- **Java Compiler API** (`javax.tools.JavaCompiler`)
- **Java Reflection API & Custom ClassLoader**

---

##  Архітектура та застосування Generics / Wildcards

### 1. Узагальнений контракт модуля (`ExecutableModule<R>`)
Задає тип результату `R`, який повертає модуль при виконанні методу `execute()`:
```java
public interface ExecutableModule<R> {
    R execute();
    String getVersion();
}
