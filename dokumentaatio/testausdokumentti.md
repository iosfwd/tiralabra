# Testausdokumentti

## Suorituskyky

RunTests-ohjelma ajaa LZ-enkoodauksen ja -dekoodauksen,
Huffman-enkoodauksen ja -dekoodauksen, sekä niiden yhdistelmän
kolmelle eri 1MB kokoiselle testitiedostolle. Ohjelma raportoi
kullekin näistä luettujen ja kirjoitettujen tiedostojen koot,
saavutetun pakkaussuhteen, sekä ajoajan.

RunBigTests-ohjelma ajaa LZ-enkoodauksen ja -dekoodauksen,
Huffman-enkoodauksen ja -dekoodauksen, sekä niiden yhdistelmän
kolmelle eri 10MB kokoiselle testitiedostolle. Ohjelma raportoi
kullekin näistä luettujen ja kirjoitettujen tiedostojen koot,
saavutetun pakkaussuhteen, sekä ajoajan.

Testi-ohjelman saa ajettua näin:

	$ cd compress

	$ mvn package

	$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.RunTests

Isommat testit saa ajettua näin:

	$ cd compress

	$ mvn package

	$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.RunBigTests

Testidata on sivustolta
<http://pizzachili.dcc.uchile.cl/index.html>, joka ylläpitää
testimateriaalia tiiviille tietorakenteille ja tiedostonpakkaukselle.
Testidata koostuu kolmesta 1MB tiedostosta, joista ensimmäinen on
DNA:ta, toinen on englannin kielistä tekstiä, ja kolmas on XML-kieltä.


| LZ            | dna.10MB  | english.10MB | xml.10MB  |
|:--------------|:----------|:-------------|:----------|
| Enkoodaus     | 27,14 sek | 29,74 sek    | 18,96 sek |
| Dekoodaus     | 36,47 sek | 36,80 sek    | 33,24 sek |
| Pakkaus-suhde | 53%       | 60%          | 25%       |


| Huffman       | dna.10MB  | english.10MB | xml.10MB  |
|:--------------|:----------|:-------------|:----------|
| Enkoodaus     | 28,42 sek | 38,67 sek    | 40,72 sek |
| Dekoodaus     | 47,54 sek | 75,84 sek    | 80,48 sek |
| Pakkaus-suhde | 28%       | 57%          | 66%       |


| LZHuffman     | dna.10MB  | english.10MB | xml.10MB  |
|:--------------|:----------|:-------------|:----------|
| Enkoodaus     | 26,76 sek | 28,90 sek    | 17,64 sek |
| Dekoodaus     | 35,17 sek | 38,27,84 sek | 34,12 sek |
| Pakkaus-suhde | 53%       | 60%          | 25%       |


## Yksikkötestien kattavuus

![Kattavuusraportti](./kattavuus.png)
