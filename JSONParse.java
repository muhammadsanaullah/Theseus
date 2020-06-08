package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.json.parsers.JsonParserFactory;
import com.json.parsers.JSONParser;

public class JSONParse {

    public ArrayList<String> parseWords(String in, int size) {


        JsonParserFactory factory=JsonParserFactory.getInstance();
        JSONParser parser=factory.newJsonParser();
        Map jsonData=parser.parseJson(in);
        List al= (List) jsonData.get("root");
        String currentStr = "";
        ArrayList<String> results = new ArrayList<String>();
        for (int i = 0; i < al.size(); i++) {
            currentStr = (String) ((Map)al.get(i)).get("word");
            if((currentStr.length() == size))
            {
                currentStr.replaceAll("\"", "");
                currentStr.replaceAll("!", "");
                currentStr.replaceAll("\\?", "");
                results.add(currentStr);
            }
        }
        return results;
    }
    public int[] parseScores(String in) {
        JsonParserFactory factory=JsonParserFactory.getInstance();
        JSONParser parser=factory.newJsonParser();
        Map jsonData=parser.parseJson(in);
        List al= (List) jsonData.get("root");
        int[] results = new int[al.size()];
        for (int i = 0; i < al.size(); i++) {
            results[i] = Integer.parseInt((String) ((Map) al.get(i)).get("score"));
        }
        return results;
    }
}
