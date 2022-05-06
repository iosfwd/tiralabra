# Toteutusdokumentti

## Huffman

Huffman-enkoodaus käy sisääntulona saadun syötteen kaksi kertaa läpi.
Ensimmäisellä kierroksella se taulukoi eri symbolien esiintymien
lukumäärät. Symbolien esiintymien lukumäärillä rakennetaan Huffman-puu
jonka avulla rakennetaan hyppytaulukko johon on tallennettu
esiintyvien symbolien koodit ja niiden pituudet. Tämän jälkeen puu
serialisoidaan ulostuloon, ja enkoodaaja käy syötteen toiseen kertaan
läpi ja kirjoittaa samalla ulostuloon sisääntulossa vastaan tulevien
symbolien koodit kooditaulukon avulla.

Huffman-dekoodaus deserialisoi sisääntulosta puun, jonka avulla se saa
konstruktoitua koodauksessa käytetyn kooditaulukon, ja sen avulla
dekoodauksessa käytettävät aputaulukot. Tämän jälkeen dekoodaaja käy
syötettä läpi, irjoittaen vastaantulevia koodeja vastaavia symboleja
ulostuloon.

### Luokat

* HuffmanEncoder

  HuffmanEncoder vastaa annetun sisääntulon Huffman-koodauksesta
  annettuun ulostuloon. Laskee myös symbolien esiintymät
  sisääntulosta.


* HuffmanDecoder

  HuffmanDecoder vastaa annetun sisääntulon Huffman-dekoodauksesta
  annettuun ulostuloon.


* HuffmanTable

  HuffmanTable rakentaa symbolien lukumääristä Huffman-puun ja luo ja
  säilöö Huffman-koodit niille.


* HuffmanNode

  Huffman-puun konstruktiossa käytettävä solmua esittävä dataluokka.


* HuffmanCodeword

  Jonkin symbolin Huffman-koodin ja sen pituuden säilövä dataluokka.


* HuffmanCodebook

  HuffmanCodebook rakentaa serialisoidusta Huffman-puusta
  Huffman-dekoodaukseen käytettävät taulukot ja säilöö yksittäisen
  koodin dekoodaukseen käytettävät metodit.


* PrefixCoder

  PrefixCoder luo prefiksi-koodit (symboli, koodin pituus)-pareille.


## Lempel-Ziv

Lempel-Ziv-enkoodaaja lukee sisääntuloa ja pitää puskurissa tietyn
verran aiemmin käsiteltyä sisääntuloa, josta se yrittää etsiä
toistuvuuksia. Jos toistuvuuksia ei löydy, tai ne ovat liian lyhyitä,
se kirjoittaa kohtaamansa symbolit ulostuloon. Mikäli se löytää
toistuvuuden, se kirjoittaa ulostuloon (offset, pituus)-parin, jossa
offset kertoo kuinka monta merkkiä taaksepäin toistuvuus alkaa ja
kuinka pitkä se on.

Lempel-Ziv dekoodaaja lukee sisääntuloa, ja pitää puskurissa tietyn
verran aiemmin käsiteltyä sisääntuloa. Jos se kohtaa symbolin se
kopioi symbolin ulostuloon. Mikäli se kohtaa (offset, pituus)-parin,
se kopioi offsetin verran takaperin päästä pituuden verran merkkejä
ulostuloon.

### Luokat

* LZEncoder

  LZEncoder vastaa annetun sisääntulon Lempel-Ziv-pakkauksesta annettuun
  ulostuloon.


* LZDecoder

  LZDecoder vastaa annetun sisääntulon Lempel-Ziv-purkamisesta
  annettuun ulostuloon.


* HashChain

  HashChain tallentaa ja yrittää hakea liukuvassa ikkunassa tavattujen
  symbolien sijainnit toistuvuuksien etsimistä varten.

## IO

### Luokat

* BitWriter

  BitWriter toteuttaa bittitason kirjoittamisen. Sillä voi kirjoittaa
  yksittäisiä bittejä ja vaihtelevan pituisia bittijonoja.

* BitReader

  BitReader toteuttaa bittitason lukemisen. Sillä voi lukea
  yksittäisiä bittejä ja vaihtelevan pituisia bittijonoja.

* ResetableFileInputStream

  Kelattava tiedostonluku joka käärii alleen Javan
  RandomAccessFile-luokan. Javan oma FileInputStream-luokka ei tue
  mark()- ja reset()-metodeja.

