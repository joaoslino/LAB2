package org.example;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Arena {
    private final Hero hero;

    private final int width;
    private final int height;

    private final List<Walls> walls;
    private final List<com.bmcl.hero.Coin> coins;
    private final List<Monster> monsters;


    public Arena(int width, int height) {
        this.width = width;
        this.height = height;

        hero = new Hero(width / 2, height / 2);
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
    }

    private List<Walls> createWalls() {
        List<Walls> walls = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            walls.add(new Walls(c, 0));
            walls.add(new Walls(c, height - 1));
        }

        for (int r = 1; r < height - 1; r++) {
            walls.add(new Walls(0, r));
            walls.add(new Walls(width - 1, r));
        }

        return walls;
    }
    private List<com.bmcl.hero.Coin> createCoins() {
        Random random = new Random();
        ArrayList<com.bmcl.hero.Coin> coins = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            coins.add(new com.bmcl.hero.Coin(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        return coins;
    }
    private List<Monster> createMonsters() {
        Random random = new Random();
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            monsters.add(new Monster(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1));
        return monsters;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

        hero.draw(graphics);

        for (Walls wall : walls) wall.draw(graphics);
        for (com.bmcl.hero.Coin coin : coins) coin.draw(graphics);
        for (Monster monster : monsters) monster.draw(graphics);

    }

    public void processKey(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp) moveHero(hero.moveUp());
        if (key.getKeyType() == KeyType.ArrowRight) moveHero(hero.moveRight());
        if (key.getKeyType() == KeyType.ArrowDown) moveHero(hero.moveDown());
        if (key.getKeyType() == KeyType.ArrowLeft) moveHero(hero.moveLeft());

        retrieveCoins();
        verifyMonsterCollisions();
    }

    public void verifyMonsterCollisions() {
        for (Monster monster : monsters)
            if (hero.getPosition().equals(monster.getPosition())) {
                System.out.println("You died!");
                System.exit(0);
            }
    }

    public void moveMonsters() {
        for (Monster monster : monsters) {
            Position monsterPosition = monster.move();
            if (canHeroMove(monsterPosition))
                monster.setPosition(monsterPosition);
        }
    }

    public void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
            moveMonsters();
        }
    }
    private void retrieveCoins() {
        for (com.bmcl.hero.Coin coin : coins)
            if (hero.getPosition().equals(coin.getPosition())) {
                coins.remove(coin);
                break;
            }
    }

    private boolean canHeroMove(Position position) {
        if (position.getX() < 0) return false;
        if (position.getX() > width - 1) return false;
        if (position.getY() < 0) return false;
        if (position.getY() > height - 1) return false;

        for (Walls wall : walls)
            if (wall.getPosition().equals(position)) return false;
        return true;
    }


}
