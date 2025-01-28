package com.example.sportintelligencetesimolettadavide;

import android.content.Context;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {

    String FILE_NAME;
    View view;

    public FileOperations(String FILE_NAME, View view) {
        this.FILE_NAME = FILE_NAME;
        this.view = view;
    }

    //Trova il filtro ricercato e lo sostituisce con uno nuovo
    public void replaceFilter(String editFilter, String newFilter) {
        List<String> newFileData = new ArrayList<>();
        StringBuilder newFileDataString = new StringBuilder();
        boolean found = false;

        String fileData = load();

        String[] fileRows = fileData.split("\n");

        for (String fileRow : fileRows) {
            if (!fileRow.equals(editFilter) || found) {
                newFileData.add(fileRow);
            } else {
                found = true;
            }
        }

        newFileData.add(newFilter);

        for (String fileEntry : newFileData) {
            newFileDataString.append(fileEntry).append("\n");
        }

        overwriteFile(newFileDataString.toString());
    }

    //Carica tutti i dati presenti sul file di testo e li ritorna come stringa
    public String load() {
        FileInputStream fis = null;
        String fileData = "";

        try {
            fis = view.getContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            fileData += sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileData;
    }

    //Salva i dati ricevuti nel file senza sovrascrivere quelli già presenti
    public void save(String dataToAdd) {
        FileOutputStream fos = null;

        String fileData = load();
        String dataToWrite = fileData + dataToAdd;
        try {
            fos = view.getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(dataToWrite.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Sovrascrive il file eliminando tutti i dati e inserendo quelli ricevuti
    private void overwriteFile(String newFileDataString) {
        FileOutputStream fos = null;
        try {
            fos = view.getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(newFileDataString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Pulisce il file eliminando tutti i dati presenti
    public void clearFile() {
        FileOutputStream fos = null;

        try {
            fos = view.getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write("".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Creca un valore da eliminare e lo rimuove dal file lasciando inalterati gli altri dati
    public String searchAndDelete(String dataToDelete, String fileData) {
        List<String> newFileData = new ArrayList<>();
        StringBuilder newFileDataString = new StringBuilder();

        String[] fileRows = fileData.split("\n");

        for (String fileRow : fileRows) {
            if (!fileRow.equals(dataToDelete)) {
                newFileData.add(fileRow);
            }
        }

        for (String fileEntry : newFileData) {
            newFileDataString.append(fileEntry).append("\n");
        }

        overwriteFile(newFileDataString.toString());
        return newFileDataString.toString();
    }
}
