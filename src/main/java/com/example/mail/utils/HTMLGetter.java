package com.example.mail.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class HTMLGetter implements CommandLineRunner {

    private final HashMap<String, String> previous = new HashMap<>();
    private final HashMap<String, String> current = new HashMap<>();

    /**
     * @return modified urls
     */
    public HashMap<String, String> getModifiedUrls(){
        HashMap<String, String> result = new HashMap<>();
        previous.forEach((key, value)->{
            if (current.containsKey(key)) {
                if(!current.get(key).equals(value)) {
                    System.out.println(value);
                    System.out.println(current.get(key));
                    result.put(key, value);
                }
            }
        });
        return result;
    }

    /**
     * @return deleted urls
     */
    public HashMap<String, String> getMissingUrls(){
        return findMissingValues(previous, current);
    }

    /**
     * @return new urls
     */
    public HashMap<String, String> getNewUrls(){
        return findMissingValues(current, previous);
    }

    /**
     * This method working like ArrayList.removeAll(). Here created new HashMap from
     * values in first table, which missing in second table
     * @param first first table
     * @param second second table
     * @return new HashMap
     */
    private HashMap<String, String> findMissingValues(HashMap<String, String> first,
                                              HashMap<String, String> second){
        HashMap<String, String> result = new HashMap<>();
        first.forEach((key, value)->{
            if (!second.containsKey(key)) {
                result.put(key, value);
            }
        });
        return result;
    }

    /**
     * Run with program and fill data to HashMaps
     * @param args
     */
    @Override
    public void run(String[] args){
        ArrayList<String> urls = new ArrayList<>();

        urls.add("https://docs.scala-lang.org/scala3/book/taste-intro.html");
        urls.add("https://docs.scala-lang.org/scala3/book/why-scala-3.html");
        urls.add("https://reactjs.org/");
        urls.add("https://reactjs.org/docs/getting-started.html");

        // filling table with previous day data
        urls.forEach(url -> previous.put(url, getHTML(url)));

        // Remove one for differences
        urls.remove(2);

        urls.add("https://javadevblog.com/raznitsa-mezhdu-hashtable-i-hashmap-v-java.html");
        urls.add("https://stackoverflow.com/");

        // Filling table with current day data
        urls.forEach(url -> current.put(url, getHTML(url)));
    }

    /**
     * Get HTML code from url
     * @param strUrl URL of page
     * @return HTML to String
     */
    private String getHTML(String strUrl){
        URL url;

        try {
            // get URL content
            url = new URL(strUrl);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
