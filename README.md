# Kinopoisk API

Фронтенд приложения для быстрого поиска информации по фильмам и сериалам с платформы «Кинопоиска».

## Требования:
- Android SDK версии 24 и выше
- Доступ к сети

## Описание:
Проект написан с использованием чистой архитектуры, MVVM.

Используемый стек технологий:
- Сеть: OkHttp
- Верстка: Compose
- Многопоточность: Coroutines/Flow
- Навигация: Jetpack Navigation
- Пагинация: Paging 3
- Хранение данных: Room
- Внедрение зависимостей: Hilt
- Загрузка изображений: Coil

### Главный экран и экран фильма
На главном экране отображается список фильмов по умолчанию. 
При прокрутке автоматически прогружаются фильмы.
Если отсутсвует сеть, фильмы подгружаются из памяти устройства.
<p align="center">
  <img src="https://github.com/user-attachments/assets/3ff2bd7b-85b2-45cc-b6de-ac11581b380d" alt="Главный экран и экран фильма" width="50%">
</p>

### Поиск
В поиске хранятся раннее введенные запросы (5 штук). 
Историю поиска можно очистить как полностью, нажав соответствующую кнопку, так и по одному запросу, свайпнув влево.
Также история поиска мэтчится с введеным в данный момент запросом.
Когда вводится запрос, начинают подгружаться подходящие фильмы с задержкой в полсекунды.
Чтобы отобразился весь список фильмов, необходимо совершить поиск.
<p align="center">
  <img src="https://github.com/user-attachments/assets/399c1298-c3df-496a-b1a6-3c0b0064e671" alt="Поиска" width="50%">
</p>

### Фильтры
С помощью фильтров можно выбрать тип: фильм или сериал, жанр, год выпуска.
Также с помощью двайного ползунка можно отрегулировать рейтинг.
Фильтры пожно сбросить по кнопке.
<p align="center">
  <img src="https://github.com/user-attachments/assets/d78b1ab9-c734-43db-a028-34efc429784d" alt="Фильтры" width="50%">
</p>

### Фильмы по фильтрам и по поиску 
Так фильмы отображаются по фильтрам и по названию.
<p align="center">
  <img src="https://github.com/user-attachments/assets/4789d1b0-4631-4aa5-b524-7ab805214c2e" alt="Фильмы по фильтрам и по поиску" width="50%">
</p>
