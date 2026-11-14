# FinanceApp

FinanceApp to rozwijany system zarządzania finansami osobistymi. Projekt ma zapewnić podstawę do rejestrowania użytkowników, kategorii oraz transakcji finansowych, a w przyszłości także do analizowania sytuacji finansowej.

## Technologie

- Java 25
- Spring Boot 3.5.7
- Maven
- Spring Web
- Spring Security
- JWT (JJWT)
- Spring Data JPA i Hibernate
- H2 — obecnie skonfigurowana baza danych działająca w pamięci

## Obecnie zaimplementowane

- Rejestracja użytkownika przez `POST /auth/register`. Dane wejściowe są walidowane, hasło zapisywane jako skrót BCrypt, a rola nowego konta ustawiana na `USER`. Zajęty username lub email powoduje odpowiedź `409 Conflict`; poprawna rejestracja zwraca `201 Created`.
- Podstawowe endpointy użytkowników: tworzenie oraz pobieranie listy i użytkownika po ID pod `/api/users`.
- Podstawowe endpointy kategorii: tworzenie oraz pobieranie listy i kategorii po ID pod `/api/categories`. Typ kategorii może mieć wartość `INCOME` albo `EXPENSE`.
- Podstawowe endpointy transakcji: tworzenie oraz pobieranie listy i transakcji po ID pod `/api/transactions`.
- Konfiguracja Spring Security w trybie bezstanowym oraz podstawowe komponenty JWT: generowanie tokenu, jego odczyt i filtr uwierzytelniający.

Komponenty JWT są częścią projektu, ale endpoint logowania wystawiający token nie jest jeszcze zaimplementowany. Obecne endpointy kategorii i transakcji zapewniają podstawowe tworzenie i odczyt, a nie pełny zestaw funkcji zarządzania finansami.

## Planowane funkcjonalności

- Logowanie użytkownika przez `AuthenticationManager` i wydawanie JWT.
- Pełniejsze zarządzanie przychodami i wydatkami, w tym filtrowanie i podsumowania.
- Rozszerzone zarządzanie kategoriami.
- Dashboard z podsumowaniem finansów i wizualizacjami.
- PostgreSQL jako docelowa, trwała baza danych zamiast obecnej bazy H2 w pamięci.
- Przyszłe wykorzystanie AI/ML do analiz lub wniosków dotyczących finansów.

## Status projektu

Projekt jest w trakcie rozwoju. Wymienione wyżej funkcjonalności planowane nie są jeszcze dostępne jako działające elementy aplikacji.
