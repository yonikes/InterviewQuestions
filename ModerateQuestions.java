import java.util.*;

public class ModerateQuestions {

    public static void main(String[] args){

         /* All questions are from chapter 16 of 'Cracking the coding interview' - moderate questions */

        SwapNumersWithoutTemp();

        TicTacToe();

        FindSmallestArrayDifference();

        EnglishInt(-2003920200);

        MasterMind();

        SubSort();

        ContigousSequense();

        PondSizes();

        SumSwap();

        RandOf7FromRandOf5();

        PairsWithSum();
    }

    //question 16.1 from Cracking the coding interview - somewhat of a brain tisser                                                                                                                                                                                                                                                                      from Cracking the coding interview
    public static void SwapNumersWithoutTemp() {
        int input_a = 10;
        int input_b = -4;

        input_b = input_a - input_b; //b is now diff = original a - original b
        input_a = input_a - input_b; //a minus diff is by definition b
        input_b = input_a + input_b; //b plus diff is by definition a

        System.out.println("a = " + input_a + " , b = " + input_b);
    }

    //question 16.4 from Cracking the coding interview
    public static void TicTacToe() {

        //based on this input, determine who is the winner, if there is one
        int[][]input = {{0, 1, 0, 1},
                {0, 1, 1, 1},
                {1, 1, 0, 0},
                {1, 0, 1, 0}};

        //there is no algorythmic trick here, the point is how to do it cleanly
        //todo - implement using iterators

    }

    //question 16.6 from Cracking the coding interview
    public static void FindSmallestArrayDifference() {

        //given two arrays of int, find the smallest difference between any two numbers, one from each array
        int[]input_a = {1, 3, 15, 11, 2};
        int[]input_b = {23, 127, 235, 19, 8};

        //best we can do is NlogN + MlogM with sorting
        Arrays.sort(input_a);
        Arrays.sort(input_b);

        int a_index = 0, b_index = 0, min_difference = Integer.MAX_VALUE;
        while ((a_index < input_a.length) && (b_index < input_b.length)){
            int difference = Math.abs(input_a[a_index] - input_b[b_index]);
            if (difference < min_difference)
                min_difference = difference;
            if (input_a[a_index] < input_b[b_index])
                a_index++;
            else
                b_index++;
        }
        //note that there is no reason to continue, because the arrays are sorted, there is no  way we can improve on the the answer we have
        System.out.println(min_difference);
    }

    //question 16.8 from Cracking the coding interview
    public static void EnglishInt(int input) {

        //the goal is to print a string which is the English representation of the input number
        //there is no algoruthmic catch here, mostly to pay attention to details and all edge cases
        final String[]POWERS = {"", "thousand", "milion", "billion"};

        //Zero is a special case
        if (input == 0)
            System.out.println("zero");

        if (input < 0){
            System.out.print("Minus ");
            EnglishInt(-1 * input);
            return;
        }

        //divide the number into triplets
        ArrayList<Integer> triplets = new ArrayList<Integer>();
        while (input != 0){
            triplets.add(input % 1000);
            input /= 1000;
        }

        //for each triplet of digits, create an english representation
        ArrayList<String>triplets_representation = new ArrayList<String>();
        for (int triplet : triplets)
            triplets_representation.add(ConvertTripletToString(triplet));

        //concatenate them all
        int triplets_num = triplets.size();
        String ans = "";
        for (int i = triplets_num - 1; i >= 0; i--) {
            String s = triplets_representation.get(i);
            if (s != "") {
                if ((i < triplets_num - 1) && (i >= 1))
                    ans += ", ";
                else
                    ans += " ";
                if (i == triplets_num - 1)
                    s = s.substring(0, 1).toUpperCase() + s.substring(1);
                ans += s + POWERS[i];
            }
        }
        System.out.println(ans);
    }

