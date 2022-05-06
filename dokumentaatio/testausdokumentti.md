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
| Enkoodaus     | 26,65 sek | 27,61 sek    | 17,62 sek |
| Dekoodaus     | 36,03 sek | 37,85 sek    | 35,04 sek |
| Pakkaus-suhde | 46,5%     | 60,4%        | 25,4%     |


| Huffman       | dna.10MB  | english.10MB | xml.10MB  |
|:--------------|:----------|:-------------|:----------|
| Enkoodaus     | 28,57 sek | 38,04 sek    | 42,28 sek |
| Dekoodaus     | 46,33 sek | 78,62 sek    | 79,82 sek |
| Pakkaus-suhde | 27,7%     | 57,4%        | 65,7%     |


| LZHuffman     | dna.10MB  | english.10MB | xml.10MB  |
|:--------------|:----------|:-------------|:----------|
| Enkoodaus     | 25,62 sek | 27,77 sek    | 17,78 sek |
| Dekoodaus     | 36,40 sek | 38,36 sek    | 36,64 sek |
| Pakkaus-suhde | 46,5%     | 59,2%        | 24,5%     |


Huffman-koodaus suoriutuu parhaiten DNA:ta pakatessa. DNA-aakkosto on
hyvin rajallinen (A, C, G, T), ja niiden jakauman voi olettaa olevan
melko tasainen, joten jokainen symboli saa keskimäärin kahden bitin
pituisen koodin. Normaalisti yhden symbolin tallennus vie tavun, mutta
Huffman-koodattuna keskimäärin vain neljäsosan tavusta, ja tämä myös
heijastuu pakkaussuhteessa, joka on lähes neljäsosa alkuperäisestä.

Dekoodaus on selkeästi hitaampaa kuin enkoodaus, koska yhden symbolin
dekoodauksen aikvaativuus on logaritminen.

Huffman-koodaus on myös hieman parempi muiden pakkaajien suhteen
englanninkielisen tekstin kanssa. Vaikka englanninkielisen tekstin
aakkoston voi olettaa sisältävän kaiki alfanumeeriset merkit, niiden
jakauma on hyvin epätasainen. Englannin kielessä esiintyy paljon
tiettyjä symboleja kuten a, e, i, ja näin ollen Huffman-koodauksesta
saadaan hyötyjä irti.

Isompi aakkosot heijastuu kuitenkin dekoodauksessa, jossa se vie
melkein kaksi kertaa pitempään kuin DNA:lla, koska yhden symbolin
dekoodauksen aikavaativuus on logaritminen.



## Yksikkötestien kattavuus

![Kattavuusraportti](./kattavuus.png)
