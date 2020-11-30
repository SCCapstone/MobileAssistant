package com.example.mobileassistant;

import android.os.AsyncTask;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class gsearch extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... query)
    {
        Document searches;
        String URL = "https://www.google.com/search?q="+query[0]+"&num="+1; //this is currently hard-coded to output just 1 result but I can change that
        try
        {
            searches = Jsoup.connect(URL).get();
        }
        catch(IOException e)
        {
            System.out.println("Failed to connect");
            return "";
        }
        String result = "";
        Elements outputs = searches.select("a[href]");
        System.out.println("Size of outputs : " + outputs.size());
        Element output = outputs.get(9);
        result=result+output.select("a[href]").text();
        return result;
    }
    /*
    public void makeSearch(String query) throws IOException
    {
        String URL = "https://www.google.com/search?q="+query+"&num="+1; //this is currently hard-coded to output just 1 result but I can change that
        Document searchs = Jsoup.connect(URL).userAgent("Mozilla/5.0").get();
        Elements outputs = searchs.select("h3.r > a");
        for(Element i : outputs) //coded with possible multiple result default in mind
        {
            String pageURL = outputs.attr("href");
            String pageName = outputs.text();
            System.out.println(pageName+" URL : "+pageURL.substring(6,pageURL.indexOf("&")));
        }
    }
     */
}
