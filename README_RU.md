[Русский](README_RU.md) | [English](README.md)

# YMeshenger

Прямые голосовые и видеозвонки. Не требуются аккаунты или доступ в Интернет. Просто отсканируйте QR-код друг друга, который будет содержать IP-адрес контакта. Работает в домашних или корпоративных сетях, а также во многих автономных сетях, таких как общественные mesh-сети.

Возможности:

- голосовые и видеозвонки
- зашифрованная связь
- без аккаунтов, регистрации и рекламы
- добавление пользовательских адресов для связи с контактами

Ограничения:

- звонки через NAT и файрволы невозможны
- изменение IP-адреса требует ручного обновления контакта

## Скриншоты

<img src="graphical-assets/phone-screenshots/logo_4.0.4.png" width="170"> <img src="graphical-assets/phone-screenshots/hello_4.0.4.png" width="170"> <img src="graphical-assets/phone-screenshots/qrcode_4.0.4.png" width="170"> <img src="graphical-assets/phone-screenshots/contacts_4.0.4.png" width="170"> <img src="graphical-assets/phone-screenshots/ringing_4.0.4.png" width="170"> <img src="graphical-assets/phone-screenshots/video_call_4.0.4.png" width="170"> <img src="graphical-assets/phone-screenshots/settings_4.0.4.png" width="170"> <img src="graphical-assets/phone-screenshots/address_management_4.0.4.png" width="170">

## Документация

YMeshenger подключается к IP-адресам в истинно P2P-стиле, используя сеть Yggdrasil. Контакты кодируются в текстовый блок, который можно обменивать через QR-код, изображение или копирование. Они содержат имя, открытый ключ и список IP-адресов или доменных имен. Обмененный открытый ключ используется для аутентификации/шифрования сигнальных данных для установления сессии [WebRTC](https://webrtc.org/), которая может передавать голос и видео.

Подробности можно найти в [Документации](docs/documentation.md) или [FAQ](docs/faq.md).

Скачать последнюю версию: [GitHub Releases](https://github.com/JB-SelfCompany/YMeshenger/releases)

## Похожие проекты

Этот список содержит только Open Source проекты.

* [linphone](https://linphone.org/) - использует SIP, может работать с IP-адресами, но неудобен в использовании
* [pion offline](https://github.com/pion/offline-browser-communication) - Демо Offline WebRTC
* [SideBand](https://github.com/markqvist/Sideband) - Чат-приложение на основе сети Reiculum
* [Qaul](https://qaul.net/) - только текстовые сообщения, связанная P2P mesh-сеть через BLE, Wifi и интернет-оверлей