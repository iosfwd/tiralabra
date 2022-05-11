# Testausdokumentti

## Suorituskyky

RunTests-ohjelma ajaa LZ-enkoodauksen ja -dekoodauksen,
Huffman-enkoodauksen ja -dekoodauksen, sekä niiden yhdistelmän
kolmelle eri 50MB kokoiselle testitiedostolle. Ohjelma raportoi
kullekin näistä luettujen ja kirjoitettujen tiedostojen koot,
saavutetun pakkaussuhteen, sekä ajoajan.

Testi-ohjelman saa ajettua näin:

	$ cd compress

	$ mvn package

	$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.RunTests

RunBigTests-ohjelma ajaa LZ-enkoodauksen ja -dekoodauksen,
Huffman-enkoodauksen ja -dekoodauksen, sekä niiden yhdistelmän
kolmelle eri 200MB kokoiselle testitiedostolle. Ohjelma raportoi
kullekin näistä luettujen ja kirjoitettujen tiedostojen koot,
saavutetun pakkaussuhteen, sekä ajoajan.

Testidata on sivustolta
<http://pizzachili.dcc.uchile.cl/index.html>, joka ylläpitää
testimateriaalia tiiviille tietorakenteille ja tiedostonpakkaukselle.
Testidata koostuu kolmesta tiedostosta, joista ensimmäinen on
DNA:ta, toinen on englannin kielistä tekstiä, ja kolmas on XML-kieltä.

Testidatan kuvaukset ovat seuraavat:

- dna: This file is a sequence of newline-separated gene DNA sequences
  (without descriptions, just the bare DNA code) obtained from files
  01hgp10 to 21hgp10, plus 0xhgp10 and 0yhgp10, from Gutenberg
  Project. Each of the 4 bases is coded as an uppercase letter
  A,G,C,T, and there are a few occurrences of other special
  characters.


- english: This file is the concatenation of English text files
  selected from etext02 to etext05 collections of Gutenberg Project.
  We deleted the headers related to the project so as to leave just
  the real text.


- xml: This file is an XML that provides bibliographic information on
  major computer science journals and proceedings and it is obtained
  from dblp.uni-trier.de.


| Lempel-Ziv   | dna.10MB  | english.10MB | xml.10MB  |
|:-------------|:----------|:-------------|:----------|
| Enkoodaus    | 26,65 sek | 27,61 sek    | 17,62 sek |
| Dekoodaus    | 36,03 sek | 37,85 sek    | 35,04 sek |
| Pakattu koko | 46,5%     | 60,4%        | 25,4%     |


| Huffman      | dna.10MB  | english.10MB | xml.10MB  |
|:-------------|:----------|:-------------|:----------|
| Enkoodaus    | 28,57 sek | 38,04 sek    | 42,28 sek |
| Dekoodaus    | 46,33 sek | 78,62 sek    | 79,82 sek |
| Pakattu koko | 27,7%     | 57,4%        | 65,7%     |


| LZHuffman    | dna.10MB  | english.10MB | xml.10MB  |
|:-------------|:----------|:-------------|:----------|
| Enkoodaus    | 25,62 sek | 27,77 sek    | 17,78 sek |
| Dekoodaus    | 36,40 sek | 38,36 sek    | 36,64 sek |
| Pakattu koko | 46,5%     | 59,2%        | 24,5%     |

### Lempel-Ziv

Lempel-Ziv-pakkaamisessa ikkunan pituutena käytettiin 2^15 merkkiä,
osuman vähimmäispituutena 3 merkkiä, enimmäispituutena 258 merkkiä, ja
enimmillään tarkistettavien osumien lukumääränä 128. Valitsin nämä
luvut koska muutkin Lempel-Ziv-pakkaajat käyttävät suunnilleen
vastaavia lukuja.

