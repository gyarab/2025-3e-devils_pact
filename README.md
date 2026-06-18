# Devil's Pact
 
2D roguelike hra s kasinovou tematikou, napsaná v Javě s využitím JavaFX.
 
## Gameplay
 
Hráč prochází 10 levely složenými z místností. Cestou poráží nepřátele a bosse, sbírá nebo kupuje vylepšení a na konci se utká s ďáblem.
 
Souboje probíhají na základě hodu kostkami. Každé kolo hráč hodí 3 kostkami — útok, štít a léčení — dokud nepřítele neporazí.
 
Dostupná vylepšení: maximální životy, kapacita štítu, útočné poškození.
 
## Ovládání
 šipky — pohyb
 akce ve hře (útok, hod kostkami a další) - mezerník
 koupit v obchodu - enter
## Technologie
 
- Java + JavaFX
- Pixel art vytvořen v aplikaci Aseprite
## Spuštění

Tato hra je naprogramována v jazyce **Java** a pro grafické rozhraní využívá framework **JavaFX**. Protože JavaFX není od verze JDK 11 zabudováno přímo v základní Javě, je potřeba při spouštění dodržet několik kroků.

### Požadavky na systém
* **Java Development Kit (JDK)** - doporučena verze 17 nebo novější.
* **JavaFX SDK** (případně vývojové prostředí, které ho stahuje automaticky, nebo nástroj typu Maven/Gradle).

### Důležité: Struktura složek (Assets)
Aby hra správně načetla veškeré textury a nespadla (nebo nevykreslila pouze zástupné barevné čtverce), musí být složka `Assets` umístěna přímo v kořenovém adresáři zdrojových kódů (vedle balíčku `projectfx`).

Správná struktura projektu vypadá takto:
```text
/src
  ├── /Assets           <-- Zde musí být složky s texturami (Backgrounds, Characters, atd.)
  └── /projectfx
        └── DevilsPact.java
```

## Dokumentace

- https://docs.google.com/document/d/1QfPm7urmsPQIhDnOnBkZ8viBnhjiV1ZGYF88hDf76Xc/edit?usp=sharing
