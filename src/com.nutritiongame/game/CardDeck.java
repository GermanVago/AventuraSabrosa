package com.nutritiongame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
    private List<Card> cards;

    public CardDeck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        // Foodtrash Cards
        cards.add(new Card("Chocolate", "/resources/images/cards/Cartas Foodtrash/Comida_chocolate.png",
                Card.CardType.FOODTRASH, 1, 0, 20));
        cards.add(new Card("Hamburguesa", "/resources/images/cards/Cartas Foodtrash/Foodtrash_Hamburguesa.png",
                Card.CardType.FOODTRASH, 5, 1, 15));
        cards.add(new Card("Hot Dog", "/resources/images/cards/Cartas Foodtrash/Foodtrash_HotDog.png",
                Card.CardType.FOODTRASH, 4, 0, 18));
        cards.add(new Card("Pizza", "/resources/images/cards/Cartas Foodtrash/Foodtrash_Pizza.png",
                Card.CardType.FOODTRASH, 6, 2, 25));
        cards.add(new Card("Soda", "/resources/images/cards/Cartas Foodtrash/Foodtrash_Soda.png",
                Card.CardType.FOODTRASH, 0, 0, 30));

        // Fruit Cards
        cards.add(new Card("Sandía", "/resources/images/cards/Cartas Frutas/Carta_fruta_Sandia.png",
                Card.CardType.FRUIT, 2, 8, 10));
        cards.add(new Card("Mango", "/resources/images/cards/Cartas Frutas/Carta_Mango.png",
                Card.CardType.FRUIT, 1, 12, 15));
        cards.add(new Card("Manzana", "/resources/images/cards/Cartas Frutas/Carta_manzana.png",
                Card.CardType.FRUIT, 0, 10, 12));
        cards.add(new Card("Naranja", "/resources/images/cards/Cartas Frutas/Carta_Naranja.png",
                Card.CardType.FRUIT, 1, 15, 8));
        cards.add(new Card("Plátano", "/resources/images/cards/Cartas Frutas/carta_platano.png",
                Card.CardType.FRUIT, 2, 8, 20));
        cards.add(new Card("Fresa", "/resources/images/cards/Cartas Frutas/fruta_fresa.png",
                Card.CardType.FRUIT, 1, 9, 5));

        // Vegetable Cards
        cards.add(new Card("Cebolla", "/resources/images/cards/Cartas Verduras/Carta_Cebolla.png",
                Card.CardType.VEGETABLE, 1, 7, 5));
        cards.add(new Card("Lechuga", "/resources/images/cards/Cartas Verduras/Carta_lechuga.png",
                Card.CardType.VEGETABLE, 1, 8, 2));
        cards.add(new Card("Pepino", "/resources/images/cards/Cartas Verduras/Carta_Pepino.png",
                Card.CardType.VEGETABLE, 1, 6, 3));
        cards.add(new Card("Tomate", "/resources/images/cards/Cartas Verduras/Carta_Tomate.png",
                Card.CardType.VEGETABLE, 1, 9, 4));
        cards.add(new Card("Zanahoria", "/resources/images/cards/Cartas Verduras/Carta_Zanahoria.png",
                Card.CardType.VEGETABLE, 1, 12, 6));
        cards.add(new Card("Brócoli", "/resources/images/cards/Cartas Verduras/Cartas_Brocoli.png",
                Card.CardType.VEGETABLE, 4, 10, 5));
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public List<Card> drawCards(int count) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < count && !cards.isEmpty(); i++) {
            drawnCards.add(drawCard());
        }
        return drawnCards;
    }
}