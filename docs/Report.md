# Итоги тестирования.

## Описание.
В данной работе было необходимо протестировать сервис покупки тура в "Марракеш". 
На данном веб-сервисе предоставлена возможность приобретения тура двумя способами: 

   1) Покупка тура по дебитовой карте.
   2) Покупка тура в кредит.
      
По условиям задачи в данном сервисе зарегистрировано всего две карты, которые имеют разный статус.

   1) *4444 4444 4444 4441 - имеет статус "Подтвержденная (Аpproved)".*
   2) *4444 4444 4444 4442 - имеет статус "Отклоненная (Declined)".*

Согластно задания нужно написать код автотестов, которые проверят работоспособность вышеуказанного веб-сервиса, 
а так же проверят данный сайт на наличие багов. Кроме того по условиям задачи необходимо проследить связь веб-сервиса 
с базой данных и установить корректность внесения информации, котороая отображается в БД после совершения покупки тура.

[Task Pages](https://github.com/netology-code/qa-diploma#readme).

## Тест кейсы.
Мной был составлен *22 тест-кейса*, среди которых *10 проваленых* и *12 завершились успешно*, 
что составляет общий процент успешности завершения автотестов - 54,54%. 
![аллюр](https://github.com/007Nick91/Diploma-Bondarenko/assets/125663805/21fc33e4-637c-436a-bee9-df63268dde44)


[Allure]([[http://localhost:63342/Diploma/build/reports/allure-report/allureReport/index.html?_ijt=a9iv392ijgibc5sodlesulk0ib&_ij_reload=RELOAD_ON_SAVE#](http://localhost:63342/Diploma/build/reports/allure-report/allureReport/index.html?_ijt=offdf32fhav4bleugmrjbcdt1l&_ij_reload=RELOAD_ON_SAVE)](http://localhost:63342/Diploma/build/reports/allure-report/allureReport/index.html?_ijt=tu6uo7dvi9dt1ps2p6lq8c7lqr&_ij_reload=RELOAD_ON_SAVE#))

## Рекомендации.
Согласно проведенного тестировани, тебуется доработка и исправление ошибок на веб-сервисе **"Путешествие дня"**, а именно: 
1. [Отображение в БД информации об успешно осуществленной покупки с "Отклоненной карты" 
        как на вкладке "Купить", так и на вкладке "Купить в кредит"](https://github.com/007Nick91/Diploma-Bondarenko/issues/7)
2. [Возможность ввода в поле "Владелец" невалидных значений](https://github.com/007Nick91/Diploma-Bondarenko/issues/4)
3. [Возможность ввода в поле "Месяц" невалидных значений](https://github.com/007Nick91/Diploma-Bondarenko/issues/2)
4. [Орфографических ошибок](https://github.com/007Nick91/Diploma-Bondarenko/issues/1)
5. [Совершение покупки с "Отклоненной" банковской карты](https://github.com/007Nick91/Diploma-Bondarenko/issues/6)
6. [Отображение под корректно заполненноыми полями, уведомлений об ошибке](https://github.com/007Nick91/Diploma-Bondarenko/issues/8)
7. [Изменить название вкладки](https://github.com/007Nick91/Diploma-Bondarenko/issues/9)
8. [Отображение одновременно двух уведомлений об "Ошибке" и "Успешно"](https://github.com/007Nick91/Diploma-Bondarenko/issues/5)
