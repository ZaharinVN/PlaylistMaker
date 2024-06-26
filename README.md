# Playlist Maker
Приложение для поиска музыкальных треков на базе API ITunes, прослушивания их 30-секундных демо, добавления треков в избранное, и формирования плейлистов из найденных треков.

# Описание функционала
В качестве архитектурнего паттерна используется MVVM.   
Приложение построено на Single Activity с фрагментами.
Навигация по приложению осуществляется при помощи меню, расположенного внизу экрана, реализована с помощью Navigation Component.  
Пункты меню, при нажатии на которые осуществляется переход на соответствующий экран:  
- Поиск
- Медиатека
- Настройки

**Поиск**  
На этом экране можно произвести поиск композиции по ее наименованию или имени исполнителя.   
Здесь также расположен список истории поиска.     
При нажатии на трек из списка осуществляется переход на экран плеера. 

**Экран плеера**  
На экране плеера пользователю доступна общая информация о композиции и исполнителе, обложка а также панель управления.   
При помощи кнопок на панели пользователь может запустить воспроизведение композици, остноваить ее, добавить трек в избранное или в плейлисты.  

**Медиатека**  
На этом экране расположены две вкладки: Избранное и Плейлисты.  
На первой вкладке расположены треки, ранее помеченные, как избранные.   
На второй вкладе расположены плейлисты, а также кнопка, при нажатии на которую можно создать новый список с треками.  

**Экран плейлиста**  
На экране плейлиста пользователь может выбрать обложку для списка композиций, написать название и краткое описание.   
Плейлист также доступен для редактирования.

**Настройки**  
На экране настроек можно изменить цветовую схему, поделиться приложением, а также написать разработчикам.

# Стек применяемых технологий
- Kotlin
- Android SDK
- MVVM
- ViewBinding
- SharedPreferences
- GitFlow
- LiveData
- Jetpack Navigation Component
- Bottom navigation
- Fragments
- Koin
- Coroutines
- Flow
- Room,
- Retrofit2
- Glide

# Системные требования
- Минимальная версия SDK для Андроид **24**
 
## Установка
[![APK Download](https://img.shields.io/badge/APK-Download-brightgreen?logo=android)](https://github.com/ZaharinVN/PlaylistMaker/blob/master/PlaylistMaker.v.1.apk)

## Запуск проекта в Android Studio

```bash
https://github.com/ZaharinVN/PlaylistMaker.git
```
## Скриншоты
<p float="left">
    <img src="https://github.com/ZaharinVN/PlaylistMaker/blob/dev/Screenshot_1.png" width="250"> 
    <img src="https://github.com/ZaharinVN/PlaylistMaker/blob/dev/Screenshot_2.png" width="250"> 
    <img src="https://github.com/ZaharinVN/PlaylistMaker/blob/dev/Screenshot_3.png" width="250"> 
    <img src="https://github.com/ZaharinVN/PlaylistMaker/blob/dev/Screenshot_4.png" width="250"> 
    <img src="https://github.com/ZaharinVN/PlaylistMaker/blob/dev/Screenshot_5.png" width="250"> 
    <img src="https://github.com/ZaharinVN/PlaylistMaker/blob/dev/Screenshot_6.png" width="250"> 
</p> 