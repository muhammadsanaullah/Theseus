javapackage Answer;
import Parser.JSONParse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import java.util.ArrayList;

public class DataMuseSolver {

    public static ArrayList<String> results;
    private JSONParse ParseJSON;

    public DataMuseSolver() {
        ParseJSON = new JSONParse();
        results = new ArrayList<String>();
    }


    public String findSimilarWords(String word) {
        String s;
        s = word.replaceAll(" ", "+");
        return getJSON("http://api.datamuse.com/words?rd=" + s);
    }

    private String getJSON(String url) {
        URL data_muse_url;
        URLConnection dc;
        StringBuilder s = null;
        try {
            data_muse_url = new URL(url);
            dc = data_muse_url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(dc.getInputStream(), "UTF-8"));
            String inputLine;
            s = new StringBuilder();
            while(!in.ready());
            while ((inputLine = in.readLine()) != null)
                s.append(inputLine);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s != null ? s.toString() : null;
    }

    public void checkDatamuse(String key, int size)
    {
        key = key.replaceAll(" ", "+");

        if(!results.isEmpty())
            results.clear();

        String json;
        json = findSimilarWords(key);
        if(json.equals("[]"))
        {	}
        else
        {
            results = ParseJSON.parseWords(json, size);
        }
        System.out.println("Datamuse hint search = " + key);
        System.out.println();
        System.out.println("JSON Format = " + json);
        System.out.println();
        System.out.println("Returned results for = " + results);
        System.out.println();
    }
}


