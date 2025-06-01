package Example;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map;
import java.util.regex.*;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.DefaultPieDataset;

public class ExpenseCount {

    public static List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {

        List<String> mainList = new ArrayList<>();

        List<Integer> baba = new ArrayList<>();
        List<Integer> familyFood = new ArrayList<>();
        List<Integer> familyOzon = new ArrayList<>();
        List<Integer> myFood = new ArrayList<>();;
        List<Integer> cafe = new ArrayList<>();
        List<Integer> car = new ArrayList<>();
        List<Integer> otherExpense = new ArrayList<>();
        List<Integer> unknownExpense = new ArrayList<>();

        List<List<Integer>> tagList = List.
                of(baba, familyFood, familyOzon, myFood, cafe, car, otherExpense, unknownExpense);

        Map<String, List<Integer>> dateLists = new LinkedHashMap<>();
        dateLists.put("d0", new ArrayList<>());

        try {
            String filePath = "D:/Tasks/Expense_count/expenses.txt";
            mainList = readLines(filePath);
        }
        catch (IOException e)  {
            System.err.println("Read file error: " + e.getMessage());
        }

        System.out.println("List is ready: " + mainList);

        String lastKey = "d0";

        //Создаю паттерн даты
        Pattern date_pattern = Pattern.compile("\\d+\\.\\d+");
        //Создаю паттерн суммы + тэга
        Pattern amount_tag_pattern = Pattern.compile
                ("(\\d+\\s*-?\\s*[а-яА-ЯёЁ]+)|(\\d+\\s*)");

        //
        // ниже цикл парсинга ПО Датам
        //

        System.out.println("\n\n\n\t\tLet's begin DATE parsing!\n\n\n");

        List<String> dateValues = new ArrayList<>();

        for (String line : mainList) {
            System.out.println(line);
            Matcher date_matcher = date_pattern.matcher(line);
            Matcher amount_tag_matcher = amount_tag_pattern.matcher(line);
            String key;

            if (date_matcher.find()) {
                System.out.println("The date_pattern was found: " + date_matcher.group());
                key = "date " + line;
                dateLists.put(key, new ArrayList<>());
                lastKey = key;
                dateValues.add(lastKey);
            }
            else if (amount_tag_matcher.find()) {
                System.out.println("The amount_tag_pattern was found: "
                        + amount_tag_matcher.group());

                // Строчка ниже - мы явно преобразуем parseInt String, который
                // забирается matcher, в Integer, отрезая все другие, кроме Int, символы.
                Integer Amount_value = Integer.parseInt(amount_tag_matcher.group()
                        .replaceAll("[^\\d]", ""));

                // Добавляем это значение в список даты, сохраняемой в lastKey
                dateLists.get(lastKey).add(Amount_value);

            }

            else {
                System.out.println("The unknown line was found: " + line);
            }
        }

        for (String key : dateLists.keySet()) {
            System.out.println("Date_list " + key + " was created");
            System.out.println(dateLists);
        }


        //
        // ниже цикл парсинга ПО ТЭГАМ
        //

        //Создаю паттерн Машина
        Pattern car_pattern = Pattern.compile
                ("(\\d+)\\s*-?\\s*(машина|тачка|бензин|бенз)",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        //Создаю паттерн Бабушка
        Pattern baba_pattern = Pattern.compile
                ("(\\d+)\\s*-?\\s*(баба|бабушка|бабушке|бабе)",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        //Создаю паттерн для Кафе и Прогулок
        Pattern cafe_pattern = Pattern.compile
                ("(\\d+)\\s*-?\\s*(кафе|кафешка|прогулка)",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        //Создаю паттерн для Моей еды
        Pattern my_food_pattern = Pattern.compile
                ("(\\d+)\\s*-?\\s*(мояеда|(моя\\s*еда))",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        //Создаю паттерн для еды семьи
        Pattern family_food_pattern = Pattern.compile
                ("^\\s*(\\d+)\\s*$");


        //Создаю паттерн для Быта семьи на маркет-плейсах
        Pattern family_ozon_pattern = Pattern.compile
                ("(\\d+)\\s*-?\\s*(Озон\\s*наш)",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        //Создаю паттерн для Других трат
        Pattern other_pattern = Pattern.compile
                ("(\\d+)\\s*-?\\s*(\\p{IsCyrillic}+(?:\\s+\\p{IsCyrillic}+)*)",
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        System.out.println("\n\n\n\t\tLet's begin TAG parsing!\n\n\n");


        for (String line : mainList) {
            System.out.println(line);

            Matcher car_matcher = car_pattern.matcher(line);
            Matcher baba_matcher = baba_pattern.matcher(line);
            Matcher cafe_matcher = cafe_pattern.matcher(line);
            Matcher my_food_matcher = my_food_pattern.matcher(line);
            Matcher family_food_matcher = family_food_pattern.matcher(line);
            Matcher family_ozon_matcher = family_ozon_pattern.matcher(line);
            Matcher other_matcher = other_pattern.matcher(line);

            if (car_matcher.find()) {
                System.out.println("The CAR_pattern was found: " + car_matcher.group());
                Integer cost = Integer.parseInt(car_matcher.group(1));
                car.add(cost);
            }

            else if (baba_matcher.find()) {
                System.out.println("The BABA_pattern was found: " + baba_matcher.group());
                Integer cost = Integer.parseInt(baba_matcher.group(1));
                baba.add(cost);
            }

            else if (cafe_matcher.find()) {
                System.out.println("The CAFE_pattern was found: " + cafe_matcher.group());
                Integer cost = Integer.parseInt(cafe_matcher.group(1));
                cafe.add(cost);
            }

            else if (my_food_matcher.find()) {
                System.out.println("The MY_FOOD_pattern was found: " + my_food_matcher.group());
                Integer cost = Integer.parseInt(my_food_matcher.group(1));
                myFood.add(cost);
            }

            else if (family_ozon_matcher.find()) {
                System.out.println("The FAM_OZON_pattern was found: " + family_ozon_matcher.group());
                Integer cost = Integer.parseInt(family_ozon_matcher.group(1));
                familyOzon.add(cost);
            }

           else if (family_food_matcher.find()) {
                System.out.println("The FAM_FOOD_pattern was found: " + family_food_matcher.group());
                Integer cost = Integer.parseInt(family_food_matcher.group(1));
                familyFood.add(cost);
            }


            else if (other_matcher.find()) {
                System.out.println("The OTHER_pattern was found: " + other_matcher.group());
                Integer cost = Integer.parseInt(other_matcher.group(1));
                otherExpense.add(cost);
            }

           else {
                System.out.println("The unknown line was found: " + line);
                // Пытаемся вытащить первое число из строки
                Pattern int_pattern = Pattern.compile("(\\d+)");
                Matcher int_matcher = int_pattern.matcher(line);
                if (int_matcher.find()) {
                    int unknownAmount = Integer.parseInt(int_matcher.group(1));
                    unknownExpense.add(unknownAmount);
                    System.out.println("Extracted integer from unknown line: " + unknownAmount);
                } else {
                    System.out.println("No number found in unknown line.");
                }
            }
        }

        System.out.println("\n******\n\t\tLet's see TAG lists!\n******\n");

        // Цикл ниже считает полную сумму денег во всех категориях

        List<Integer> sumTag = new ArrayList<>();
        Map<String, Double> tagPercents = new HashMap<>();

        for (int i=0; i < tagList.size(); i++){
            int sum = tagList.get(i).stream().mapToInt(Integer::intValue).sum();
            sumTag.add(sum);
        }
        double sum_of_tags = sumTag.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total sum for period from " + dateValues.get(0) + " to "
                + dateValues.get(dateValues.size() -1) + ": \n"+ sum_of_tags + ", 100 % \n");

        List<Double> procents = new ArrayList<>(); // Список для проверки суммы процентов - 100%

        double sum_car = car.stream().mapToInt(Integer::intValue).sum();
        double proc_car = Math.round((sum_car/sum_of_tags)*100 * 10.0) / 10.0;
        procents.add(proc_car);
        System.out.println("The CAR list of amount: " + car + "\nSum: " + sum_car + ", " + proc_car + " %\n");


        double sum_baba = baba.stream().mapToInt(Integer::intValue).sum();
        double proc_baba = Math.round((sum_baba/sum_of_tags)*100 * 10.0) / 10.0;
        procents.add(proc_baba);
        System.out.println("The GRANNY list of amount: " + car + "\nSum: " + sum_baba + ", " + proc_baba + " %\n");

        double sum_ffood = familyFood.stream().mapToInt(Integer::intValue).sum();
        double proc_ffood = Math.round((sum_ffood/sum_of_tags)*100 * 10.0) / 10.0;
        procents.add(proc_ffood);
        System.out.println("The FAMILY FOOD list of amount: "
                + familyFood + "\nSum: " + sum_ffood + ", " + proc_ffood + " %\n");

        double sum_fam_ozon= familyOzon.stream().mapToInt(Integer::intValue).sum();
        double proc_fam_ozon = Math.round((sum_fam_ozon/sum_of_tags)*100 * 10.0) / 10.0;
        procents.add(proc_fam_ozon);
        System.out.println("The FAMILY OZON list of amount: "
                + familyOzon + "\nSum: " + sum_fam_ozon + ", " + proc_fam_ozon + " %\n");

        double sum_my_food = myFood.stream().mapToInt(Integer::intValue).sum();
        double proc_my_food = Math.round((sum_my_food/sum_of_tags)*100 * 10.0) / 10.0;
        procents.add(proc_my_food);
        System.out.println("The MY FOOD list of amount: "
                + myFood + "\nSum: " + sum_my_food + ", " + proc_my_food + " %\n");

        double sum_cafe = cafe.stream().mapToInt(Integer::intValue).sum();
        double proc_cafe = Math.round((sum_cafe/sum_of_tags)*100 * 10.0) / 10.0;
        procents.add(proc_cafe);
        System.out.println("The CAFE list of amount: "
                + cafe + "\nSum: " + sum_cafe + ", " + proc_cafe + " %\n");

        double sum_other = otherExpense.stream().mapToInt(Integer::intValue).sum();
        double proc_other = Math.round((sum_other/sum_of_tags)*100 * 10.0) / 10.0;
        procents.add(proc_other);
        System.out.println("The OTHER list of amount: "
                + otherExpense + "\nSum: " + sum_other + ", " + proc_other + " %\n");

        double sum_unknown = unknownExpense.stream().mapToInt(Integer::intValue).sum();
        double proc_unknown = Math.round((sum_unknown / sum_of_tags) * 100 * 10.0) / 10.0;
        procents.add(proc_unknown);
        System.out.println("The UNKNOWN list of amount: "
                + unknownExpense + "\nSum: " + sum_unknown + ", " + proc_unknown + " %\n");

        // Map <String, Double> с процентами по тэгам для ДИАГРАММЫ

        tagPercents.put("Бабушка", proc_baba);
        tagPercents.put("Семья: быт", proc_fam_ozon);
        tagPercents.put("Семья: еда", proc_ffood);
        tagPercents.put("Моя еда", proc_my_food);
        tagPercents.put("Кафе", proc_cafe);
        tagPercents.put("Машина", proc_car);
        tagPercents.put("Другие", proc_other);
        tagPercents.put("Неизвестные", proc_unknown);

        System.out.println("This is percents of tags: "+tagPercents+"\n\n");

        ExpensePieChart chart = new ExpensePieChart("Анализ расходов", tagPercents);
        chart.pack();
        chart.setVisible(true);

        // Проверка, что сумма процентов во всех TAGS = 100%
        double sum_procents = procents.stream().mapToDouble(Double::doubleValue).sum();
        long rounded = Math.round(sum_procents);
        if (rounded == 100) {
            System.out.println("Procent Assertation...\nOKAY! Sum of procent values = 100%\n");
        }
        else {
            System.out.println("WARNING! Sum of procent values is NOT 100%");
        }
        // Проверка закончена
    }

    // ДАЛЕЕ СЛЕДУЕТ КОД ОПИСАНИЯ ДИАГРАММЫ

    public static class ExpensePieChart extends ApplicationFrame {

        public ExpensePieChart(String title, Map<String, Double> data) {
            super(title);
            DefaultPieDataset dataset = new DefaultPieDataset();

            for (Map.Entry<String, Double> entry : data.entrySet()) {
                dataset.setValue(entry.getKey(), entry.getValue());
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Распределение расходов", // заголовок
                    dataset,
                    true, // показать легенду
                    true,
                    false
            );

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
            setContentPane(chartPanel);

        }

    }

    //  КОНЕЦ ОПИСАНИЕ ДИАГРАММЫ

}
