/* Description:the program is foe a item shop based out of dungon and dragons 5e. it will make a shop and aloock the user to buy/ bell itsm. the buy page will allow the user to see 3 random items with al lthe stats for the items as wel as price. the sell page is made so that the user can work witht he shops funds and allow the shop to buy form the users their items and give ethe total of the shops funds after the transaction. the main page is for the usere to choose which fuction they want to go to first. 
   Author: Brianna Manning
   Date: 10.11.24
   Program Name: MainPage.java
*/

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DnDItemApp extends Application {

    private List<String> itemTypes = Arrays.asList("Armor", "Magic Item", "Potion", "Weapon");
    private List<String> itemRarities = Arrays.asList("Common", "Uncommon", "Rare", "Legendary");

    private VBox buyPage;
    private VBox sellPage;

    @Override
    public void start(Stage primaryStage) { // main page that opens at start of application
        primaryStage.setTitle("D&D Item Shop");

        buyPage = createBuyPage();
        sellPage = createSellPage();

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Button buyButton = new Button("Buy Page");
        buyButton.setOnAction(e -> primaryStage.setScene(new Scene(buyPage, 600, 400));

        Button sellButton = new Button("Sell Page");
        sellButton.setOnAction(e -> primaryStage.setScene(new Scene(sellPage, 600, 400));

        root.getChildren().addAll(buyButton, sellButton);

        Scene scene = new Scene(root, 400, 300);

        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());  // Load the external CSS file

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createBuyPage() { // buy page the shop will show 3 items at random from the item inventory sheet 
        VBox buyPage = new VBox(10);
        buyPage.setPadding(new Insets(10));

        HBox itemsContainer = new HBox(10);

        Button randomizeButton = new Button("Randomize Items");
        randomizeButton.setOnAction(e -> {
            itemsContainer.getChildren().clear();

            try {
                JSONParser parser = new JSONParser();
                JSONArray items = (JSONArray) parser.parse(new FileReader("items.json"));

                for (int i = 0; i < 3; i++) {
                    JSONObject jsonItem = (JSONObject) items.get(i);

                    String itemType = (String) jsonItem.get("type");
                    String itemName = (String) jsonItem.get("name");
                    String itemDescription = (String) jsonItem.get("description");
                    String itemPrice = (String) jsonItem.get("price");
                    String itemRarity = (String) jsonItem.get("rarity");
                    String itemStats = (String) jsonItem.get("stats");

                    VBox itemBox = new VBox(5);
                    itemBox.setStyle("-fx-border-color: black; -fx-padding: 10px;");
                    itemBox.getChildren().addAll(
                            new Label("Type: " + itemType),
                            new Label("Name: " + itemName),
                            new Label("Description: " + itemDescription),
                            new Label("Price: " + itemPrice),
                            new Label("Rarity: " + itemRarity),
                            new Label("Stats: " + itemStats)
                    );

                    itemsContainer.getChildren().add(itemBox);
                }
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }
        });

        buyPage.getChildren().addAll(randomizeButton, itemsContainer);

        return buyPage;
    }

    private VBox createSellPage() { //creates a sell page for the user to see the shop's funds and allow the user to sell the items to the shop and do a simple =/- transaction to find the shop total funds after the transaction is complete.
        double shopFunds = 1000.0;

        VBox sellPage = new VBox(10);
        sellPage.setPadding(new Insets(10));

        Label fundsLabel = new Label("Shop's Current Funds: $" + shopFunds);

        TextField purchaseAmountField = new TextField();
        purchaseAmountField.setPromptText("Enter Purchase Amount");

        Button sellButton = new Button("Buy Item");
        sellButton.setOnAction(e -> {
            String purchaseAmountText = purchaseAmountField.getText();

            if (purchaseAmountText.isEmpty()) {
                System.out.println("Please enter the purchase amount.");
                return;
            }

            try {
                double purchaseAmount = Double.parseDouble(purchaseAmountText);

                if (purchaseAmount > shopFunds) {
                    System.out.println("Insufficient funds. Shop cannot make the purchase.");
                } else {
                    shopFunds -= purchaseAmount;
                    fundsLabel.setText("Shop's Current Funds: $" + shopFunds);
                    System.out.println("Item purchased successfully. Shop's funds updated.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid purchase amount.");
            }
        });

        sellPage.getChildren().addAll(fundsLabel, purchaseAmountField, sellButton);

        return sellPage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