## Hyötyluokat

### Luokat

* BinaryLogarithm

  Toteuttaa kaksikantaisen logaritmin kokonaisluvuille.



## Aikavaativuudet

### Huffman-enkoodaus


Sisääntulon symbolien esiintymien skannaamisen aikavaativuus on
lineaarinen sisääntulon pituuden suhteen, koska siinä tarvitsee vain
käydä sisääntulo läpi.


Olkoon A aakkosto ja |A| aakkoston koko. Huffman-puu on binääripuu
jonka avulla aakkoston alkioille saadaan optimaaliset koodit.
Huffman-puun rakentaminen tapahtuu seuraavasti:


    Sisääntulo: taulukko S[1,[A]] missä kukin alkio sisältää (symboli,
                symbolin paino)-parin.

    Ulostulo: Huffman-puun juurisolmu


    Järjestä taulukko kasvavaan järjestykseen symbolien painojen mukaan.

    Luo S:ästä linkitetty listä L missä i:nnes alkio säilöö (symboli,
    symbolin paino, seuraava alkio, vasen lehti, oikea
    lehti)-viisikon, missä seuraava alkio on on osoitin S[i + 1]
    vastaavaan alkioon, ja lehdet ovat null-osoittimia.

    I <- L (listan ensimmäinen alkio)

    while L.seuraava_alkio != null {

          Luo alkio N jolla N.vasen <- L:n pää, N.oikea <- L.seuraava, N.paino <- (L.paino + L.seuraava.paino)

          while (I.seuraava != null && I.seuraava.paino <= N.paino) {

                I <- I.seuraava

          }

          N.seuraava <- I.seuraava

          I.seuraava <- N

          L <- L.seuraava.seuraava

    }

    palauta L


Huffman-puun rakentaminen on ahne algoritmi, joka poistaa aina listan
päässä olevat kaksi kevyintä puuta ja yhdistää ne uudeksi puuksi.
Sitten se hakee uudelle puulle paikan listasta. Vaikka puun paikkaa
skannataan lineaarisesti listasta, listaa ei käydä läpi
kokonaisuudessaan kuin vain kerran, koska uusi puu on aina raskaampi
kuin yksinään aiempi puu listassa. Puun alkioiden yläraja on 2|A|,
joten järjestämisen jälkeen aikavaativuus on O(|A|). Järjestäminen vie
O(|A| log |A|) aikaa, joten se dominoi puun rakentamista. Näin ollen
järjestäminen huomioituna Huffman-puun rakentamisen aikavaativuus on
O(|A| log |A|).


Huffman-puun tilavaativuus on O(|A|) koska se konstruktoi binääripuun
syötteen aakkostosta.


Huffman-koodien pituudet saadaan generoitua kulkemalla puuta rekursiivisesti
ja pitämällä kirjaa syvyydestä kunnes saavutaan lehtisolmuun.

Prefiksi-koodit symboleille saadaan (symboli, koodin pituus)-pareista:


    Sisääntulo: taulukko S[1,|A|] missä alkiot ovat (symboli, koodin pituus)-pareja

    Ulostulo: taulukko (symboli, koodin pituus, koodi)-kolmikkoja

    Järjestä S koodien pituuksien mukaan

    S[1].koodi <- 0

    for (i <- 2 to |S|) {

        S[i].koodi <- S[i - 1].koodi + 1 << (S[i].pituus - S[i - 1].pituus)

    }

    palauta S


Järjestäminen huomioimatta prefiksikoodaus vie lineaarisesti aikaa
aakkoston koon suhteen.


Symbolit, koodit, ja niiden pituudet voidaan tallentaa
hyppytaulukkoon, jolloin symbolin koodauksen aikavaativuus on O(1).
Sisääntulon koodaaminen ulostuloon on lineaarinen suhteessa
sisääntulon kokoon, koska sisääntulo täytyy kulkea läpi kerran,
koodaten symboli ulostuloon yksi kerrallaan, joten n pitkän
sisääntulon koodauksen aikavaativuus on O(n).


Hyppytaulukon tilavaativuus on O(|A|), koska se tallentaa koodin
jokaiselle syötteen aakkoston symbolille.


### Huffman-dekoodaus

