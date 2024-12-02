package com.nutritiongame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardDeck {
    private List<Card> allCards;    // All available cards
    private List<Card> currentDeck; // Current game deck

    public CardDeck() {
        allCards = new ArrayList<>();
        currentDeck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        // Protein-rich foods (new category)
        allCards.add(new Card("Pollo", "/resources/images/cards/Cartas Proteinas/Carta_pollo.png",
                Card.CardType.PROTEIN, 25, 5, 0, 85,
                "El pollo es una excelente fuente de proteína magra."));
        allCards.add(new Card("Huevos", "/resources/images/cards/Cartas Proteinas/Carta_huevo.png",
                Card.CardType.PROTEIN, 13, 8, 1, 80,
                "Los huevos son ricos en proteínas y vitaminas."));
        allCards.add(new Card("Frijoles", "/resources/images/cards/Cartas Proteinas/Carta_frijol.png",
                Card.CardType.PROTEIN, 15, 7, 20, 85,
                "Los frijoles tienen proteína vegetal y fibra para tu salud."));

        // Foodtrash Cards
        allCards.add(new Card("Chocolate", "/resources/images/cards/Cartas Foodtrash/Comida_chocolate.png",
                Card.CardType.FOODTRASH, 1, 0, 20, 20,
                "El chocolate tiene mucha azúcar."));
        allCards.add(new Card("Hamburguesa", "/resources/images/cards/Cartas Foodtrash/Foodtrash_Hamburguesa.png",
                Card.CardType.FOODTRASH, 5, 1, 15, 30,
                "La hamburguesa tiene muchas grasas."));
        allCards.add(new Card("Hot Dog", "/resources/images/cards/Cartas Foodtrash/Foodtrash_HotDog.png",
                Card.CardType.FOODTRASH, 4, 0, 18, 25,
                "El hot dog tiene muchos conservadores."));
        allCards.add(new Card("Pizza", "/resources/images/cards/Cartas Foodtrash/Foodtrash_Pizza.png",
                Card.CardType.FOODTRASH, 6, 2, 25, 35,
                "La pizza tiene muchas calorías."));
        allCards.add(new Card("Soda", "/resources/images/cards/Cartas Foodtrash/Foodtrash_Soda.png",
                Card.CardType.FOODTRASH, 0, 0, 30, 10,
                "La soda tiene mucha azúcar y nada de nutrientes."));

        // Fruit Cards with adjusted values
        allCards.add(new Card("Sandía", "/resources/images/cards/Cartas Frutas/Carta_fruta_Sandia.png",
                Card.CardType.FRUIT, 2, 8, 10, 75,
                "La sandía te mantiene hidratado."));
        allCards.add(new Card("Mango", "/resources/images/cards/Cartas Frutas/Carta_Mango.png",
                Card.CardType.FRUIT, 1, 12, 15, 80,
                "El mango es rico en vitamina A."));
        allCards.add(new Card("Manzana", "/resources/images/cards/Cartas Frutas/Carta_manzana.png",
                Card.CardType.FRUIT, 0, 10, 12, 85,
                "La manzana ayuda a tu digestión."));
        allCards.add(new Card("Naranja", "/resources/images/cards/Cartas Frutas/Carta_Naranja.png",
                Card.CardType.FRUIT, 1, 15, 8, 90,
                "La naranja es rica en vitamina C."));
        allCards.add(new Card("Plátano", "/resources/images/cards/Cartas Frutas/carta_platano.png",
                Card.CardType.FRUIT, 2, 8, 20, 75,
                "El plátano te da energía."));
        allCards.add(new Card("Fresa", "/resources/images/cards/Cartas Frutas/fruta_fresa.png",
                Card.CardType.FRUIT, 1, 9, 5, 85,
                "Las fresas tienen antioxidantes."));

        // Vegetable Cards with adjusted values
        allCards.add(new Card("Cebolla", "/resources/images/cards/Cartas Verduras/Carta_Cebolla.png",
                Card.CardType.VEGETABLE, 1, 7, 5, 80,
                "La cebolla ayuda a tu sistema inmune."));
        allCards.add(new Card("Lechuga", "/resources/images/cards/Cartas Verduras/Carta_lechuga.png",
                Card.CardType.VEGETABLE, 1, 8, 2, 85,
                "La lechuga es baja en calorías."));
        allCards.add(new Card("Pepino", "/resources/images/cards/Cartas Verduras/Carta_Pepino.png",
                Card.CardType.VEGETABLE, 1, 6, 3, 85,
                "El pepino te mantiene hidratado."));
        allCards.add(new Card("Tomate", "/resources/images/cards/Cartas Verduras/Carta_Tomate.png",
                Card.CardType.VEGETABLE, 1, 9, 4, 90,
                "El tomate tiene licopeno para tu salud."));
        allCards.add(new Card("Zanahoria", "/resources/images/cards/Cartas Verduras/Carta_Zanahoria.png",
                Card.CardType.VEGETABLE, 1, 12, 6, 95,
                "La zanahoria es buena para tus ojos."));
        allCards.add(new Card("Brócoli", "/resources/images/cards/Cartas Verduras/Cartas_Brocoli.png",
                Card.CardType.VEGETABLE, 4, 10, 5, 100,
                "El brócoli es un supervegetal nutritivo."));
    }

    public void prepareForChallenge(String challengeType) {
        currentDeck = new ArrayList<>();

        // Get appropriate cards for the challenge
        List<Card> matchingCards = allCards.stream()
                .filter(card -> isGoodForChallenge(card, challengeType))
                .collect(Collectors.toList());

        // Get some other cards for variety
        List<Card> otherCards = allCards.stream()
                .filter(card -> !isGoodForChallenge(card, challengeType))
                .collect(Collectors.toList());

        // Ensure a good mix (60% good cards, 40% others)
        int matchingCount = Math.min(matchingCards.size(), 6);
        int otherCount = Math.min(otherCards.size(), 4);

        Collections.shuffle(matchingCards);
        Collections.shuffle(otherCards);

        currentDeck.addAll(matchingCards.subList(0, matchingCount));
        currentDeck.addAll(otherCards.subList(0, otherCount));

        shuffle();
    }

    private boolean isGoodForChallenge(Card card, String challengeType) {
        switch (challengeType) {
            case "proteinas":
                return card.getNutrientValue("proteins") >= 10 && card.getHealthScore() >= 70;
            case "vitaminas":
                return card.getNutrientValue("vitamins") >= 15 && card.getHealthScore() >= 70;
            case "carbohidratos":
                return card.getNutrientValue("carbs") <= 15 && card.getHealthScore() >= 70;
            default:
                return false;
        }
    }

    public void shuffle() {
        Collections.shuffle(currentDeck);
    }

    public Card drawCard() {
        if (currentDeck.isEmpty()) {
            prepareForChallenge("proteinas"); // Default challenge
        }
        return !currentDeck.isEmpty() ? currentDeck.remove(0) : null;
    }

    public List<Card> drawCards(int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count && !currentDeck.isEmpty(); i++) {
            drawnCards.add(drawCard());
        }
        return drawnCards;
    }
}