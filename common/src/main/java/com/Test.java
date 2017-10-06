package com;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.thread.Trader;
import com.thread.Transaction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/7/25.
 */
public class Test {
    static String getname(Transaction transaction){
        return Optional.ofNullable(new Transaction(new Trader("Raoul", "Cambridge"), 2011, 300))
                .map(Transaction::getTrader)
                .map(Trader::getName)
                .orElse("Unknown");
    }

    public static void main(String[] args) {
        System.out.println(getname(null));

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        //1. Find all transactions in the year 2011 and sort them by value (small to high).
        List<Transaction> collect = transactions.stream().filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(collect);

        // 2. What are all the unique cities where the traders work?
        List<String> collect1 = transactions.stream().map(t -> t.getTrader().getCity()).distinct().collect(Collectors.toList());
        System.out.println(collect1);

        // 3. Find all traders from Cambridge and sort them by name.
        List<Transaction> collect2 = transactions.stream().filter(t -> "Cambridge".equals(t.getTrader().getCity()))
                .sorted(Comparator.comparing(t -> t.getTrader().getName()))
                .collect(Collectors.toList());
        System.out.println(collect2);

        // 4. Return a string of all traders’ names sorted alphabetically.
        List<String> collect3 = transactions.stream().map(t -> t.getTrader().getName()).sorted().collect(Collectors.toList());
        System.out.println(collect3);

        //5. Are any traders based in Milan?
        boolean milan = transactions.stream().map(Transaction::getTrader).anyMatch(t -> t.getCity().endsWith("Milan"));
        System.out.println(milan);

        //6. Print all transactions’ values from the traders living in Cambridge.
        List<Integer> cambridge = transactions.stream().filter(t -> t.getTrader().getCity().equals("Cambridge")).map(Transaction::getValue).collect(Collectors.toList());
        System.out.println(cambridge);

        //         7. What’s the highest value of all the transactions?
        Optional<Integer> reduce = transactions.stream().map(Transaction::getValue).reduce(Integer::max);
        reduce.ifPresent(System.out::println);

        Optional<Integer> reduce2 = transactions.stream().map(Transaction::getValue).reduce(Integer::min);
        reduce2.ifPresent(System.out::println);
    }
}
