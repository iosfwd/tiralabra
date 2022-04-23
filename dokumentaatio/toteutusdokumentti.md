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