    //assumption: 0<=n<=999
    private static String ConvertTripletToString(int n) {
        final String[] ONE_DIGIT_CONSTANTS = {" ", "one ", "two ", "three ", "four ", "five ", "six ", "seven ", "eight ", "nine "};
        final String[] TENS_CONSTANTS = {" ", " ", "twenty ", "thirty ", "forty ", "fifty ", "sixty ", "seventy ", "eighty ", "ninety "};
        final String[] TEENS_CONSTANTS = {"ten ", "eleven ", "twelve ", "thirteen ", "fourteen ", "fifteen ", "sixteen ", "seventeen ", "eighteen ", "nineteen "};

        String ans = "";

        //check hundreds digit
        if (n >= 100){
            int num_hundreds = n / 100;
            ans += ONE_DIGIT_CONSTANTS[num_hundreds] + "hundred ";
        }

        //move to last two digits
        n = n % 100;
        //teens are special..
        if ((10 <= n) && (n <= 19)){
            ans += TEENS_CONSTANTS[n - 10];
        }
        else{
            if (n >= 20){
                int num_tens = n / 10;
                ans += TENS_CONSTANTS[num_tens];
            }
            //move to last digit
            n = n % 10;
            if (n != 0)
                ans += ONE_DIGIT_CONSTANTS[n];
        }

        return ans;
    }

    //question 16.15 from Cracking the coding interview
    private static void MasterMind() {

        //given a guess and a solution, find how many hits and how many pseudo-hits
        String solution =   "BRYG";
        String guess =      "BGRB";

        int num_hits = 0, num_misses = 0;
        char[] guess_array = guess.toCharArray();
        char[] solution_array = solution.toCharArray();

        //a hashmap to contain the number of times each color appears in the solution
        Map<Character, Integer> map = new HashMap<>();
        for (char c : solution_array) {
            if (map.containsKey(c) == false)
                map.put(c, 1);
            else
                map.replace(c, (map.get(c) + 1));
        }

        //scan the guess
        for (int i = 0; i < guess.length(); i++){
            if (guess_array[i] == solution_array[i]) {
                num_hits++;
                map.replace(guess_array[i], map.get(guess_array[i]) - 1);
            }
            else{
                if ((map.containsKey(guess_array[i])) &&(map.get(guess_array[i]) != 0)){
                    num_misses++;
                    map.replace(guess_array[i], map.get(guess_array[i]) - 1);
                }
            }
        }

        System.out.println("The guess contains " + num_hits + " hits and " + num_misses + " misses");

    }


    //question 16.16 from Cracking the coding interview
    public static void SubSort() {

        //given an array, find the indexes i and j such that sorting arr[i....j] will cause arr to be sorted (while minimizing i and j of course)
        int[] input = {1, 4, 7, 6, 12, 15};

        if (input.length == 1){
            System.out.println("Already sorted");
            return;
        }

        //we want to split input into [start, middle, finish], where start and finish are already sorted
        //find the start and the finish of the middle part,
        int start_middle = FindStartMiddle(input);
        if (start_middle == input.length){
            System.out.println("Already sorted");
            return;
        }
        int end_middle = FindEndMiddle(input);

        //we want to know the max value in the first two sections:
        //we know that all the numbers in the 'finish' section must be larger than this value
        int max_value_start_and_middle = FindMaxValue(input, 0, end_middle);

        //we want to know the min value in the last two sections.
        //we know that all the numbers in the 'start' section must be smaller than this value
        int min_value_middle_and_end = FindMinValue(input, start_middle, input.length - 1);

        //we know we need to sort the middle.
        //enlarge this section we need to sort towards the start and end of the array, as long we encounter values out of order
        int left_range = start_middle, right_range = end_middle;
        //note that we know that we need to enlarge at least by one (that's how we found 'start_middle'. We could have written it differently, but this is nicer
        while ((left_range > 0) && (input[left_range - 1] > min_value_middle_and_end))
            left_range--;
        while ((right_range < input.length - 1) && (input[right_range + 1] < max_value_start_and_middle))
            right_range++;
        System.out.println(left_range + " , " + right_range);
    }

