package com.mihai.colorcodecreator;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements FabDialogFragment.FabDialogListener {

    private SeekBar alpha;
    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;
    private TextView target;
    private TextView cod;
    private Toolbar toolbar;
    public static final String FISIER_CULORI = "culori";
    public static final String FAB_CULOARE = "culoare";
    public static final String FAB_COD = "cod";
    private static final String SAVED_COLOR = "culoareSalvata";
    private static final String SAVED_CODE = "codSalvat";
    private ArrayList<CuloareSalvata> culoriSalvate;
    private ArrayList<CuloareSalvata> culoriSalvateNoi;

    private class SnackUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
                culoriSalvateNoi.remove(culoriSalvateNoi.size() - 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alpha = (SeekBar) findViewById(R.id.aBar);
        red = (SeekBar) findViewById(R.id.rBar);
        green = (SeekBar) findViewById(R.id.gBar);
        blue = (SeekBar) findViewById(R.id.bBar);
        target = (TextView) findViewById(R.id.target);
        cod = (TextView) findViewById(R.id.cod);
        TextView alphaText = (TextView) findViewById(R.id.aText);
        toolbar = (Toolbar) findViewById(R.id.slidingActionBar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        SeekBar.OnSeekBarChangeListener l = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int a = alpha.getProgress();
                    int r = red.getProgress();
                    int g = green.getProgress();
                    int b = blue.getProgress();
                    target.setBackgroundColor(Color.argb(a, r, g, b));
                    String text = "#";
                    if (a == 0) {
                        text += "00";
                        if (r == 0) {
                            text += "00";
                            if (g == 0) {
                                text += "00";
                                if (b == 0)
                                    text += "0";
                            }
                        }
                    }
                    text += Integer.toHexString(Color.argb(a, r, g, b));
                    if (a > 0 && a < 16) {
                        text = "#0" + text.substring(1);
                    }
                    cod.setText(text.toCharArray(), 0, text.length());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        alpha.setOnSeekBarChangeListener(l);
        red.setOnSeekBarChangeListener(l);
        green.setOnSeekBarChangeListener(l);
        blue.setOnSeekBarChangeListener(l);
        alphaText.setOnLongClickListener(v -> {
            CharSequence text = "Alfa reprezinta transparenta.";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return true;
        });
        fab.setOnClickListener(v -> {
            DialogFragment fabDialog = new FabDialogFragment();
            Bundle pentruDialog=new Bundle();
            int a = alpha.getProgress();
            int r = red.getProgress();
            int g = green.getProgress();
            int b = blue.getProgress();
            pentruDialog.putInt(FAB_CULOARE, Color.argb(a, r, g, b));
            pentruDialog.putString(FAB_COD, cod.getText().toString());
            fabDialog.setArguments(pentruDialog);
            fabDialog.show(getFragmentManager(), "Adauga");
        });

        setActionBar(toolbar);
        culoriSalvate = new ArrayList<>();
        culoriSalvateNoi = new ArrayList<>();
        Scanner sc = null;
        try
        {
            FileInputStream f=openFileInput(FISIER_CULORI);
            sc=new Scanner(f);
            while(sc.hasNext())
            {
                int culoare = Integer.parseInt(sc.next());
                String titlu = sc.next();
                titlu = titlu.replace('_', ' ');
                culoriSalvate.add(new CuloareSalvata(culoare, titlu));
            }
            sc.close();
        }
        catch(FileNotFoundException e)
        {
            try
            {
                FileOutputStream fos = openFileOutput(FISIER_CULORI, Context.MODE_PRIVATE);
                fos.close();
            }
            catch(Exception ee)
            {
                CharSequence exceptie2 = "Eroare la crearea fisierului.";
                Toast.makeText(getApplicationContext(), exceptie2, Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            CharSequence exceptie = "Exceptie la citirea din fisier.";
            Toast.makeText(getApplicationContext(), exceptie, Toast.LENGTH_LONG).show();
        }
        finally
        {
            if(sc != null)
                sc.close();
        }
        //culoriSalvate.sort(new ComparatorCuloare());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, m);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(SAVED_CODE, cod.getText());
        int a = alpha.getProgress();
        int r = red.getProgress();
        int g = green.getProgress();
        int b = blue.getProgress();
        int culoare = Color.argb(a, r, g, b);
        outState.putInt(SAVED_COLOR, culoare);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        CharSequence text = savedInstanceState.getCharSequence(SAVED_CODE);
        cod.setText(text);
        int culoare = savedInstanceState.getInt(SAVED_COLOR);
        alpha.setProgress(Color.alpha(culoare));
        red.setProgress(Color.red(culoare));
        green.setProgress(Color.green(culoare));
        blue.setProgress(Color.blue(culoare));
        target.setBackgroundColor(culoare);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        FileOutputStream f;
        DataOutputStream dos = null;
        try {
            f = openFileOutput(FISIER_CULORI, Context.MODE_APPEND);
            dos = new DataOutputStream(f);
            for(CuloareSalvata c: culoriSalvate)
            {
                Integer culoare = c.getCod();
                String out = culoare.toString() + " " + c.getNume() + " ";
                dos.writeBytes(out);
            }
            for(CuloareSalvata c: culoriSalvateNoi)
            {
                Integer culoare = c.getCod();
                String out = culoare.toString() + " " + c.getNume() + " ";
                dos.writeBytes(out);
            }
            dos.close();
        }
        catch (IOException e) {
            //Nu vreau sa faca nimic
        }
        finally
        {
            if(dos != null)
                try
                {
                    dos.close();
                }
                catch (IOException e)
                {
                    //Nu e nevoie sa faca nimic
                }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String input) {
        int culoare = dialog.getArguments().getInt(FAB_CULOARE);
        if(input.compareTo("") == 0 || input.length() > 50)
        {
            CharSequence temp = "Nume gol sau prea mare (>50 carctere)";
            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
            return;
        }
        culoriSalvateNoi.add(new CuloareSalvata(culoare, input));
        String pentruSnack = "Culoare adaugata";
        CoordinatorLayout coordLayout = (CoordinatorLayout) findViewById(R.id.coordLayout);
        Snackbar snack = Snackbar.make(coordLayout, pentruSnack, Snackbar.LENGTH_LONG);
        snack.setAction(R.string.undo, new SnackUndoListener());
        snack.show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    public void showPopup(MenuItem v)
    {
        PopupMenu popup = new PopupMenu(this, toolbar, Gravity.END);
        popup.setOnMenuItemClickListener(item -> {
            switch(item.getItemId())
            {
                case R.id.cMenuNegru:
                {
                    seteazaCuloare(Color.BLACK, "Color.BLACK");
                    return true;
                }
                case R.id.cMenuGriI:
                {
                    seteazaCuloare(Color.DKGRAY, "Color.DKGRAY");
                    return true;
                }
                case R.id.cMenuGri:
                {
                    seteazaCuloare(Color.GRAY, "Color.GRAY");
                    return true;
                }
                case R.id.cMenuGriD:
                {
                    seteazaCuloare(Color.LTGRAY, "Color.LTGRAY");
                    return true;
                }
                case R.id.cMenuAlb:
                {
                    seteazaCuloare(Color.WHITE, "Color.White");
                    return true;
                }
                case R.id.cMenuRosu:
                {
                    seteazaCuloare(Color.RED, "Color.RED");
                    return true;
                }
                case R.id.cMenuGalben:
                {
                    seteazaCuloare(Color.YELLOW, "Color.YELLOW");
                    return true;
                }
                case R.id.cMenuVerde:
                {
                    seteazaCuloare(Color.GREEN, "Color.GREEN");
                    return true;
                }
                case R.id.cMenuTurcoaz:
                {
                    seteazaCuloare(Color.CYAN, "Color.CYAN");
                    return true;
                }
                case R.id.cMenuAlbastru:
                {
                    seteazaCuloare(Color.BLUE, "Color.BLUE");
                    return true;
                }
                case R.id.cMenuPurpuriuI:
                {
                    seteazaCuloare(Color.MAGENTA, "Color.MAGENTA");
                    return true;
                }
                default:
                {
                    seteazaCuloare(item.getItemId(), item.getTitle().toString());
                    return true;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        for(CuloareSalvata c: culoriSalvate)
            popup.getMenu().add(Menu.NONE, c.getCod(), Menu.NONE, c.getNume());
        for(CuloareSalvata c: culoriSalvateNoi)
            popup.getMenu().add(Menu.NONE, c.getCod(), Menu.NONE, c.getNume());
        popup.show();
    }
    
    public void stergere(MenuItem m)
    {
        FileOutputStream f = null;
        try {
            culoriSalvate.clear();
            culoriSalvateNoi.clear();
            f = openFileOutput(FISIER_CULORI, Context.MODE_PRIVATE);
            f.close();
        }
        catch (FileNotFoundException e) {
            String exceptie = "Fisierul nu a fost gasit.";
            Toast.makeText(getApplicationContext(), exceptie, Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            String exceptie = "Exceptie la inchiderea fisierului";
            Toast.makeText(getApplicationContext(), exceptie, Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            String exceptie = "Eroare la stergerea continutului";
            Toast.makeText(getApplicationContext(), exceptie, Toast.LENGTH_LONG).show();
        }
        finally
        {
            if(f != null)
                try {
                    f.close();
                } catch (IOException e) {
                    String exceptie = "Exceptie la inchiderea fisierului";
                    Toast.makeText(getApplicationContext(), exceptie, Toast.LENGTH_LONG).show();
                }
        }

    }

    public void toSliderActivity(MenuItem m)
    {
        Intent intentie = new Intent(this, SlidingActivity.class);
        intentie.putExtra(SlidingActivity.COD_CULORI, culoriSalvate);
        intentie.putExtra(SlidingActivity.COD_CULORI_NOI, culoriSalvateNoi);
        startActivity(intentie);
    }

    public void seteazaCuloare(int culoare, String cText)
    {
        target.setBackgroundColor(culoare);
        String text = "#" + Integer.toHexString(culoare);
        text+=" - " + cText;
        cod.setText(text);
        alpha.setProgress(Color.alpha(culoare));
        red.setProgress(Color.red(culoare));
        green.setProgress(Color.green(culoare));
        blue.setProgress(Color.blue(culoare));
    }

}
