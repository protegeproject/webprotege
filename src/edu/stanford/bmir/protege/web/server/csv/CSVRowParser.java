package edu.stanford.bmir.protege.web.server.csv;

import au.com.bytecode.opencsv.*;
import au.com.bytecode.opencsv.CSVReader;
import edu.stanford.bmir.protege.web.shared.csv.CSVRow;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVRowParser {

    private String row;

    private int currentPos;

    public CSVRowParser(String row) {
        this.row = row;
        this.currentPos = 0;
    }

    public CSVRow parseRow() {
        return null;
    }


    public static void main(String[] args) throws Exception {

        au.com.bytecode.opencsv.CSVReader r = new CSVReader(new FileReader(new File("/tmp/airports.csv")));
        List<String[]> all = r.readAll();
        for(String [] row : all) {
            for (String col : row) {
                System.out.print(col);
                System.out.print("    ----    ");
            }
            System.out.println();
        }
    }

}
