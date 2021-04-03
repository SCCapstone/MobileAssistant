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
        String numresults = "1";
        boolean toolongflag = false;
        if(query[0].contains("search") || query[0].contains("Google") || query[0].contains("look up"))
        {
            query[0] = query[0].replace("search ","");
            query[0] = query[0].replace("Google ","");
            query[0] = query[0].replace("look up ","");
            query[0] = query[0].replace("search","");
            query[0] = query[0].replace("Google","");
            query[0] = query[0].replace("look up","");
            query[0]=query[0].trim();
        }
        if(query[0].contains("top") || query[0].contains("results") && query[0].contains("1") || query[0].contains("2") || query[0].contains("3") || query[0].contains("4") || query[0].contains("5") || query[0].contains("6") || query[0].contains("7") || query[0].contains("8") || query[0].contains("9") || query[0].contains("0"))
        {
            query[0] = query[0].replace("top ","");
            query[0] = query[0].replace("results ","");
            query[0] = query[0].replace("top","");
            query[0] = query[0].replace("results","");
            for(int i=0;i<query[0].length();i++)
            {
                String temp = query[0];
                if(temp.charAt(i)=='1' || temp.charAt(i)=='2' || temp.charAt(i)=='3' || temp.charAt(i)=='4' || temp.charAt(i)=='5' || temp.charAt(i)=='6' || temp.charAt(i)=='7' || temp.charAt(i)=='8' || temp.charAt(i)=='9' || temp.charAt(i)=='0')
                {
                    for(String piece: temp.split(" "))
                    {
                        if(piece.charAt(0)=='1'||piece.charAt(0)=='2'||piece.charAt(0)=='3'||piece.charAt(0)=='4'||piece.charAt(0)=='5'||piece.charAt(0)=='6'||piece.charAt(0)=='7'||piece.charAt(0)=='8'||piece.charAt(0)=='9'||piece.charAt(0)=='0')
                        {
                            numresults=piece;
                        }
                    }
                }
            }
            String temp2=numresults+" ";
            query[0] = query[0].replaceAll(temp2,"");
            query[0]=query[0].trim();
            int nresultstemp=Integer.parseInt(numresults);
            nresultstemp++;
            numresults=String.valueOf(nresultstemp);
        }
        if((query[0].contains("news") || query[0].contains("headlines")) && Integer.parseInt(numresults) <= 5)
        {
            numresults = "6";
        }
        Document searches;
        String URL = "https://www.google.com/search?q="+query[0]+"&num="+numresults;//this is currently hard-coded to output just 1 result but I can change that
        try
        {
            searches = Jsoup.connect(URL).get();
        }
        catch(IOException e)
        {
            System.out.println("Failed to connect");
            return "Sorry, my API failed to connect. Are you sure you are connected to the internet?";
        }
        String result = "";
        Elements outputs = searches.select("a[href]");
        int numresultsint = Integer.parseInt(numresults);
        for(int i=9;i<9+numresultsint;i++)
        {
            Element output=outputs.get(i);
            String temp = output.attr("href");
            String finaltitle="";
            if(temp.contains("https"))
            {
                String s1=output.text();
                int s2end = s1.indexOf(".");
                for(int j=s2end;j>1;j--)
                {
                    if(s1.charAt(j)==' ')
                    {
                        finaltitle=s1.substring(0,j);
                        break;
                    }
                }
            }
            if(finaltitle.equals(""))
            {
                String stringtemp=output.text();
                stringtemp=stringtemp.trim();
                finaltitle=stringtemp;
            }
            if(temp.startsWith("/search?q=") || temp.startsWith("https://support.google") || temp.startsWith("https://www.google.com") || temp.startsWith("#") || temp.startsWith("/preferences") || temp.startsWith("https://policies") || temp.startsWith("https://maps.google.com") || temp.contains("adurl")) //eliminating all of googles tertiary links on the page
            {
                //do nothing
            }
            else
            {
                temp=temp.split("/search\\?")[0];
                if(temp.contains("https"))
                {
                    result=result+finaltitle+" -\n";
                    result=result+temp+"\n\n";
                }
            }
        }
        result=result.trim();
        if(result.length()<=5) //backup plan to ensure no empty results
        {
            result=result+"Results :\n\n";
            int count = 1;
            for(int i=0;i<outputs.size();i++)
            {
                Element output=outputs.get(i);
                String temp = output.attr("href");
                if(temp.startsWith("/search?q=") || temp.startsWith("https://support.google") || temp.startsWith("https://www.google.com") || temp.startsWith("#") || temp.startsWith("/preferences") || temp.startsWith("https://policies") || temp.startsWith("https://maps.google.com") || temp.contains("adurl")) //eliminating all of googles tertiary links on the page
                {
                    //do nothing
                }
                else
                {
                    temp=temp.split("/search\\?")[0];
                    if(temp.contains("https"))
                    {
                        result=result+"Result "+count+" -\n";
                        count++;
                        result=result+temp;
                        result=result+"\n\n";
                    }
                }
            }
            if(count>=numresultsint+10)
            {
                toolongflag=true;
            }
        }
        result=result.trim();
        String retURL = URL.replaceAll(" ","+");
        if(result.length()<=10) //if still empty even after backup plan, apologize and give the URL of the google search
        {
            result="I'm sorry, my API seems to have been unable to resolve your request. Try this - \n" + retURL;
        }
        if(toolongflag)
        {
            result="I'm sorry, advertisements seem to be intruding on your google search. Please retry your search or try this link - \n" + retURL;
        }
        return result.trim();
    }
}
