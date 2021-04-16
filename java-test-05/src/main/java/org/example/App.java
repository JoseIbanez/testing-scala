package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        list();

    }

    public static void list() {

        List<String> l1 = new ArrayList<String>();

        l1.add("aaa");
        l1.add("hi");
        l1.add("there");
        l1.add("bye");

        l1.removeIf( x -> x.equals("hi"));

        l1.forEach( i -> {
            System.out.println(i);
        });

        l1.stream()
                .map( i -> "i:"+i  )
                .forEach( x -> {System.out.println(x);});

    }
}
