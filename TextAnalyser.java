import java.io.*;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TextAnalyser extends Application{
	 
	//Sort hashmap values
	public HashMap<String, Integer> sortByValue(HashMap<String, Integer> counts){
     
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(counts.entrySet());
 
        Collections.sort(list, (i1, i2) -> i2.getValue().compareTo(i1.getValue()));
 
        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
        	temp.put(aa.getKey(), aa.getValue());
        	}
        return temp;
        }
	
	//JavaFX
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		TextField tb = new TextField();
		
		 Label lbl = new Label();
		 lbl.setLayoutX(300);
		 lbl.setLayoutY(100);
		 lbl.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.ITALIC, 24));
		 
		 Button btnStart = new Button("Start");
         btnStart.setLayoutX(150);
         btnStart.setOnAction((ActionEvent e) -> {
             try {
                 textAnalyser();
                 } catch (IOException e1) {
                	 e1.printStackTrace();
                	 }
             });
         VBox RBpane = new VBox();
         RBpane.setPadding(new Insets(50,10,10,90));
         RBpane.setSpacing(5);
         RBpane.getChildren().addAll(btnStart);
         
         VBox vbox = new VBox();
         vbox.setMaxWidth(500);
         vbox.setSpacing(5);
         vbox.setPadding(new Insets(10, 10,10, 10));
         
         vbox.getChildren().addAll(lbl, RBpane);
         
         ((Group) scene.getRoot()).getChildren().addAll(vbox);
		 
		 stage.setScene(scene);
		 stage.setTitle("Word Occurrences");
		 stage.setWidth(250);
		 stage.setHeight(250);
		 stage.show();
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		 //launch(args); 
		 
		 int choice;
			//Word Storing Section.
			//A do while loop to take multiple words from the user.
			do{
			//A scanner object to take input word from user.
			Scanner in = new Scanner(System.in);
			//Try catch to handle checked exception like sqlexception and classnotfound exception.
			try
			{
			//Asking user to enter a word.
			System.out.println("Enter a word:");
			String word = in.next();
			//Loading the jdbc driver.
			Class.forName("com.mysql.jdbc.Driver");
			//Establishing Connection.
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wordoccurrences","root","sqlrootak");
			//Creating statement.
			PreparedStatement ps = con.prepareStatement("insert into word (words) values(?)");
			//Setting the word entered by the user in the query.
			ps.setString(1,word);
			//Executing the query.
			ps.executeUpdate();
			//Showing the database table word after addition of the user word.
			System.out.println("Database after addition of your word");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from word");
			while(rs.next()){
			System.out.println(rs.getString(1));
			}

			con.close();
			}
			catch(ClassNotFoundException |SQLException c){
			System.out.println(c.getMessage());
			}
			System.out.println("Do you want to enter more word(1= yes/0= no)");
			choice = in.nextInt();
			}while(choice==1);
	}
	
	
	   
	
	public int textAnalyser() throws IOException {
        //file reader
        File file = new File("src/1065-0.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int wordCount = 1;
        
        HashMap<String, Integer> counts = new HashMap<>();
        
        while ((line = br.readLine()) != null) { 
            String[] words = line.split("[\\s.;,?:!()\"]+");
            for (String word : words) {
                word = word.trim();
                if (word.length() > 0) {
                    if (counts.containsKey(word)) {
                        counts.put(word, counts.get(word) + 1);
                    } else {
                        counts.put(word, (int) 1L);
                    }
                }
            }
        }
        
        
        //sort and print text
        Map<String, Integer> sorted = sortByValue(counts);
        
        for (Entry<String, Integer> entry : sorted.entrySet()) {
        	if(wordCount <=20) {
            System.out.println(wordCount + ". " + entry.getKey() + " : " + entry.getValue());
            wordCount++;
            } else {
            	break;
            }
        } 
        
        br.close();
		return wordCount;
    }
}
