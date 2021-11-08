package com.src;
import java.util.Scanner;

public class Ruleta{
    public static int money = 200;
    public static int aiCount = 0;
    public static int playerCount = 1;
    public static Scanner scan = new Scanner(System.in);

    public static int blacks[] = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
    public static int reds[] = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};

    public static int playerColor[] = new int[playerCount];
    public static int playerBets[] = new int[playerCount];
    public static int playerMoney[] = new int[playerCount];
    public static int playerBet = 0;

    public static int aiColor[] = new int[aiCount];
    public static int aiBets[] = new int[aiCount];
    public static int aiMoney[] = new int[playerCount];

    public static String currentRound = "";
    public static int roulette = 0;
    public static int color = 0;

    public static void main(String[] args) {
        menu();
    }

    public static void menu(){
        System.out.println("1 - Začít hrát.");
        System.out.println("2 - Změnit peníze hráčů (hráči i AI) (momentálně " + money + ").");
        System.out.println("3 - Změnit počet hráčů (momentálně " + playerCount + ").");
        System.out.println("4 - Změnit počet AI (momentálně " + aiCount + ").");
        System.out.println("0 - Ukončit program.");

        int choice = scan.nextInt();

        switch (choice){
            case 1 -> {setupGame();}
            case 2 -> {
                System.out.println("Zadej nový počet začátečních peněz:");
                money = scan.nextInt();
                menu();
            }
            case 3 -> {
                System.out.println("Zadej nový počet hráčů:");
                playerCount = scan.nextInt();
                menu();
            }
            case 4 -> {
                System.out.println("Zadej nový počet AI hráčů:");
                aiCount = scan.nextInt();
                menu();
            }
            case 0 -> {System.exit(0);}
            default -> {
                menu();
            }
        }
    }

    public static void setupGame(){
        playerColor = new int[playerCount];
        playerBets = new int[playerCount];
        playerMoney = new int[playerCount];

        aiColor = new int[aiCount];
        aiBets = new int[aiCount];
        aiMoney = new int[aiCount];

        for (int i = 0; i < playerCount; i++) {
            playerMoney[i] = money;
        }

        for (int i = 0; i < aiCount; i++) {
            aiMoney[i] = money;
        }

        game();
    }

    public static void game(){
        roulette = drawNumber();
        color = numberToColor(roulette);

        currentRound = "Ruleta vytočila " + roulette + ", to je " + colorTranslator(color) + ".\n";

        for (int i = 0; i < playerCount; i++){
            if(playerMoney[i] <= 0){
                currentRound += "Hráč " + (i+1) + " je bez peněz a odešel než ho začne nahánět ruská mafie.\n";
                continue;
            }

            if(playerColor[i] != -1){
                playerChoice(i);
            }

            if(playerColor[i] != -1){
                playerBet(i);
            }

            else{
                currentRound += "Hráč " + (i+1) + " (ne)šťastně odešel s " + playerMoney[i] + " penězi. \n";
                continue;
            }

            if(playerColor[i] == color){
                playerMoney[i] = moneyAfterBet(playerMoney[i], color, playerBets[i]);
                currentRound += "Hráč " + (i+1) + " vsadil " + playerBets[i] + " na " + colorTranslator(playerColor[i]) + ". Vyhrál a nyní je jeho zůstatek: " + playerMoney[i] + ".\n";
            }

            else{
                playerMoney[i] -= playerBets[i];
                currentRound += "Hráč " + (i+1) + " vsadil " + playerBets[i] + " na " + colorTranslator(playerColor[i]) + ". Prohrál a nyní je jeho zůstatek: " + playerMoney[i] + ".\n";
            }
        }

        for (int i = 0; i < aiCount; i++) {
            if(aiMoney[i] <= 0){
                currentRound += "AI " + (i+1) + " už nemá peníze, už nehraje.\n";
                continue;
            }

            aiChoice(i);
            aiBet(i);

            if(aiColor[i] == color){
                aiMoney[i] = moneyAfterBet(aiMoney[i], color, aiBets[i]);
                currentRound += "AI " + (i+1) + " vsadilo " + aiBets[i] + " na " + colorTranslator(aiColor[i]) + ". Vyhrál a nyní je jeho zůstatek: " + aiMoney[i] + ".\n";
            }

            else{
                aiMoney[i] -= aiBets[i];
                currentRound += "AI " + (i+1) + " vsadilo " + aiBets[i] + " na " + colorTranslator(aiColor[i]) + ". Prohrál a nyní je jeho zůstatek: " + aiMoney[i] + ".\n";
            }
        }

        System.out.println(currentRound);
        nextRound();
    }

    public static int drawNumber(){
        return (int) (Math.random()*37);
    }

    public static int numberToColor(int roulette){
        for (int i = 0; i < blacks.length; i++) {
            if(roulette == blacks[i]){
                return 1;
            }
        }

        for (int i = 0; i < reds.length; i++) {
            if(roulette == reds[i]){
                return 2;
            }
        }

        if(roulette == 0){
            return 0;
        }

        return -1;
    }

    public static void playerChoice(int i){
        System.out.println("Hráči " + (i+1) + " prosím zadej na jakou barvu chceš hrát.\n 0 - Zelená \n 1 - Černá \n 2 - Červená \n 3 - Nechci hrát");
        switch(scan.nextInt()){
            case 1 -> {playerColor[i] = 1;}
            case 2 -> {playerColor[i] = 2;}
            case 0 -> {playerColor[i] = 0;}
            case 3 -> {playerColor[i] = -1;}
            default -> {
                System.out.println("Prosím napiš červená, černá, zelená nebo nechci.");
                playerChoice(i);
            }
        }
    }

    public static void playerBet(int i){
        System.out.println("Hráči " + (i+1) + " prosím kolik chceš vsadit. (1 - " + playerMoney[i] + ")");
        playerBet = scan.nextInt();

        if(playerBet<1){
            System.out.println("Prosím vsaď více jak 1.");
            playerBet(i);
        }

        if(playerBet>playerMoney[i]){
            System.out.println("Tolik nemáš!");
            playerBet(i);
        }

        playerBets[i] = playerBet;
    }

    public static void aiChoice(int i){
        aiColor[i] = numberToColor(drawNumber());
    }

    public static void aiBet(int i){
        aiBets[i] = (int) (Math.random() * aiMoney[i])+1;
    }

    public static String colorTranslator(int color){
        switch(color){
            case 0 -> {return "zelená";}
            case 1 -> {return "černá";}
            case 2 -> {return "červená";}
            default -> {return "chyba";}
        }
    }

    public static int moneyAfterBet(int money, int color, int bet){
        switch(color){
            case 0 -> {money += (bet*35);}
            case 1 -> {money += bet;}
            case 2 -> {money += bet;}
        }

        return money;
    }

    public static void nextRound(){
        System.out.println("Chceš vygenerovat další kolo? \n 1 - Ano \n 0 - Ne");
        switch(scan.nextInt()){
            case 1 -> game();
            case 0 -> menu();
            default -> nextRound();
        }
    }
}
