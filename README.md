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

### Главный экран
На главном экране отображается список фильмов по умолчанию. 
При прокрутке автоматически прогружаются фильмы.
Если отсутсвует сеть, фильмы подгружаются из памяти устройства.
Выдача фильмов по фильтрам не разработана.
<p align="center">
  <img src="https://github.com/AlievIsa/KinopoiskApi/assets/91617416/dada5368-8c6e-425d-95fe-934811df73c1" alt="Главный экран">
</p>

### Поиск
В поиске хранятся раннее введенные запросы (20 штук). 
Историю поиска можно очистить как полностью, нажав соответствующую кнопку, так и по одному запросу, свайпнув влево.
<p align="center">
  <img src="https://github.com/AlievIsa/KinopoiskApi/assets/91617416/fcf3765a-48e0-4266-af47-31b5d3f320b8" alt="История поиска">
</p>

Также история поиска мэтчится с введеным в данный момент запросом.
Когда вводится запрос, начинают подгружаться подходящие фильмы с задержкой в полсекунды.
Чтобы отобразился весь список фильмов, необходимо совершить поиск.
<p align="center">
  <img src="https://github.com/AlievIsa/KinopoiskApi/assets/91617416/70056332-79cd-4af5-a528-f6f4908cda4a" alt="Страница с фильмами по названию">
</p>

### Фильмы по поиску
Так выглядит страница с фильмами по названию.
<p align="center">
  <img src="https://github.com/AlievIsa/KinopoiskApi/assets/91617416/91c1084a-ae47-4f26-a6f2-d077c80ac2b4" alt="Страница фильма">
</p>

### Экран фильма
Можно перейти на страницу фильма, но к сожалению разработка данной страницы не была завершена.
<p align="center">
  <img src="https://github.com/AlievIsa/KinopoiskApi/assets/91617416/f52b8ba6-1d1e-42c3-8486-73e2b3e68419" alt="Страница фильма">
</p>