    private static int FindStartMiddle(int[] input) {
        int start_middle = 1;
        while ((start_middle < input.length) && (input[start_middle] > input[start_middle - 1]))
            start_middle++;
        return start_middle;
    }

    private static int FindEndMiddle(int[] input) {
        int end_middle = input.length - 2;
        while (input[end_middle] < input[end_middle + 1])
            end_middle--;
        return end_middle;
    }

    private static int FindMinValue(int[] input, int start, int finish) {
        int min = input[start];
        for (int i = start + 1; i <= finish; i++)
            min = Math.min(min, input[i]);
        return min;
    }

    private static int FindMaxValue(int[] input, int start, int finish) {
        int max = input[start];
        for (int i = start + 1; i <= finish; i++)
            max = Math.max(max, input[i]);
        return max;
    }

    //question 16.17 from Cracking the coding interview
    public static void ContigousSequense() {

        //given an array with positive and negative values, find the biggest sum in a contigous array
        int[] input = {-4, 3, 2, -1, 3, 2, -6, 5, 2};

        //find the start of the first positive sequence. If there is none, return the biggest negative number (zero included)
        //note that if we can agree that in case of all negative we'll return zero, this can be removed with a small change to the second part of the function
        int biggest_negative = Integer.MIN_VALUE;
        int start_positive_sequence = -1;
        for (int i = 0; i < input.length; i++){
            if (input[i] > 0){
                start_positive_sequence = i;
                break;
            }
            else
                biggest_negative = Math.max(biggest_negative, input[i]);
        }
        if (start_positive_sequence == -1) {
            System.out.println(biggest_negative);
            return;
        }

        int max_sum = 0;
        int current_sum = 0;
        int i = start_positive_sequence;
        while (i < input.length){
            current_sum += input[i];
            max_sum = Math.max(max_sum, current_sum);
            if (current_sum < 0)
                current_sum = 0;
            i++;
        }
        System.out.println(max_sum);
    }

    //question 16.19 from Cracking the coding interview
    public static void PondSizes() {

        //If 0 is water, find the sizes of all the ponds
        int[][]input = {{0, 2, 1, 0},
                        {0, 0, 0, 1},
                        {1, 1, 0, 1},
                        {0, 1, 0, 1}};

        ArrayList<Integer> sizes = new ArrayList<Integer>();
        for (int r = 0; r < input.length; r++)
            for (int c = 0; c < input[0].length; c++)
                if (input[r][c] == 0)
                    sizes.add(FindPondSize(input, r, c));
        for (int size : sizes)
            System.out.print(size + " ");

    }

    //a cool version without a 'visited' DS. The price is that we are modifying the 'input' matrix
    private static Integer FindPondSize(int[][] map, int r, int c) {

        if (r < 0 || r > map.length - 1 || c < 0 || c > map[0].length - 1)
            return 0;

        if (map[r][c] != 0)
            return 0;

        int count = 1;
        map[r][c] = -1;
        for (int row = r - 1; row <= r + 1; row++)
            for (int col = c - 1; col <= c + 1; col++)
                if (row != r || col != c)
                    count += FindPondSize(map, row, col);
        return count;
    }

    //question 16.21 from Cracking the coding interview
    public static void SumSwap() {

        //given two arrays, find 2 numbers (1 in each) whose swaping will cause the array's sizes to be equal
        int[] a = {4, 1 ,2 ,1 ,1 , 2}; //11
        int[] b = {3, 6, 3, 3}; //15

        //count the sum of a, while inserting its elements into a hashset
        Set<Integer> set = new HashSet<Integer>();
        int sum_a = 0;
        for (int val : a){
            sum_a += val;
            set.add(val);
        }
        //count the sum of b
        int sum_b = 0;
        for (int val : b)
            sum_b += val;


        int sum_difference = sum_a - sum_b; //note that we don't need to worry about the sign - negative will work out fine as well
        if (sum_difference % 2 == 1) {
            System.out.println("No such elements exits");
            return;
        }

        int element_difference = sum_difference / 2;
        for (int val : b) {
            if (set.contains(val + element_difference)) {
                System.out.println(val + element_difference + " , " + val);
                return;
            }
        }
    }