Lempel-Ziv-pakkauksella DNA-tiedoston sai pakattua alle puoleen sen
alkuperäisestä koosta. DNA-aakkosto on rajattu, joten on todennäköistä
että DNA-merkkijonosta löytyy toistuvia alimerkkijonoja. Lempel-Ziv
häviää kuitenkin Huffman-koodaukselle, joka saa hyödynnettyä rajattua
aakkostoa paremmin.

Lempel-Ziv on melko tasoissa toisten pakkausmetodien kanssa
englanninkielisen tekstin suhteen, jonka se saa pakattua 60%
alkuperäisestä koosta. Englanninkielisessä tekstissä esiintyy
toistuvasti tiettyjä sanoja, mutta ne eivät välttämättä esiinny
yhdessä, jolloin algoritmi ei välttämättä löydä pitkiä toistuvia
alimerkkijonoja ikkunan sisältä.

Lempel-Ziv suoriutuu selkeästi Huffmania paremmin XML:än kanssa. Koska
XML:ässä on paljon paikallista toisteisuutta, Lempel-Ziv löytää hyviä
osumia nopeasti, ja saa siten pakattua XML:ää tehokkaasti, neljäsosaan
alkuperäisestä koosta.

Lempel-Ziv suoriutuu purkamisessa tasaisesti ja kaikista parhaiten. Se
on algoritmisesti hyvin yksinkertainen ja aikavaadivuudeltaan
lineaariaikainen, joka näkyy ajoajoissa.


### Huffman

Huffman-koodaus suoriutuu selkeästi parhaiten DNA:ta pakatessa.
DNA-aakkosto on hyvin rajallinen (A, C, G, T), ja niiden jakauman voi
olettaa olevan melko tasainen, joten jokainen symboli saa keskimäärin
kahden bitin pituisen koodin. Normaalisti yhden symbolin tallennus vie
tavun, mutta Huffman-koodattuna keskimäärin vain neljäsosan tavusta,
ja tämä myös heijastuu pakkaussuhteessa, joka on lähes neljäsosa
alkuperäisestä.

Dekoodaus on selkeästi hitaampaa kuin enkoodaus, koska yhden symbolin
dekoodauksen aikvaativuus on logaritminen.

Huffman-koodaus on myös hieman parempi muiden pakkaajien suhteen
englanninkielisen tekstin kanssa. Vaikka englanninkielisen tekstin
aakkoston voi olettaa sisältävän kaiki alfanumeeriset merkit, niiden
jakauma on hyvin epätasainen. Englannin kielessä esiintyy paljon
tiettyjä symboleja kuten a, e, i, ja näin ollen Huffman-koodauksesta
saadaan hyötyjä irti.

Isompi aakkosto heijastuu kuitenkin dekoodauksessa, jossa se vie
melkein kaksi kertaa pitempään kuin DNA:lla, koska yhden symbolin
dekoodauksen aikavaativuus on logaritminen.

Huffman-koodaus toimii XML:älle heikommin kuin Lempel-Ziv. XML:ässä
aakkosto sisältää todennäköisesti alfanumeeristen merkkien lisäksi
erikoismerkkejä, ja merkkien jakauma on melko tasainen. Näin ollen
Huffman-puu antaa melkein yhtäpitkiä koodeja kaikille symboleille.

Isosta aakkostosta johtuen XML:än dekoodaus on myös hidasta.

### LZHuffman

LZHuffman toimii muuten kuten Lempel-Ziv-pakkaus, paitsi että se
Huffman-koodaa literaalit. Sen tulokset eivät olleet kovin
merkittävästi Lempel-Ziviä parempia, parhaimmillaan se paransi
pakkausta noin prosentin verran.

Purkaminen oli vain hieman hitaampaa kuin pelkällä Lempel-Zivillä.
LZHuffman-tulokset antavat kuitenkin antoi hyvin osviittaa siitä että
Lempel-Ziv löysi osumia parhaansa mukaan.


## Yksikkötestien kattavuus

![Kattavuusraportti](./kattavuus.png)
