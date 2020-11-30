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
        Document searchs;
        String URL = "https://www.google.com/search?q="+query+"&num="+1; //this is currently hard-coded to output just 1 result but I can change that
        try
        {
            searchs = Jsoup.connect(URL).get();
        }
        catch(IOException e)
        {
            System.out.println("Failed to connect");
            return "";
        }
        String result = "";
        System.out.println("Printing toString for .get() : " + searchs.toString() + "\n");
        Elements outputs = searchs.select("a[href]");
        for(Element i : outputs) //coded with possibility of multiple result default in mind
        {
            String temp = i.attr("href");
            if(temp.startsWith("/url?q="))
            {
                result=result+temp+"\n";
            }
            //result = result +"\nTitle : "+i.text()+" URL : "+i.html();
        }
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
