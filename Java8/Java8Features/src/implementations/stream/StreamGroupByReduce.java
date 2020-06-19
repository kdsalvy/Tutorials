package implementations.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamGroupByReduce {

    public static void main(String[] args) {
        List<Group> groupList = Arrays.asList(new Group("Car", "Hyundai", 5), new Group("Car", "Renault", 4), new Group("Car", "Honda", 6), new Group("Car", "Maruti", 7), new Group("Car", "Nissan", 9), new Group("Car", "Mercedes", 11),
            new Group("Car", "Porsche", 12), new Group("Car", "Lamborghini", 3), new Group("Car", "BMW", 18), new Group("Car", "Ford", 20));

        BinaryOperator<StockCount> listToStockCount = (StockCount stockA, StockCount stockB) -> {
            stockA.setStock(stockA.getStock() + stockB.getStock());
            stockA.setCompany(stockB.getCompany());
            return stockA;
        };

        Function<Group, StockCount> groupToStockCount = (Group group) -> new StockCount(group.getKey(), group.getCount());
        
        Function<Map.Entry<String, List<Group>>, StockCount> entryToStockCount = entry -> entry.getValue()
            .stream()
            .map(groupToStockCount)
            .reduce(new StockCount(), listToStockCount);

        long startTime = System.currentTimeMillis();
        
        Map<String, List<Group>> groupMap = groupList.stream()
            .collect(Collectors.groupingBy(Group::getKey));
        List<StockCount> stockListA = groupMap.entrySet()
            .stream()
            .map(entryToStockCount)
            .collect(Collectors.toList());
        
        long endTime = System.currentTimeMillis();
        System.out.println("time elapsed (in millis): " + (endTime - startTime));

        stockListA.stream()
            .forEach(System.out::println);

        startTime = System.currentTimeMillis();
        Collection<StockCount> stockCollection = groupList.stream()
            .collect(
                Collectors.groupingBy(
                    Group::getKey, Collectors.mapping(
                        groupToStockCount, Collectors.reducing(
                            new StockCount(), listToStockCount)))).values();

        endTime = System.currentTimeMillis();
        System.out.println("time elapsed (in millis): " + (endTime - startTime));

        stockCollection.stream()
            .forEach(System.out::println);
        stockCollection.removeAll(stockCollection);

    }

}

class StockCount {
    private String company;
    private Integer stock;

    public StockCount() {
        this.stock = 0;
    }

    public StockCount(String company, Integer stock) {
        this.company = company;
        this.stock = stock;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "StockCount [company=" + company + ", stock=" + stock + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((company == null) ? 0 : company.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StockCount other = (StockCount) obj;
        if (company == null) {
            if (other.company != null)
                return false;
        } else if (!company.equals(other.company))
            return false;
        return true;
    }

}

class Group {
    private String key;
    private String value;
    private Integer count;

    public Group(String key, String value, Integer count) {
        this.key = key;
        this.value = value;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Group [key=" + key + ", value=" + value + ", count=" + count + "]";
    }

}
