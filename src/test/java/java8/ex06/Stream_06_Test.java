package java8.ex06;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/**
 * Exercice 06 - Stream Parallel
 */
public class Stream_06_Test {

    private static final long NB = 10_000_000;

    // Soit une méthode impérative qui permet de construire une somme des chiffres de 1 à n
    private long imperativeSum(long n) {
        long result = 0;

        for (long i = 1L; i < n; i++) {
            result += i;
        }
        return result;
    }

    // TODO compléter la méthode iterateSum
    // TODO utiliser la méthode Stream.iterate
    // TODO cette méthode doit produire le même résultat que imperativeSum
    private long iterateSum(long n) {
    	long result = Stream.iterate(1L,i->i+1).limit(n-1).collect(Collectors.summingLong(l -> l.longValue()));
        return result;
    }

    // TODO exécuter le test pour vérifier que les méthodes imperativeSum et iterateSum produisent le même résultat
    @Test
    public void test_imperativeSum_vs_iterateSum() {

        Stream.of(1L, 1000L, NB).forEach(n -> {
            long result1 = imperativeSum(n);
            long result2 = iterateSum(n);
            assertThat(result1, is(result2));
        });
    }

    // TODO compléter la méthode parallelIterateSum
    // TODO utiliser la méthode Stream.iterate
    // TODO transformer en stream parallel (.parallel())
    private long parallelIterateSum(long n) {
        return Stream.iterate(1L,i->i+1).limit(n-1).parallel().collect(Collectors.summingLong(l -> l.longValue()));
    }

    // TODO exécuter le test pour vérifier que les méthodes imperativeSum, iterateSum et parallelIterateSum produisent le même résultat
    @Test
    public void test_imperativeSum_vs_iterateSum_vs_parallelIterateSum() {

        Stream.of(1L, 1000L, NB).forEach(n -> {
            long result1 = imperativeSum(n);
            long result2 = iterateSum(n);
            long result3 = parallelIterateSum(n);

            assertThat(result1, is(result2));
            assertThat(result1, is(result3));
        });
    }

    // Essayons maintenant d'avoir une indication sur les performances des 3 traitements

    // Voici une méthode qui exécute 10 fois un traitement et retourne le meilleur temps (le plus court)
    private long monitor(Consumer<Long> fn, long n) {

        long fastest = Long.MAX_VALUE;

        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            fn.accept(n);
            long end = System.nanoTime();
            long duration = (end - start) / 1_000_000;
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    // TODO compléter le test pour invoquer la méthode monitor dans chaque cas
    // TODO visualiser les temps d'exécution
    @Test
    public void monitor_imperativeSum_vs_iterateSum_vs_parallelIterateSum() {
    	Map<String,Long> result = new HashMap<String,Long>();
    	result.put("imperativeSum", monitor(c -> imperativeSum(NB),1000L));
    	result.put("iterateSum", monitor(c -> iterateSum(NB),1000L));
    	result.put("parallelIterateSum", monitor(c -> parallelIterateSum(NB),1000L));
    	
    	Optional<Entry<String, Long>> gagant = result.entrySet().stream().min((p1,p2) -> Long.compare(p1.getValue(),p2.getValue()));
    	System.out.println("Sur ma machine, le gagnan est ... " + gagant.get().getKey());

        Logger.getAnonymousLogger().info("imperativeSum => " + result.get("imperativeSum") +" ms");
        Logger.getAnonymousLogger().info("iterateSum => " + result.get("iterateSum") + " ms");
        Logger.getAnonymousLogger().info("parallelIterateSum => " + result.get("parallelIterateSum") +  " ms");
    }

    // Quel résultat obtenez-vous ?
    // Sur ma machine, le gagnant est ... imperativeSum !
    // INFO: imperativeSum => 3 ms
    // INFO: iterateSum => 103 ms
    // INFO: parallelIterateSum => 103 ms

    // Ecrivons à présent, autrement cette somme

    // TODO compléter la méthode rangeSum
    // TODO utiliser la méthode LongStream.rangeClosed
    private long rangeSum(long n) {
        return LongStream.rangeClosed(1L,n-1).sum();
    }

    // TODO vérifier que l'implémentation de rangeSum
    @Test
    public void test_imperativeSum_vs_rangeSum() {
        Stream.of(1L, 20L, 1000L, NB).forEach(n -> {
            long result1 = imperativeSum(n);
            long result2 = rangeSum(n);
            System.out.println("result1: "+ result1);
            System.out.println("result2: "+ result2);

            assertThat(result1, is(result2));
        });
    }

    // TODO compléter la méthode rangeSum
    // TODO utiliser la méthode LongStream.rangeClosed
    // TODO transformer en stream parallel (.parallel())
    private long rangeParallelSum(long n) {
        return LongStream.rangeClosed(1L,n-1).parallel().sum();
    }

    // TODO vérifier que l'implémentation de rangeParallelSum
    @Test
    public void test_imperativeSum_vs_rangeSum_vs_rangeParallelSum() {

        Stream.of(1L, 20L, 1000L, NB).forEach(n -> {
            long result1 = imperativeSum(n);
            long result2 = rangeSum(n);
            long result3 = rangeParallelSum(n);

            assertThat(result1, is(result2));
            assertThat(result1, is(result3));
        });
    }

    @Test
    public void monitor_imperativeSum_vs_iterateSum_vs_parallelIterateSum_vs_rangeSum_vs_rangeParallelSum() {
    	Map<String,Long> result = new HashMap<String,Long>();
    	result.put("imperativeSum", monitor(c -> imperativeSum(NB),1000L));
    	result.put("iterateSum", monitor(c -> iterateSum(NB),1000L));
    	result.put("parallelIterateSum", monitor(c -> parallelIterateSum(NB),1000L));
    	result.put("rangeSum", monitor(c -> rangeSum(NB),1000L));
    	result.put("rangeParallelSum", monitor(c -> rangeParallelSum(NB),1000L));
    	
    	Optional<Entry<String, Long>> gagant = result.entrySet().stream().min((p1,p2) -> Long.compare(p1.getValue(),p2.getValue()));
    	
    	System.out.println("Sur ma machine, le gagnan est ... " + gagant.get().getKey());
        Logger.getAnonymousLogger().info("imperativeSum => " + result.get("imperativeSum") + " ms");
        Logger.getAnonymousLogger().info("iterateSum => " + result.get("iterateSum") + " ms");
        Logger.getAnonymousLogger().info("parallelIterateSum => " + result.get("parallelIterateSum") +  " ms");
        Logger.getAnonymousLogger().info("rangeSum => " + result.get("rangeSum") + " ms");
        Logger.getAnonymousLogger().info("rangeParallelSum => "+ result.get("rangeParallelSum") + " ms");
    }

    // Quel résultat obtenez-vous ?
    // Sur ma machine, le gagnant est ... rangeParallelSum !
    // INFO: imperativeSum => 3 ms
    // INFO: iterateSum => 100 ms
    // INFO: parallelIterateSum => 90 ms
    // INFO: rangeSum => 4 ms
    // INFO: rangeParallelSum => 1 ms

    // Les performances de traitements en parallèle dépendent de la capacité d'une structure à se décomposer.
    // Stream.iterate() conçu pour générer un flux continue infinie ne se décompose pas alors qu'une structure finie comme
    // LongStream.rangeClosed se décompose aisément.
}
