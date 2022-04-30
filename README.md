# Tiralabra

## Käyttöohje

Testi-ohjelman saa ajettua näin:

    $ cd compress

    $ mvn package

    $ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.RunTests


Ohjelmia saa muuten ajettua näin:

* Kääntäminen:

	`$ cd compress`

	`$ mvn package`


* LZ-enkoodaus:

	`$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.LZ e polku/inputiin polku/outputiin`


* LZ-dekoodaus:

	`$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.LZ d polku/inputiin polku/outputiin`


* Huffman-enkoodaus:

	`$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.Huffman e polku/inputiin polku/outputiin`


* Huffman-dekoodaus:

	`$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.Huffman d polku/inputiin polku/outputiin`

* LZHuffman-enkoodaus:

	`$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.LZHuffman e polku/inputiin polku/outputiin`


* LZHuffman-dekoodaus:

	`$ java -cp target/org.jmv.compress-1.0-SNAPSHOT.jar org.jmv.compress.LZHuffman d polku/inputiin polku/outputiin`


## Viikkoraportit
[Viikkoraportti 1](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/viikkoraportti1.md)

[Viikkoraportti 2](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/viikkoraportti2.md)

[Viikkoraportti 3](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/viikkoraportti3.md)

[Viikkoraportti 4](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/viikkoraportti4.md)

[Viikkoraportti 5](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/viikkoraportti5.md)

[Viikkoraportti 6](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/viikkoraportti6.md)

## Dokumentaatio

[Määrittelydokumentti](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/m%C3%A4%C3%A4rittelydokumentti.md)

[Testausdokumentti](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/testausdokumentti.md)

[Toteutusdokumentti](https://github.com/iosfwd/tiralabra/blob/main/dokumentaatio/toteutusdokumentti.md)
