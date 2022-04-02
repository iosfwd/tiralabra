# Viikkoraportti 3

- Aloitin tällä viikolla työstämään Lempel-Ziv-pakkausta.

- Toteutin hyvin yksinkertaisen LZ77-pakkaajan ja -purkajan. Pakkaaja
  yrittää hakea parasta mahdollista osumaa lineaarihaulla
  liukuvasta ikkunasta, ja koodaa kaiken (offset, pituus, seuraava
  symboli)-kolmikkoina.

- Opin tällä viikolla implementoimaan karvalakki-mallin LZ77-pakkaajan
  ja -purkajan.

- Seuraavaksi ajattelin toteuttaa tehokkaamman keinon löytää aiempia
  osumia kuin lineaarihaku ja lisätä kulujen vertailun, eli
  kannattaako pakkaajan koodata osumat joko edellä mainittuna
  kolmikkoina vaiko kaikki osuman symbolit literaaleina.

- Käytin tällä viikolla aikaa noin 8 tuntia.