Huffman-dekoodaaja lukee sisääntulosta serialisoidun Huffman-puun.
Sitten symboleille lasketaan prefiksi-kodit, ja luodaan kolme
aputaulukkoa, missä h = O(log n) on puun korkeus:

* Taulukko L[1,|A|] sisältää
  puun tallentamat symbolit järjestyksessä vasemmalta-oikealle-ylhäältä-alas.

* Taulukko F[1,h] tallentaa alkioon F[i] ensimmäisen
  i-pitkän koodin symbolin indeksin L:ssä. Jos i-pituisia koodeja ei
  ole, F[i] = F[i + 1].

* Taulukko C[1,h] tallentaa alkioon C[i] symbolin L[F[i]]
  koodin. Jos i-pitkiä koodeja ei ole, niin C[i] = C[i + 1] / 2.

Prefiksikoodauksen jälkeen aputaulukot saa rakennettua lineaarisesti
skannaamalla. Prefiksikoodaukseen vaadittava järjestäminen dominoi
aputaulukkojen rakentamista, joten sen aikavaativuus on O(|A| log
|A|).

Yhden symbolin dekoodaus sisääntulosta tapahtuu seuraavasti:


      N <- h verran bittejä sisääntulosta

      Etsi binäärihakua käyttäen i siten että (N < C[i+1] << (h - i - 1)) ja (N >= C[i] << (h - i))

      N <- (N >> h - i)

      Siirry i eteenpäin sisääntulossa

      Palauta L[F[i] + N - C[i]]


Koska dekoodaus käyttää binäärihakua, sen aikavaativuus on O(log h) = O(log log n).


Koko sisääntulon dekoodaamista varten se pitää käydä kokonaan läpi, ja
yhden merkin dekoodaus vie O(log h) = O(log log n) aikaa, niin
dekoodauksen n symbolin dekoodauksen aikavaativuus on O(n log h) = O(n
log log n).


### Lempel-Ziv-pakkaus

Lempel-Ziv-pakkaaminen käsittelee sisääntulon seuraavasti:


    i <- 1

    while (i <= size) {

        Hae pisin osuma ennen i:tä enintään ikkunan pituuden päästä

        offset <- -(pisimmän osuman indeksi - i)

        if (osuman pituus < minimi pituus) {

            Laita nollabitti ja nykyisellä kohdalla oleva symboli ulostuloon

            i <- i + 1

        } else {

            Laita ykkösbitti ja (offset, osuman pituus)-pari ulostuloon

            i <- i + pituus

        }

    }


Naiivisti toteutettu Lempel-Ziv-pakkaaja kävisi aina osumaa etsiessä
koko aiemman sisääntulon läpi, jolloin sen aikavaativuus olisi O(n^2),
missä n on syötteen pituus. Liukuvaa ikkunaa käytettäessä pakkaaja
etsii osumia enintään ikkunan pituuden päästä nykyisestä kohdasta,
joten jos w olisi ikkunan pituus, niin aikavaativuus olisi O(nw).


Liukuvaa ikkunaa käyttävän Lempel-Ziv-pakkaamisen tilavaativuus on
O(w) koska sen täytyy pitää muistissa ikkunan pituuden verran aiemmin
käsiteltyä syötettä.


### Lempel-Ziv-purkaminen

Lempel-Ziv-purkaminen tapahtuu seuraavasti:


    i <- 1

        while (i <= size) {

        Lue bitti b sisääntulosta

        if (b = 0) {

           Lue symboli ja laita se ulostuloon

           i <- i + 1

        } else {

          Lue (offset, pituus)-pari sisääntulosta

          Kopioi kohdasta (i  - offset) pituuden verran merkkejä ulostuloon

          i <- i + pituus

        }

    }


Purkamista varten sisääntulo täytyy käydä kerran läpi, ja kopioiminen
vie aikaa lineaarisesti kopioitavan osuuden pituuden verran, joten
purkamisen aikavaativuus on O(n), missä n on sisääntulon koko.

Lempel-Ziv-purkamisen tilavaativuus myös on O(w) koska sen täytyy
pitää muistissa ikkunan pituuden verran aiemmin käsiteltyä syötettä.

## Lähteet

Gonzalo Navarro. Compact Data Structures: A Practical Approach.

Kunihiko Sadakane. An improvement on hash-based algorithms for searching the longest-match string used in LZ77-type data compression.
