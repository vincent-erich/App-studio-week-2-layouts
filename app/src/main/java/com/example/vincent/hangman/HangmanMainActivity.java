package com.example.vincent.hangman;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HangmanMainActivity extends AppCompatActivity {

    private ImageView hangmanImage;
    private TextView clue;
    private TextView textLettersGuessed;
    private EditText userInput;

    private String[] words;
    private Random randomGenerator;
    private String randomGeneratedWord;

    private int nWrongGuesses;
    private ArrayList<String> lettersGuessed;
    private boolean lost;
    private boolean won;

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
        System.out.println("Generated word: " + randomGeneratedWord);

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

        if (id == R.id.action_new_game) {
            createNewGame();
            return true;
        }
        else if (id == R.id.action_help) {
            startActivity(new Intent(getApplicationContext(), HangmanHelpActivity.class));
            return true;
        }
        else if (id == R.id.action_exit) {
            //DialogFragment myFragment = new ExitDialogFragment();
            //myFragment.show(getFragmentManager(), "theDialog");
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewGame() {
        randomGeneratedWord = words[randomGenerator.nextInt(words.length)];
        System.out.println("Generated word: " + randomGeneratedWord);
        showInitialClue();
        lettersGuessed = new ArrayList<String>();
        textLettersGuessed.setText("");
        hangmanImage.setImageResource(R.drawable.hangman6);
        nWrongGuesses = 0;
        lost = false;
        won = false;

        Toast.makeText(this, R.string.text_new_game, Toast.LENGTH_SHORT).show();
    }

    private void showInitialClue() {
        String initialClue = "";
        for (int i=0; i<randomGeneratedWord.length(); i++) {
            initialClue = initialClue + "?";
        }
        clue.setText(initialClue);
    }

    public void guessButtonClicked(View view) {

        String guessedLetter = String.valueOf(userInput.getText());
        String oldTextClue = String.valueOf(clue.getText());
        String newTextClue = "";
        int counter;

        if (lost) {
            Toast.makeText(this, R.string.text_lost, Toast.LENGTH_LONG).show();
        }
        else if (won) {
            Toast.makeText(this, R.string.text_won, Toast.LENGTH_LONG).show();
        }
        else if (guessedLetter.length() == 0 || guessedLetter.length() > 1) {
            Toast.makeText(this, R.string.text_invalid_input_1, Toast.LENGTH_SHORT).show();
        }
        else if (!Arrays.asList(validInput).contains(guessedLetter)) {
            Toast.makeText(this, R.string.text_invalid_input_2, Toast.LENGTH_SHORT).show();
        }
        else if (lettersGuessed.contains(guessedLetter)) {
            Toast.makeText(this, R.string.text_letter_already_guessed, Toast.LENGTH_SHORT).show();
        }
        else {
            lettersGuessed.add(guessedLetter);
            showLettersGuessed();

            if (randomGeneratedWord.contains(guessedLetter)) {
                for (counter = 0; counter < randomGeneratedWord.length(); counter++) {
                   if (Character.toString(randomGeneratedWord.charAt(counter)).equals(guessedLetter)) {
                       newTextClue = newTextClue + guessedLetter;
                   }
                   else {
                       newTextClue = newTextClue + Character.toString(oldTextClue.charAt(counter));
                   }
                }
                clue.setText(newTextClue);

                if (!newTextClue.contains("?")) {
                    won = true;
                    Toast.makeText(this, R.string.text_won, Toast.LENGTH_LONG).show();
                }
            }
            else {
                nWrongGuesses++;

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
        String text = lettersGuessed.get(0);
        for (int i=1; i<lettersGuessed.size(); i++) {
            text = text + " " + lettersGuessed.get(i);
        }
        textLettersGuessed.setText(text);
    }

}

