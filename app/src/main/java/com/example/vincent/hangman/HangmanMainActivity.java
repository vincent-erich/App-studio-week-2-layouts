package com.example.vincent.hangman;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HangmanMainActivity extends AppCompatActivity {

    // Properties of the class...

    // The widgets/views.
    private ImageView hangmanImage;
    private TextView clue;
    private TextView textLettersGuessed;
    private EditText userInput;

    private String[] words;             // String array with all the words for the game.
    private Random randomGenerator;     // A random generator.
    private String randomGeneratedWord; // The word for the game.

    private int nWrongGuesses;                  // The number of wrong guesses.
    private ArrayList<String> lettersGuessed;   // ArrayList with all the guessed letters.
    private boolean lost;
    private boolean won;

    // String array with all the valid input characters (letters).
    private String[] validInput = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
    "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman_main);

        hangmanImage = (ImageView) findViewById(R.id.hangman_image);
        clue = (TextView) findViewById(R.id.clue2);
        textLettersGuessed = (TextView) findViewById(R.id.letters_guessed2);
        userInput = (EditText) findViewById(R.id.user_input);

        words = getResources().getStringArray(R.array.words);
        randomGenerator = new Random();
        randomGeneratedWord = words[randomGenerator.nextInt(words.length)];
        System.out.println("Generated word (onCreate): " + randomGeneratedWord);

        nWrongGuesses = 0;
        lettersGuessed = new ArrayList<String>();
        lost = false;
        won = false;

        showInitialClue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hangman_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Start/create a new game.
        if (id == R.id.action_new_game) {
            createNewGame();
            return true;
        }
        // Go to HangmanHelpActivity.java (shows the help activity).
        else if (id == R.id.action_help) {
            startActivity(new Intent(getApplicationContext(), HangmanHelpActivity.class));
            return true;
        }
        // Exit the application.
        else if (id == R.id.action_exit) {
            DialogFragment myFragment = new ExitDialogFragment();
            myFragment.show(getFragmentManager(), "theDialog");
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInitialClue() {
        // Show the initial clue (i.e., a '?' at the place of each letter in the word).
        String initialClue = "";
        for (int i=0; i<randomGeneratedWord.length(); i++) {
            initialClue = initialClue + "?";
        }
        clue.setText(initialClue);
    }

    private void createNewGame() {
        // Start/create a new game (i.e., set all the properties to their default value).
        randomGeneratedWord = words[randomGenerator.nextInt(words.length)];
        System.out.println("Generated word (createNewGame): " + randomGeneratedWord);
        showInitialClue();
        lettersGuessed = new ArrayList<String>();
        textLettersGuessed.setText("");
        hangmanImage.setImageResource(R.drawable.hangman6);
        nWrongGuesses = 0;
        lost = false;
        won = false;

        Toast.makeText(this, R.string.text_new_game, Toast.LENGTH_SHORT).show();
    }

    public void guessButtonClicked(View view) {
        // Handle a click on the 'GUESS' button.
        String guessedLetter = String.valueOf(userInput.getText()); // The letter guessed by the user.
        String oldTextClue = String.valueOf(clue.getText());        // The clue before the 'GUESS' button was clicked.
        String newTextClue = "";                                    // The clue after the 'GUESS' button is clicked.
        int counter;

        // The 'GUESS' button is clicked, but the user has already lost the game.
        if (lost) {
            Toast.makeText(this, R.string.text_lost, Toast.LENGTH_LONG).show();
        }
        // The 'GUESS' button is clicked, but the user has already won the game.
        else if (won) {
            Toast.makeText(this, R.string.text_won, Toast.LENGTH_LONG).show();
        }
        // The 'GUESS' button is clicked, but the input is empty or consists of more than one letter.
        else if (guessedLetter.length() == 0 || guessedLetter.length() > 1) {
            Toast.makeText(this, R.string.text_invalid_input_1, Toast.LENGTH_SHORT).show();
        }
        // The 'GUESS' button is clicked, but the input is invalid (i.e., a number, a special character, etc.).
        else if (!Arrays.asList(validInput).contains(guessedLetter)) {
            Toast.makeText(this, R.string.text_invalid_input_2, Toast.LENGTH_SHORT).show();
        }
        // The 'GUESS' button is clicked, but the letter has already been guessed.
        else if (lettersGuessed.contains(guessedLetter)) {
            Toast.makeText(this, R.string.text_letter_already_guessed, Toast.LENGTH_SHORT).show();
        }
        else {
            lettersGuessed.add(guessedLetter);
            showLettersGuessed();

            // The word contains the guessed letter.
            if (randomGeneratedWord.contains(guessedLetter)) {
                // Create the new clue.
                for (counter = 0; counter < randomGeneratedWord.length(); counter++) {
                   if (Character.toString(randomGeneratedWord.charAt(counter)).equals(guessedLetter)) {
                       newTextClue = newTextClue + guessedLetter;
                   }
                   else {
                       newTextClue = newTextClue + Character.toString(oldTextClue.charAt(counter));
                   }
                }
                clue.setText(newTextClue);

                // The user has won the game.
                if (!newTextClue.contains("?")) {
                    won = true;
                    Toast.makeText(this, R.string.text_won, Toast.LENGTH_LONG).show();
                }
            }
            // The word does not contain the guessed letter.
            else {
                nWrongGuesses++;

                // Set the right Hangman image.
                switch (nWrongGuesses) {
                    case 1:
                        hangmanImage.setImageResource(R.drawable.hangman5);
                        break;
                    case 2:
                        hangmanImage.setImageResource(R.drawable.hangman4);
                        break;
                    case 3:
                        hangmanImage.setImageResource(R.drawable.hangman3);
                        break;
                    case 4:
                        hangmanImage.setImageResource(R.drawable.hangman2);
                        break;
                    case 5:
                        hangmanImage.setImageResource(R.drawable.hangman1);
                        break;
                    // The user has lost the game.
                    case 6:
                        hangmanImage.setImageResource(R.drawable.hangman0);
                        lost = true;
                        Toast.makeText(this, R.string.text_lost, Toast.LENGTH_LONG).show();
                        clue.setText(randomGeneratedWord);
                        break;
                }
            }
        }
        userInput.setText("");
    }

    private void showLettersGuessed() {
        // Show the letters guessed thus far.
        String text = lettersGuessed.get(0);
        for (int i=1; i<lettersGuessed.size(); i++) {
            text = text + ", " + lettersGuessed.get(i);
        }
        textLettersGuessed.setText(text);
    }

    /*
    Due to time constraints, it was not possible to implement the data retention when the screen
    is rotated.

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
    */

}

