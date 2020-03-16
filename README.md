# springTestTask

### Задача: 
Spring Boot приложение принимает по rest запрос вида {"request":"12 17 84"} и должно вернуть {"response":"13 18 85"} (все числа больше на 1). Если в строке буква (например {"request":"й12а 17 84и"}), то приложение падает с 502 http-кодом и сообщает, что "в запросе не все числа". Метод в контроллере не должен возвращать ResponseEntity. 

### Описание пакетов и классов:
#### src/main/java/spring
* RootController: вся обработка входного json.

#### src/test/java/spring
Тесты
* RootControllerTest: проверяет работу rest.
* RootControllerTestNoSpring: проверяет входные параметры функций
