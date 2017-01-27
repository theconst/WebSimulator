///*
// 
// */
//package ua.kpi.atep.services.impl;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//import java.util.stream.Collectors;
//import org.springframework.stereotype.Service;
//import ua.kpi.atep.services.CSVWriter;
//
///**
// *
// * @author Home
// */
//@Service
//public class CSVWriterImpl implements CSVWriter {
//
//    private static final String SEPARATOR = ",";
//
//    private static final String EMPTY = "_";
//
//    @Override
//    public CharSequence asCSV(Map<String, List<Double>> table) {
//        StringBuilder result = new StringBuilder();
//        Set<Entry<String, List<Double>>> entries = table.entrySet();
//
//        entries.forEach(x -> result.append(x.getKey()).append(SEPARATOR));
//        result.append(System.lineSeparator());
//
//        List<Iterator<Double>> iterators = entries.stream()
//                .map(x -> x.getValue().iterator()).collect(Collectors.toList());
//
//        while (iterators.stream().anyMatch(Iterator::hasNext)) {
//
//            /* val1,val2,val3, valn */
//            iterators.forEach(
//                    it -> result.append(it.hasNext() ? it.next() : EMPTY)
//                            .append(SEPARATOR)
//            );
//
//            /* \n */
//            result.append(System.lineSeparator());
//        }
//
//        return result;
//    }
//
//}
