
package com.nutritiongame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardDeck {
    private List<Card> allCards;
    private List<Card> currentDeck;
    private boolean isBotMode;

    public CardDeck(boolean isBotMode) {
        this.isBotMode = isBotMode;
        allCards = new ArrayList<>();
        currentDeck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        initializeProteinCards();
        initializeFruitCards();
        initializeVegetableCards();
        initializeFoodtrashCards();
    }

    private void initializeProteinCards() {
        allCards.add(new Card("Pollo", "/resources/images/cards/Cartas Proteinas/Carta_pollo.png",
                Card.CardType.PROTEIN, 25, 5, 0, 85,
                "El pollo es una excelente fuente de proteína magra."));
        allCards.add(new Card("Huevos", "/resources/images/cards/Cartas Proteinas/Carta_huevo.png",
                Card.CardType.PROTEIN, 13, 8, 1, 80,
                "Los huevos son ricos en proteínas y vitaminas."));
        allCards.add(new Card("Frijoles", "/resources/images/cards/Cartas Proteinas/Carta_frijol.png",
                Card.CardType.PROTEIN, 15, 7, 20, 85,
                "Los frijoles tienen proteína vegetal y fibra para tu salud."));
    }

    private void initializeFruitCards() {
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
    }

    private void initializeVegetableCards() {
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

    private void initializeFoodtrashCards() {
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
    }

    private List<Card> getCardsForChallenge(String challengeType, boolean isGoodCard) {
        return allCards.stream()
                .filter(card -> isGoodForChallenge(card, challengeType) == isGoodCard)
                .collect(Collectors.toList());
    }

    private void addCardsToCurrentDeck(List<Card> cardList, int count) {
        Collections.shuffle(cardList);
        currentDeck.addAll(cardList.subList(0, Math.min(count, cardList.size())));
    }

    private void prepareBotDeck(String challengeType) {
        List<Card> junkFood = getCardsForChallenge(challengeType, false);
        List<Card> otherCards = getCardsForChallenge(challengeType, true);

        addCardsToCurrentDeck(junkFood, 6);
        addCardsToCurrentDeck(otherCards, 4);
    }

    private void preparePlayerDeck(String challengeType) {
        List<Card> challengeCards = getCardsForChallenge(challengeType, true);
        List<Card> otherCards = getCardsForChallenge(challengeType, false);

        if (challengeType.equals("proteinas")) {
            List<Card> proteinCards = allCards.stream()
                    .filter(card -> card.getType() == Card.CardType.PROTEIN)
                    .collect(Collectors.toList());
            addCardsToCurrentDeck(proteinCards, 2);
        }

        addCardsToCurrentDeck(challengeCards, 6);
        addCardsToCurrentDeck(otherCards, 10 - currentDeck.size());
    }

    public void prepareForChallenge(String challengeType) {
        currentDeck = new ArrayList<>();

        if (isBotMode) {
            prepareBotDeck(challengeType);
        } else {
            preparePlayerDeck(challengeType);
        }

        shuffle();
    }

    private boolean isGoodForChallenge(Card card, String challengeType) {
        switch (challengeType) {
            case "proteinas":
                return !isBotMode && card.getType() == Card.CardType.PROTEIN
                        || card.getNutrientValue("proteins") >= 10;
            case "vitaminas":
                return card.getNutrientValue("vitamins") >= 15;
            case "carbohidratos":
                return card.getNutrientValue("carbs") <= 15;
            default:
                return false;
        }
    }

    public void shuffle() {
        Collections.shuffle(currentDeck);
    }

    public Card drawCard() {
        if (currentDeck.isEmpty()) {
            return null;
        }
        return currentDeck.remove(0);
    }

    public List<Card> drawCards(int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count && !currentDeck.isEmpty(); i++) {
            drawnCards.add(drawCard());
        }
        return drawnCards;
    }
}