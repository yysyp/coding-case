package ps.demo.commonlibx.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamTool {

    //List<Person> distinctElements = list.stream()
    //      .filter( distinctByKey(p -> p.getFname() + " " + p.getLname()) )
    //      .collect( Collectors.toList() );
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    //List<Record> list = recordsList
    //     .stream()
    //     .filter(distinctByKeys(Record::getId, Record::getName))
    //     .collect(Collectors.toList());
    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t -> {
            final List<?> keys = Arrays.stream(keyExtractors).map(ke -> ke.apply(t)).collect(Collectors.toList());
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }

//    Map<String, List<BigDecimal>> grouped = recMods.stream().collect(Collectors.groupingBy(
//    e -> {
//        return e.getSeq() + e.getName();
//    },
//    Collectors.collectingAndThen(Collectors.toList(), list -> {
//        ArrayList list1 = new ArrayList();
//        BigDecimal sum = list.stream().map(RecMod::getJq).reduce(BigDecimal.ZERO, BigDecimal::add);
//        list1.add(sum);
//        sum = list.stream().map(RecMod::getHc).reduce(BigDecimal.ZERO, BigDecimal::add);
//        list1.add(sum);
//        sum = list.stream().map(RecMod::getDyfw).reduce(BigDecimal.ZERO, BigDecimal::add);
//        list1.add(sum);
//        return list1;
//    })));

}
