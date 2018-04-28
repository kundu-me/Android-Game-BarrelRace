package com.utdallas.nxkundu.barrelracegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This class provides the Help
 * How to Play the Game
 * Admin Contact
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

public class HelpActivity extends AppCompatActivity {

    /**************************************************************************
     * Method
     * Overriding onCreate method
     *
     **************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**************************************************************************
     * Method
     *
     **************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    /**************************************************************************
     * Method
     *
     **************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    /**************************************************************************
     * Method
     * Overriding onSupportNavigateUp()
     * To make the appbar back button function
     **************************************************************************/
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**************************************************************************
     * Method
     * Overriding onBackPressed()
     * To make the back button custom function
     *
     **************************************************************************/
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