    //question 16.23 from Cracking the coding interview
    public static void RandOf7FromRandOf5() {

        //given a method that generated a random between 0 and 4 (inclusive), generate a random between 0 and 6 (inclusive)
        while (true){
            int attempt = (5 * rand5()) + rand5();

            //the idea to get the numbers [0-20] in equal probability, so we throw away larger numbers
            if (attempt >= 21)
                continue;

            System.out.println(attempt % 7);
            break;
        }
    }

    private static int rand5(){
        return (int)Math.floor(Math.random() * 6);
    }

    //question 16.24 from Cracking the coding interview
    public static void PairsWithSum() {

        //to avoid saving the results in form of arrays of size 2
        class Pair {
            int first;
            int second;

            public Pair(int first, int second){
                this.first = first;
                this.second = second;
            }

            public String toString(){
                return "(" + first + " , " + second + ")";
            }

            //a textbook example of how to implement 'equals'
            public boolean equals(Object other){

                if (other == this)
                    return true;

                if (other == null || !(other instanceof Pair))
                    return false;

                Pair other_pair = (Pair)other;
                return (    ((this.first == other_pair.first) || (this.first == other_pair.second))
                        &&  ((this.second == other_pair.first) || (this.second == other_pair.second)));
            }
        }
        //given an array, find all pairs whose sum is K
        int[] arr = {1, -8, 2, -3, 4, 7, 2, 3, 0, 1, 3, 7, -3};
        int k = 4;

        //my initial solution was to first input all elements into Hashmap, mapping from a value to its number
        //of its occurrences, and then scan the array looking for complements while decreasing the count (of both the value
        //and the complement) when we find a match.
        //this will work but it scans the map twice, which is unnecessary - we can do it in one.

        ArrayList<Pair> pairs = new ArrayList<>();
        Map<Integer, Integer> unpaired_elements_count = new HashMap<>();
        for (int val : arr){
            int complement = k - val;
            //note how the usage of getOrDefault replaces two checks - if the key exists, and what is its value
            if (unpaired_elements_count.getOrDefault(complement, 0) > 0){
                //found a match - remove count of the complement and add this pair to our results if it's not there already
                adjustCounterBy(unpaired_elements_count, complement, -1);
                Pair pair = new Pair(complement, val);
                if (!pairs.contains(pair))
                    pairs.add(pair);
            }
            else{
                //no match for this value - add it to the map
                if (unpaired_elements_count.getOrDefault(complement, 0) == 0)
                    unpaired_elements_count.put(val, 1);
                else
                    adjustCounterBy(unpaired_elements_count, val, 1);
            }

        }
        for (Pair pair : pairs)
            System.out.println(pair.toString());

        /*
        Map<Integer, Integer> elements_count = new HashMap<>();
        for (int val : arr){
            if (!elements_count.containsKey(val))
                elements_count.put(val, 1);
            else
                elements_count.put(val, elements_count.get(val) + 1);
        }


        for (int val : arr){
            int complement = k - val;
            if (elements_count.containsKey(complement) && elements_count.get(complement) != 0){
                //important to decrease #of both val and complement, to avoid multiple printings of identical values (2,2) in this example
                elements_count.put(val, elements_count.get(val) - 1);
                elements_count.put(complement, elements_count.get(complement) - 1);
                System.out.println(val + " , " + complement);
            }
        }
        */
    }

    //an auxiliary function that changes the value for a specific key in map according to adjustment
    private static void adjustCounterBy(Map<Integer, Integer> map, int key, int adjustment){
        map.put(key, map.get(key) + adjustment);
    }

}
