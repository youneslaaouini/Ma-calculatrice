package com.example.calculatrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.calculatrice.ui.CalculatorFragment;
import com.example.calculatrice.ui.CurrencyFragment;
import com.example.calculatrice.ui.DateFragment;
import com.example.calculatrice.ui.LengthFragment;
import com.example.calculatrice.ui.TemperatureFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Fragment currentFragment, lengthFragment, calculatorFragment, temperatureFragment, currencyFragment, dateFragment;
    int selectedItemIndex = 0;

    public void burgerClickHandler(View view){
        if(drawerLayout.isOpen()){
            drawerLayout.close();
        }else{
            drawerLayout.open();
        }
    }

    public void setToolbarTitle(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView textView = (TextView) toolbar.findViewById(R.id.fragment_title);

        textView.setText(title);
    }

    public void replaceFragment(Fragment fragment){
         getSupportFragmentManager().beginTransaction()
                 .replace(R.id.main_container, fragment)
                 .commit();
    }

    public void setSelectedItem(int index){
        if(index != selectedItemIndex){
            navigationView.getMenu().getItem(selectedItemIndex).setChecked(false);
            navigationView.getMenu().getItem(index).setChecked(true);

            selectedItemIndex = index;
        }
    }

    public boolean onSelectItem(@NonNull MenuItem item){
        int newSelectedItemIndex = selectedItemIndex;

        CharSequence titleCharSeq = item.getTitle();

        if(titleCharSeq != null){
            String title = titleCharSeq.toString();

            Log.i("INFO", title);

            if(currentFragment == null){
                currentFragment = calculatorFragment;
            }

            if(getString(R.string.calculator_title).equals(title)){
                newSelectedItemIndex = 0;

                currentFragment = calculatorFragment;

            }else if(getString(R.string.currency_converter_title).equals(title)){
                newSelectedItemIndex = 1;

                currentFragment = currencyFragment;

            }else if(getString(R.string.length_converter_title).equals(title)){
                newSelectedItemIndex = 2;

                currentFragment = lengthFragment;

            }else if(getString(R.string.temperature_converter_title).equals(title)){
                newSelectedItemIndex = 3;

                currentFragment = temperatureFragment;

            }else if(getString(R.string.date_converter_title).equals(title)){
                newSelectedItemIndex = 4;

                currentFragment = dateFragment;
            }

            setSelectedItem(newSelectedItemIndex);

            setToolbarTitle(title);

            replaceFragment(currentFragment);

            drawerLayout.close();
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);

        navigationView = findViewById(R.id.nav_view);

        calculatorFragment = new CalculatorFragment();

        currencyFragment = new CurrencyFragment();

        lengthFragment = new LengthFragment();

        temperatureFragment = new TemperatureFragment();

        dateFragment = new DateFragment();

        navigationView.setNavigationItemSelectedListener(item -> onSelectItem(item));

        replaceFragment(calculatorFragment);
    }
}